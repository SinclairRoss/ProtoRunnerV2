package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ai.VehicleInfo;
import com.raggamuffin.protorunnerv2.ai.VehicleInfo.AfterBurnerStates;
import com.raggamuffin.protorunnerv2.ai.VehicleInfo.MovementStates;
import com.raggamuffin.protorunnerv2.colours.ColourBehaviour_LerpTo;
import com.raggamuffin.protorunnerv2.colours.ColourBehaviour;
import com.raggamuffin.protorunnerv2.colours.ColourBehaviour_Flicker;
import com.raggamuffin.protorunnerv2.colours.ColourBehaviour_Pulse;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Burst;
import com.raggamuffin.protorunnerv2.pubsub.InternalPubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.InternalTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.utils.DecayCounter;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.Weapon;
import com.raggamuffin.protorunnerv2.weapons.Weapon_None;

public abstract class Vehicle extends GameObject
{
	private final double DAMAGE_DECAY_RATE = 2.0;
	
	///// Motion Attributes
	protected Engine m_Engine;
	protected Publisher m_OnDeathPublisher;

	////// Vehicle Attributes
	protected int m_HullPoints;			// How much damage that this ship can take.
	protected int m_MaxHullPoints;		// The maximum available hullpoints.

	///// Misc Attributes
    protected Weapon m_Utility;
	protected Weapon m_PrimaryWeapon;
	protected Boolean m_LasersOn;
	private VehicleInfo m_VehicleInfo;
	private DecayCounter m_DamageDecayCounter;
	
	///// Colour Attributes
	protected ColourBehaviour m_AmbientBehaviour;
	protected ColourBehaviour m_DamageBehaviour;
	protected ColourBehaviour_LerpTo m_StressBehaviour;
	
	// Particle Emitters.
	protected ParticleManager m_ParticleManager;
	protected ParticleEmitter_Burst m_BurstEmitter;

    protected InternalPubSubHub m_InternalPubSub;
    protected Publisher m_InternalDamagedPublisher;

    protected boolean m_CanBeTargeted;

	public Vehicle(GameLogic game)
	{
		super(game.GetPubSubHub(), game.GetGameAudioManager());

        m_InternalPubSub = new InternalPubSubHub();
        m_InternalDamagedPublisher = m_InternalPubSub.CreatePublisher(InternalTopics.DamageTaken);

		m_ParticleManager = game.GetParticleManager();

		///// Motion Attributes.
		m_Position 		= new Vector3();

		///// Attributes.
		m_MaxHullPoints = 100;
		m_HullPoints 	= m_MaxHullPoints;
		m_VehicleInfo  = new VehicleInfo();

		m_AmbientBehaviour = new ColourBehaviour_Pulse(this, ColourBehaviour.ActivationMode.Continuous);	
		AddColourBehaviour(m_AmbientBehaviour);
		
		m_DamageBehaviour = new ColourBehaviour_Flicker(this, ColourBehaviour.ActivationMode.Triggered);
		AddColourBehaviour(m_DamageBehaviour);

		m_StressBehaviour = new ColourBehaviour_LerpTo(this, ColourBehaviour.ActivationMode.Continuous);
        AddColourBehaviour(m_StressBehaviour);

		AddChild(new FloorGrid(m_Colour));
		
		m_LasersOn = false;
		
		m_BurstEmitter = new ParticleEmitter_Burst(game, m_BaseColour, m_AltColour, 40);

		m_DamageDecayCounter = new DecayCounter(1.0, DAMAGE_DECAY_RATE);

        m_CanBeTargeted = true;

        m_PrimaryWeapon = new Weapon_None(this, game);
        m_Utility = new Weapon_None(this, game);
	}
	
	@Override
	public void Update(double deltaTime)
	{
		m_Engine.Update(deltaTime);

        m_BurstEmitter.SetPosition(m_Position);
        m_BurstEmitter.SetVelocity(m_Velocity);

        m_Utility.Update(deltaTime);
		m_PrimaryWeapon.Update(deltaTime);
		
		m_DamageDecayCounter.Update(deltaTime);
		
		m_StressBehaviour.SetIntensity(m_Engine.GetExertion() + m_DamageDecayCounter.GetValue());

		super.Update(deltaTime);
	}
	
