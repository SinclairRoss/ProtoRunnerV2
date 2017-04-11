package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ai.VehicleInfo;
import com.raggamuffin.protorunnerv2.ai.VehicleInfo.AfterBurnerStates;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Burst;
import com.raggamuffin.protorunnerv2.pubsub.InternalPubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.InternalTopics;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.weapons.Weapon;
import com.raggamuffin.protorunnerv2.weapons.Weapon_None;

public abstract class Vehicle extends GameObject
{
	private final double DAMAGE_DECAY_RATE = 2.0;

	///// Motion Attributes
	protected Engine m_Engine;
	protected Publisher m_OnDeathPublisher;

	////// Vehicle Attributes
	protected double m_HullPoints;			// How much damage that this ship can take.
	protected double m_MaxHullPoints;		// The maximum available hullpoints.

	///// Misc Attributes
    protected Weapon m_Utility;
	protected Weapon m_PrimaryWeapon;
	private VehicleInfo m_VehicleInfo;
	protected Timer m_DamageDecayCounter;
	
	// Particle Emitters.
	protected ParticleManager m_ParticleManager;
	protected ParticleEmitter_Burst m_BurstEmitter;

    protected InternalPubSubHub m_InternalPubSub;
    protected Publisher m_InternalDamagedPublisher;

    protected boolean m_CanBeTargeted;

	protected VehicleClass m_VehicleClass;

	protected StatusEffectManager m_StatusEffectManager;

	private FloorGrid m_FloorGrid;

	private GameLogic m_Game;
    protected AffiliationKey m_Faction;
    protected PubSubHub m_PubSubHub;

	public Vehicle(GameLogic game, ModelType modelType, double boundingRadius)
	{
		super(modelType, boundingRadius);

        m_Game = game;

        m_PubSubHub = game.GetPubSubHub();

        m_Faction = AffiliationKey.BlueTeam;

        m_InternalPubSub = new InternalPubSubHub();
        m_InternalDamagedPublisher = m_InternalPubSub.CreatePublisher(InternalTopics.DamageTaken);

		m_ParticleManager = game.GetParticleManager();

		///// Attributes.
		m_MaxHullPoints = 4;
		m_HullPoints 	= m_MaxHullPoints;
		m_VehicleInfo  = new VehicleInfo();

        if(modelType != ModelType.Nothing)
        {
			m_FloorGrid = new FloorGrid(GetPosition(), GetColour(), 10.0);
			game.AddObjectToRenderer(m_FloorGrid);
        }
		
		m_BurstEmitter = new ParticleEmitter_Burst(game, GetColour(), GetColour(), 40);

        m_DamageDecayCounter = new Timer(1.0 / DAMAGE_DECAY_RATE);
        m_DamageDecayCounter.Start();

        m_CanBeTargeted = true;

        m_PrimaryWeapon = new Weapon_None(this, game);
        m_Utility = new Weapon_None(this, game);

		m_VehicleClass = VehicleClass.StandardVehicle;

		m_StatusEffectManager = new StatusEffectManager();
	}

	@Override
	public void Update(double deltaTime)
	{
		m_Engine.Update(deltaTime);

        m_Utility.Update(deltaTime);
		m_PrimaryWeapon.Update(deltaTime);

		super.Update(deltaTime);
	}
	
	public void SelectWeapon(Weapon newWeapon)
    {
        switch(newWeapon.GetEquipmentType())
        {
            case Weapon:
			{
				m_PrimaryWeapon.CeaseFire();
				m_PrimaryWeapon.WeaponUnequipped();

				m_PrimaryWeapon = newWeapon;

				m_PrimaryWeapon.WeaponEquipped();

				break;
			}
            case Utility:
			{
				m_Utility = newWeapon;
				m_Utility.OpenFire();
				break;
			}
        }
	}

	public void CollisionResponse(double damage)
	{
		if(!HasStatusEffect(StatusEffect.Shielded))
		{
			DrainEnergy(damage);
			m_InternalDamagedPublisher.Publish();
		}

        UpdateDamageDecayCounter();
	}

    public void UpdateDamageDecayCounter()
    {
        m_DamageDecayCounter.Start();
    }
	
	public void DrainEnergy(double drain)
	{
		m_HullPoints -= drain;
		m_HullPoints = m_HullPoints < 0 ? 0 : m_HullPoints;
	}
	
	public void ChargeEnergy(double charge)
	{
		m_HullPoints += charge;
		m_HullPoints = m_HullPoints > m_MaxHullPoints ? m_MaxHullPoints : m_HullPoints;
	}
	
	public void DodgeLeft()
	{
		m_Engine.DodgeLeft();
	}
	
	public void DodgeRight()
	{
		m_Engine.DodgeRight();
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
		//m_Engine.SetDirection(m_Left);
		//m_VehicleInfo.SetMovementState(MovementStates.StrafeLeft);
	}
	
	public void StrafeRight()
	{
		//m_Engine.SetDirection(m_Right);
		//m_VehicleInfo.SetMovementState(MovementStates.StrafeRight);
	}
	
	public void UseRearEngine()
	{
		//m_Engine.SetDirection(m_Forward);
        //m_VehicleInfo.SetMovementState(MovementStates.Normal);
    }

    public void UseForwardEngine()
	{
		//m_Engine.SetDirection(m_Backward);
		//m_VehicleInfo.SetMovementState(MovementStates.Reverse);
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
        if(m_OnDeathPublisher != null)
        {
            m_OnDeathPublisher.Publish(GetModel().ordinal());
        }
	}
	
	@Override
	public boolean IsValid() 
	{
        return m_HullPoints > 0;
	}

	public void SetHullPoints(double hp)
	{
		m_HullPoints = (int) MathsHelper.Clamp(hp, 0, m_MaxHullPoints);
	}

	public Weapon GetPrimaryWeapon() 
	{
		return m_PrimaryWeapon;
	}

	public Weapon GetUtility()
	{
		return m_Utility;
	}
	
	public double GetHullPoints()
	{
		return m_HullPoints;
	}
	
	public double GetMaxHullPoints()
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

	public VehicleClass GetVehicleClass() { return m_VehicleClass; }
	public void SetVehicleClass(VehicleClass vClass) { m_VehicleClass = vClass; }

    public void ApplyStatusEffect(StatusEffect effect)
    {
        m_StatusEffectManager.ApplyStatusEffect(effect);
    }

    public void RemoveStatusEffect(StatusEffect effect)
    {
        m_StatusEffectManager.RemoveStatusEffect(effect);
    }

    public boolean HasStatusEffect(StatusEffect effect)
    {
        return m_StatusEffectManager.HasStatusEffect(effect);
    }

	@Override
	public double GetInnerColourIntensity() { return m_DamageDecayCounter.GetInverseProgress(); }

	@Override
	public void CleanUp()
	{
		SendDeathMessage();

        m_Game.GetPopperController().Pop(GetPosition(), GetVelocity());

		m_BurstEmitter.SetPosition(GetPosition());
		m_BurstEmitter.SetVelocity(GetVelocity());
		m_BurstEmitter.Burst();

		m_PrimaryWeapon.CeaseFire();
		m_PrimaryWeapon.CleanUp();

		if(m_FloorGrid != null)
		{
			m_Game.RemoveObjectFromRenderer(m_FloorGrid);
		}
	}

    public AffiliationKey GetAffiliation()
    {
        return m_Faction;
    }

    public void SetAffiliation(AffiliationKey key)
    {
        m_Faction = key;
    }
}