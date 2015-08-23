package com.raggamuffin.protorunnerv2.vehicles;

import com.raggamuffin.protorunnerv2.ai.VehicleInfo;
import com.raggamuffin.protorunnerv2.ai.VehicleInfo.AfterBurnerStates;
import com.raggamuffin.protorunnerv2.ai.VehicleInfo.MovementStates;
import com.raggamuffin.protorunnerv2.colours.ColourBehaviour_LerpTo;
import com.raggamuffin.protorunnerv2.colours.ColourBehaviour;
import com.raggamuffin.protorunnerv2.colours.ColourBehaviour_Flicker;
import com.raggamuffin.protorunnerv2.colours.ColourBehaviour_Pulse;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Engine;
import com.raggamuffin.protorunnerv2.gameobjects.FloorGrid;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.PostFireAction;
import com.raggamuffin.protorunnerv2.gameobjects.PostFireAction_Null;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.particles.BurstEmitter;
import com.raggamuffin.protorunnerv2.pubsub.InternalPubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.InternalTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.utils.DecayCounter;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.Explosion;
import com.raggamuffin.protorunnerv2.weapons.Projectile;
import com.raggamuffin.protorunnerv2.weapons.Weapon;

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
	protected Weapon m_PrimaryWeapon;
	protected Boolean m_LasersOn;
	private VehicleInfo m_VehicleInfo;
	protected PostFireAction m_PostFireAction;
	private DecayCounter m_DamageDecayCounter;
	
	///// Colour Attributes
	protected ColourBehaviour m_AmbientBehaviour;
	protected ColourBehaviour m_DamageBehaviour;
	protected ColourBehaviour_LerpTo m_StressBehaviour;
	
	// Particle Emitters.
	protected ParticleManager m_ParticleManager;
	protected BurstEmitter m_BurstEmitter;

    protected InternalPubSubHub m_InternalPubSub;
    protected Publisher m_InternalDamagedPublisher;

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
		m_StressBehaviour.SetAltColourByReference(m_AltColour);
        AddColourBehaviour(m_StressBehaviour);

		AddChild(new FloorGrid(m_Colour));
		
		m_LasersOn = false;
		
		m_BurstEmitter = new BurstEmitter(this, m_ParticleManager);
		AddChild(m_BurstEmitter);
		
		m_PostFireAction = new PostFireAction_Null(this);

		m_DamageDecayCounter = new DecayCounter(1.0, DAMAGE_DECAY_RATE);
	}
	
	@Override
	public void Update(double deltaTime)
	{
		m_Engine.Update(deltaTime);

		m_PrimaryWeapon.Update(deltaTime);
		
		m_DamageDecayCounter.Update(deltaTime);
		
		m_StressBehaviour.SetIntensity(m_Engine.GetExertion() + m_DamageDecayCounter.GetValue());

		super.Update(deltaTime);
	}
	
	public void SelectWeapon(Weapon newWeapon)
	{
		if(m_PrimaryWeapon != null)
			m_PrimaryWeapon.LasersOff();
		
		m_PrimaryWeapon = newWeapon;

		if(m_LasersOn)
			m_PrimaryWeapon.LasersOn();
	}
	
	@Override
	public void CollisionResponse(GameObject Collider, double deltaTime)
	{
        m_DamageBehaviour.TriggerBehaviour();
		
		if(Collider instanceof Projectile)
		{
			m_HullPoints -= ((Projectile) Collider).GetBaseDamage();
		}
		
		if(Collider instanceof Explosion)
		{
			m_HullPoints -= ((Explosion) Collider).GetDamageOutput(deltaTime);
		}
		
		m_DamageDecayCounter.AddValue(1.0);
        m_InternalDamagedPublisher.Publish();
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
		m_Engine.Dodge(m_Left);
	}
	
	public void DodgeRight()
	{
		m_Engine.Dodge(m_Right);
	}

    public double GetTurnRate()
    {
        return m_Engine.GetTurnRate();
    }

    public boolean IsDodging()
    {
        return m_Engine.GetDodgeOutput() > 0.0;
    }

	public void SetTurnRate(double TurnRate)
	{
		m_Engine.SetTurnRate(TurnRate);
	}
	
	public void SetEngineOutput(double Output)
	{
		m_Engine.SetEngineOutput(Output);
	}
	
	public void StrafeLeft()
	{
		m_Engine.SetDirection(m_Left);
		m_VehicleInfo.SetMovementState(MovementStates.StrafeLeft);
	}
	
	public void StrafeRight()
	{
		m_Engine.SetDirection(m_Right);
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

			return false;
		}
		
		if(m_HullPoints <= 0)
		{
			SendDeathMessage();
			m_BurstEmitter.Burst();
			
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
	
	public PostFireAction GetPostFireAction()
	{
		return m_PostFireAction;
	}

    public InternalPubSubHub GetInternalPubSubHub()
    {
        return m_InternalPubSub;
    }
}




