	public void SelectWeapon(Weapon newWeapon)
    {
        switch(newWeapon.GetEquipmentType())
        {
            case Weapon:
                m_PrimaryWeapon.CeaseFire();
                m_PrimaryWeapon.WeaponUnequipped();

                m_PrimaryWeapon = newWeapon;

                m_PrimaryWeapon.WeaponEquipped();

                break;

            case Utility:
                m_Utility = newWeapon;
                m_Utility.OpenFire();
                break;
        }
	}

	public void CollisionResponse(double damage)
	{
        DrainEnergy(damage);

        m_DamageBehaviour.TriggerBehaviour();
        UpdateDamageDecayCounter();
        m_InternalDamagedPublisher.Publish();
	}

    public void UpdateDamageDecayCounter()
    {
        m_DamageDecayCounter.AddValue(1.0);
    }
	
	public void DrainEnergy(double drain)
	{
		m_HullPoints -= drain;
		m_HullPoints = (int)MathsHelper.Clamp(m_HullPoints, 0, m_MaxHullPoints);
	}
	
	public void ChargeEnergy(double charge)
	{
		m_HullPoints += charge;
		m_HullPoints = (int)MathsHelper.Clamp(m_HullPoints, 0, m_MaxHullPoints);
	}
	
	public void DodgeLeft()
	{
		m_Engine.Dodge(m_Right);
	}
	
	public void DodgeRight()
	{
		m_Engine.Dodge(m_Left);
	}

    public double GetTurnRate()
    {
        return m_Engine.GetTurnRate();
    }

    public boolean IsDodging()
    {
        return m_Engine.GetDodgeOutput() > 0.0;
    }

	public void SetTurnRate(double turnRate)
	{
		m_Engine.SetTurnRate(turnRate);
	}
	
	public void SetEngineOutput(double output)
	{
		output = MathsHelper.Clamp(output, 0, 1);
		m_Engine.SetEngineOutput(output);
	}
	
	public void StrafeLeft()
	{
		m_Engine.SetDirection(m_Right);
		m_VehicleInfo.SetMovementState(MovementStates.StrafeLeft);
	}
	
	public void StrafeRight()
	{
		m_Engine.SetDirection(m_Left);
		m_VehicleInfo.SetMovementState(MovementStates.StrafeRight);
	}
	
	public void UseRearEngine()
	{
		m_Engine.SetDirection(m_Forward);
        m_VehicleInfo.SetMovementState(MovementStates.Normal);
    }

    public void UseForwardEngine()
	{
		m_Engine.SetDirection(m_Backward);
		m_VehicleInfo.SetMovementState(MovementStates.Reverse);
	}
	
	public void EngageAfterBurners()
	{
		m_Engine.EngageAfterBurners();
		m_VehicleInfo.SetAfterBurnerState(AfterBurnerStates.Engaged);
    }

    public void DisengageAfterBurners()
	{
		m_Engine.DisengageAfterBurners();
		m_VehicleInfo.SetAfterBurnerState(AfterBurnerStates.Disengaged);
	}
	
	protected void SendDeathMessage()
	{
		m_OnDeathPublisher.Publish(m_MaxHullPoints);
	}
	
	@Override
	public boolean IsValid() 
	{
		if(IsForciblyInvalidated())
		{
            m_BurstEmitter.Burst();
            m_PrimaryWeapon.CeaseFire();

			return false;
		}
		
		if(m_HullPoints <= 0)
		{
			SendDeathMessage();
			m_BurstEmitter.Burst();
            m_PrimaryWeapon.CeaseFire();

			return false;
		}

		return true;
	}

	public void SetHullPoints(double hp)
	{
		m_HullPoints = (int) MathsHelper.Clamp(hp, 0, m_MaxHullPoints);
	}
	
	public double GetMass()
	{
		return m_Mass;
	}
	
	public Weapon GetPrimaryWeapon() 
	{
		return m_PrimaryWeapon;
	}
	
	public int GetHullPoints()
	{
		return m_HullPoints;
	}
	
	public int GetMaxHullPoints()
	{
		return m_MaxHullPoints;
	}
	
	public VehicleInfo GetVehicleInfo()
	{
		return m_VehicleInfo;
	}

    public InternalPubSubHub GetInternalPubSubHub()
    {
        return m_InternalPubSub;
    }

    public boolean CanBeTargeted()
    {
        return m_CanBeTargeted;
    }

	public void EnableRoll()
	{
		m_Engine.EnableRoll();
	}

	public void DisableRoll()
	{
		m_Engine.DisableRoll();
	}
}