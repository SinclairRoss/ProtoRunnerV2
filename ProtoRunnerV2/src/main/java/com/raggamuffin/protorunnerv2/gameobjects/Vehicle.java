package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ObjectEffect.ObjectEffect;
import com.raggamuffin.protorunnerv2.ObjectEffect.ObjectEffectType;
import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.AudioEmitter_Point;
import com.raggamuffin.protorunnerv2.audio.EAudioRepeatBehaviour;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Burst;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.Weapon;
import com.raggamuffin.protorunnerv2.weapons.Weapon_None;

public abstract class Vehicle extends GameObject
{
	private final double DAMAGE_DECAY_RATE = 2.0;
    private final double DAMAGE_INDICATOR_RATE = 10.0;

    ///// Control
    private GameLogic m_Game;
    protected PubSubHub m_PubSubHub;

	////// Vehicle Attributes
	private double m_HullPoints;
	private final double m_MaxHullPoints;
    private boolean m_CanBeTargeted;
    private VehicleClass m_VehicleClass;
    private StatusEffectRecordKeeper m_StatusEffectRecords;
    private AffiliationKey m_Affiliation;
	private Vector3 m_SteeringVector;
    private SteeringState m_SteeringState;
    protected Engine m_Engine;

	private ObjectEffect m_HealthBar;

	///// Misc Attributes
	private Weapon m_PrimaryWeapon;
	private Timer m_DamageDecayCounter;
	private double m_DamagePulser;
    private Publisher m_OnDeathPublisher;

	///// Aesthetics
	private ParticleEmitter_Burst m_BurstEmitter;
	private FloorGrid m_FloorGrid;

    ///// Audio
	private AudioEmitter_Point m_AudioDodge;

	public Vehicle(GameLogic game, ModelType modelType, Vector3 position, double boundingRadius, int hullPoints, VehicleClass vClass, boolean canBeTargeted, PublishedTopics onDeathTopic, AffiliationKey affiliation)
	{
		super(modelType, boundingRadius);

        SetPosition(position);

        ///// Control
        m_Game = game;
        m_PubSubHub = game.GetPubSubHub();

        ////// Vehicle Attributes
		m_MaxHullPoints = hullPoints;
        m_HullPoints = hullPoints;
        m_CanBeTargeted = canBeTargeted;
        m_VehicleClass = vClass;
        m_StatusEffectRecords = new StatusEffectRecordKeeper(game.GetObjectEffectController(), this);
        m_Affiliation = affiliation;
		m_SteeringVector = new Vector3(Vector3.FORWARD);
        m_SteeringState = SteeringState.Forward;
        m_Engine = null;

        ///// Misc Attributes
        m_PrimaryWeapon = new Weapon_None(this, game);
        m_DamageDecayCounter = new Timer(1.0 / DAMAGE_DECAY_RATE);
        m_DamageDecayCounter.Start();

        if(onDeathTopic != null)
        {
            m_OnDeathPublisher = m_PubSubHub.CreatePublisher(onDeathTopic);
        }

        ///// Aesthetics
        m_BurstEmitter = new ParticleEmitter_Burst(game, GetColour(), GetColour());

		m_FloorGrid = new FloorGrid(GetPosition(), GetColour(), 5.0);
		game.GetGameObjectManager().AddFloorGrid(m_FloorGrid);

        ///// Audio
        m_AudioDodge = new AudioEmitter_Point(this, game.GetGameAudioManager(), AudioClips.Dodge, EAudioRepeatBehaviour.Single);
	}

	@Override
	public void Update(double deltaTime)
	{
		m_Engine.Update(deltaTime);
		m_PrimaryWeapon.Update(deltaTime);

		m_DamagePulser += (deltaTime * (1.0 - GetHullPointsAsPercentage())) * DAMAGE_INDICATOR_RATE;
        m_DamagePulser %= Math.PI;

		super.Update(deltaTime);
	}

	public void CollisionResponse(double damage)
	{
		if(!HasStatusEffect(StatusEffect.Shielded))
		{
			DrainEnergy(damage);
            m_Game.GetObjectEffectController().CreateEffect(ObjectEffectType.DamageMarker, this);
			m_DamageDecayCounter.Start();
		}
	}
	
	public void DrainEnergy(double drain)
	{
		if(m_HealthBar == null)
		{
			m_HealthBar = m_Game.GetObjectEffectController().CreateHealthBar(this);
		}

		m_HullPoints -= drain;

		if(m_HullPoints < 0)
		{
			m_HullPoints = 0;
		}
	}
	
	public void DodgeLeft()
	{
		m_Engine.DodgeLeft();
		m_AudioDodge.Start();
	}
	
	public void DodgeRight()
	{
		m_Engine.DodgeRight();
		m_AudioDodge.Start();
	}

	public double GetHullPointsAsPercentage()
	{
		double hp = m_HullPoints / m_MaxHullPoints;
		return hp;
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

	protected void SelectWeapon(Weapon weapon) { m_PrimaryWeapon = weapon; }

	@Override
	public void CleanUp()
	{
		m_HealthBar = null;

        m_HullPoints = 0;

        if(m_OnDeathPublisher != null)
        {
            m_OnDeathPublisher.Publish(GetModel().ordinal());
        }

        //m_Game.GetPopperController().Pop(GetPosition(), GetVelocity());

        if(GetModel() != ModelType.Nothing)
        {
            m_BurstEmitter.SetPosition(GetPosition());
            m_BurstEmitter.SetVelocity(GetVelocity());
            m_BurstEmitter.Burst();
        }

		m_PrimaryWeapon.ReleaseTrigger();
		m_PrimaryWeapon.CleanUp();

		m_Engine.CleanUp();

		m_FloorGrid.NotifyOfAnchorInvalidation();
	}

    @Override
    public double GetInnerColourIntensity()
	{
		double damageReaction = m_DamageDecayCounter.GetInverseProgress();
		return damageReaction;
    }

    @Override
    public boolean IsValid() { return m_HullPoints > 0; }

    public void StrafeLeft() { m_SteeringState = SteeringState.Left; }
    public void StrafeRight() { m_SteeringState = SteeringState.Right; }
    public void MoveForward() { m_SteeringState = SteeringState.Forward; }
    public void Reverse() { m_SteeringState = SteeringState.Reverse; }
    public void LockSteering() { m_SteeringState = SteeringState.Locked; }
    public SteeringState GetSteeringState() { return m_SteeringState; }

    public void EngageAfterBurners() { m_Engine.EngageAfterBurners(); }
    public void DisengageAfterBurners() { m_Engine.DisengageAfterBurners(); }

    public void ApplyStatusEffect(StatusEffect status) { m_StatusEffectRecords.ApplyStatusEffect(status); }
    public void RemoveStatusEffect(StatusEffect effect) { m_StatusEffectRecords.RemoveStatusEffect(effect); }
    public boolean HasStatusEffect(StatusEffect effect) { return m_StatusEffectRecords.HasStatusEffect(effect); }

    public boolean CanBeTargeted() { return m_CanBeTargeted; }
    public AffiliationKey GetAffiliation() { return m_Affiliation; }
    public VehicleClass GetVehicleClass() { return m_VehicleClass; }

    public Weapon GetPrimaryWeapon() { return m_PrimaryWeapon; }
    protected void OpenFire() { m_PrimaryWeapon.PullTrigger(); }
    protected void CeaseFire() { m_PrimaryWeapon.ReleaseTrigger(); }
}