package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.weapons.Weapon_DeployFlares;
import com.raggamuffin.protorunnerv2.weapons.Weapon_LaserVampire;
import com.raggamuffin.protorunnerv2.weapons.Weapon_MultiLaser;
import com.raggamuffin.protorunnerv2.weapons.Weapon_PulseLaser;
import com.raggamuffin.protorunnerv2.weapons.Weapon_MissileLauncher;
import com.raggamuffin.protorunnerv2.weapons.Weapon;
import com.raggamuffin.protorunnerv2.weapons.WeaponSlot;

public class Vehicle_Runner extends Vehicle
{
	private WeaponSlot m_CurrentlyUsedSlot;
	private ControlScheme m_Input;

	// Weapons.
	private Weapon m_WeaponLeft;
	private Weapon m_WeaponRight;
	private Weapon m_WeaponUp;
	private Weapon m_WeaponDown;
	
	private Publisher m_DamageTakenPublisher;
	private Publisher m_SwitchWeaponsPublisher;

    private ChaseCamera m_Camera;
    private final double m_HealthDrainRate;

    private Subscriber FireSubscriber;
    private Subscriber CeaseFireSubscriber;
    private Subscriber EvadeLeftSubscriber;
    private Subscriber EvadeRightSubscriber;
    private Subscriber StrafeLeftSubscriber;
    private Subscriber StrafeRightSubscriber;
    private Subscriber EngageAfterBurnersSubscriber;
    private Subscriber DisengageAfterBurnersSubscriber;
    private Subscriber WeaponLeftSubscriber;
    private Subscriber WeaponRightSubscriber;
    private Subscriber WeaponUpSubscriber;
    private Subscriber WeaponDownSubscriber;
    private Subscriber ForwardSubscriber;
    private Subscriber ReverseSubscriber;
    private Subscriber EnemyDestroyedSubscriber;

    public Vehicle_Runner(GameLogic game)
	{
		super(game);

        m_Camera = game.GetCamera();

		m_Input = game.GetControlScheme();
		m_Model = ModelType.Runner;

        m_BaseColour = game.GetColourManager().GetPrimaryColour();
        m_AltColour = game.GetColourManager().GetAccentingColour();

        m_BurstEmitter.SetInitialColour(m_BaseColour);
        m_BurstEmitter.SetFinalColour(m_AltColour);

        m_Position.SetVector(0, 0, 0);
		
		m_Engine = new Engine_Standard(this, game);
		m_Engine.SetMaxTurnRate(2.0);//2
		m_Engine.SetMaxEngineOutput(3000);//3000
        m_Engine.SetAfterBurnerOutput(0);
		
		m_MaxHullPoints = 1000;
		m_HullPoints 	= m_MaxHullPoints;

        double completeHealthDrainTime = 20.0; //20
        m_HealthDrainRate = m_MaxHullPoints / completeHealthDrainTime;

		AddChild(new Radar(this, game));

		SetAffiliation(AffiliationKey.BlueTeam);

		m_WeaponLeft 	= new Weapon_PulseLaser(this, game);
		m_WeaponRight 	= new Weapon_LaserVampire(this, game);
		m_WeaponUp 		= new Weapon_MissileLauncher(this, game);
		m_WeaponDown 	= new Weapon_DeployFlares(this, game);
		
		m_LasersOn = true;
			
		m_DamageTakenPublisher     	= m_PubSubHub.CreatePublisher(PublishedTopics.PlayerHit);
		m_OnDeathPublisher 			= m_PubSubHub.CreatePublisher(PublishedTopics.PlayerDestroyed);
		m_SwitchWeaponsPublisher	= m_PubSubHub.CreatePublisher(PublishedTopics.PlayerSwitchedWeapon);

        FireSubscriber = new FireSubscriber();
        CeaseFireSubscriber = new CeaseFireSubscriber();
        EvadeLeftSubscriber = new EvadeLeftSubscriber();
        EvadeRightSubscriber =  new EvadeRightSubscriber();
        StrafeLeftSubscriber = new StrafeLeftSubscriber();
        StrafeRightSubscriber = new StrafeRightSubscriber();
        EngageAfterBurnersSubscriber = new EngageAfterBurnersSubscriber();
        DisengageAfterBurnersSubscriber = new DisengageAfterBurnersSubscriber();
        WeaponLeftSubscriber = new WeaponLeftSubscriber();
        WeaponRightSubscriber = new WeaponRightSubscriber();
        WeaponUpSubscriber = new WeaponUpSubscriber();
        WeaponDownSubscriber = new WeaponDownSubscriber();
        ForwardSubscriber = new ForwardSubscriber();
        ReverseSubscriber = new ReverseSubscriber();
        EnemyDestroyedSubscriber = new EnemyDestroyedSubscriber();

        m_PubSubHub.SubscribeToTopic(PublishedTopics.Fire, FireSubscriber);
        m_PubSubHub.SubscribeToTopic(PublishedTopics.CeaseFire, CeaseFireSubscriber);
        m_PubSubHub.SubscribeToTopic(PublishedTopics.EvadeLeft, EvadeLeftSubscriber);
        m_PubSubHub.SubscribeToTopic(PublishedTopics.EvadeRight, EvadeRightSubscriber);
        m_PubSubHub.SubscribeToTopic(PublishedTopics.StrafeLeft, StrafeLeftSubscriber);
        m_PubSubHub.SubscribeToTopic(PublishedTopics.StrafeRight, StrafeRightSubscriber);
        m_PubSubHub.SubscribeToTopic(PublishedTopics.AfterBurnersEngage, EngageAfterBurnersSubscriber);
        m_PubSubHub.SubscribeToTopic(PublishedTopics.AfterBurnersDisengage, DisengageAfterBurnersSubscriber);
        m_PubSubHub.SubscribeToTopic(PublishedTopics.WeaponLeft, WeaponLeftSubscriber);
        m_PubSubHub.SubscribeToTopic(PublishedTopics.WeaponRight, WeaponRightSubscriber);
        m_PubSubHub.SubscribeToTopic(PublishedTopics.WeaponUp, WeaponUpSubscriber);
        m_PubSubHub.SubscribeToTopic(PublishedTopics.WeaponDown, WeaponDownSubscriber);
        m_PubSubHub.SubscribeToTopic(PublishedTopics.Forward, ForwardSubscriber);
        m_PubSubHub.SubscribeToTopic(PublishedTopics.Reverse, ReverseSubscriber);
        m_PubSubHub.SubscribeToTopic(PublishedTopics.EnemyDestroyed, EnemyDestroyedSubscriber);

        SelectWeaponBySlot(WeaponSlot.Left);
    }

	@Override
	public void Update(double deltaTime)
	{
     //   DrainEnergy(m_HealthDrainRate * deltaTime);
        m_Engine.SetTurnRate(m_Input.GetTilt());
		super.Update(deltaTime);
	}

    @Override
    public void CleanUp()
    {
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.Fire, FireSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.CeaseFire, CeaseFireSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.EvadeLeft, EvadeLeftSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.EvadeRight, EvadeRightSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.StrafeLeft, StrafeLeftSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.StrafeRight, StrafeRightSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.AfterBurnersEngage, EngageAfterBurnersSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.AfterBurnersDisengage, DisengageAfterBurnersSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.WeaponLeft, WeaponLeftSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.WeaponRight, WeaponRightSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.WeaponUp, WeaponUpSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.WeaponDown, WeaponDownSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.Forward, ForwardSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.Reverse, ReverseSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.EnemyDestroyed, EnemyDestroyedSubscriber);
    }

    @Override
	public void CollisionResponse(double damage)
	{
		super.CollisionResponse(damage);
		m_DamageTakenPublisher.Publish();
	}

	public void SelectWeaponBySlot(WeaponSlot slot)
	{
        m_CurrentlyUsedSlot = slot;

		switch(slot)
		{
			case Down:
				SelectWeapon(m_WeaponDown);
				break;
			case Left:
				SelectWeapon(m_WeaponLeft);
				break;
			case Right:
				SelectWeapon(m_WeaponRight);
				break;
			case Up:
				SelectWeapon(m_WeaponUp);
				break;		
		}
	}

    @Override
    public void SelectWeapon(Weapon newWeapon)
    {
        super.SelectWeapon(newWeapon);
        m_SwitchWeaponsPublisher.Publish();
    }
	
	public WeaponSlot GetWeaponSlot()
	{
		return m_CurrentlyUsedSlot;
	}

    @Override
    public void EngageAfterBurners()
    {
        super.EngageAfterBurners();
        m_Camera.SprintCam();
    }

    @Override
    public void DisengageAfterBurners()
    {
        super.DisengageAfterBurners();
        m_Camera.NormalCam();
    }

    private class FireSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_PrimaryWeapon.OpenFire();
        }
    }

    private class CeaseFireSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_PrimaryWeapon.CeaseFire();
        }
    }

    private class EvadeLeftSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            DodgeLeft();
        }
    }

    private class EvadeRightSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            DodgeRight();
        }
    }

    private class StrafeLeftSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            StrafeLeft();
        }
    }

    private class StrafeRightSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            StrafeRight();
        }
    }

    private class EngageAfterBurnersSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            EngageAfterBurners();
        }
    }

    private class DisengageAfterBurnersSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            DisengageAfterBurners();
        }
    }

    private class WeaponLeftSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            SelectWeaponBySlot(WeaponSlot.Left);
        }
    }

    private class WeaponRightSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            SelectWeaponBySlot(WeaponSlot.Right);
        }
    }

    private class WeaponUpSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            SelectWeaponBySlot(WeaponSlot.Up);
        }
    }

    private class WeaponDownSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            SelectWeaponBySlot(WeaponSlot.Down);
        }
    }

    private class ForwardSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            UseRearEngine();
        }
    }

    private class ReverseSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            UseForwardEngine();
        }
    }

    private class EnemyDestroyedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            ChargeEnergy(200);
        }
    }
}
