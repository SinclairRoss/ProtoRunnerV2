package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.weapons.BurstLaser;
import com.raggamuffin.protorunnerv2.weapons.PulseLaser;
import com.raggamuffin.protorunnerv2.weapons.RocketLauncher;
import com.raggamuffin.protorunnerv2.weapons.PanicSwitch;
import com.raggamuffin.protorunnerv2.weapons.Weapon;
import com.raggamuffin.protorunnerv2.weapons.WeaponSlot;

public class Runner extends Vehicle
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

	public Runner(GameLogic game)
	{
		super(game);

		m_Input = game.GetControlScheme();

		m_Model = ModelType.Runner;

		m_Position.SetVector(0, 0, 0);
		
		m_Engine = new Engine(this, m_ParticleManager, new EngineUseBehaviour_Drain(this));
		m_Engine.SetMaxTurnRate(2.0);//2
		m_Engine.SetMaxEngineOutput(1500);//1500
		
		m_MaxHullPoints = 1000;
		m_HullPoints 	= m_MaxHullPoints;

		AddChild(new Radar(this, game));
		
		m_PostFireAction = new PostFireAction_Player(this, m_PubSubHub);
		
		SetAffiliation(AffiliationKey.BlueTeam);

		m_WeaponLeft 	= new PulseLaser(this, game);
		m_WeaponRight 	= new BurstLaser(this, game);
		m_WeaponUp 		= new RocketLauncher(this, game);
		m_WeaponDown 	= new PanicSwitch(this, game);
		
		m_LasersOn = true;
			
		m_DamageTakenPublisher     	= m_PubSubHub.CreatePublisher(PublishedTopics.PlayerHit);
		m_OnDeathPublisher 			= m_PubSubHub.CreatePublisher(PublishedTopics.PlayerDestroyed);
		m_SwitchWeaponsPublisher	= m_PubSubHub.CreatePublisher(PublishedTopics.PlayerSwitchedWeapon);

        m_PubSubHub.SubscribeToTopic(PublishedTopics.Fire, new FireSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.CeaseFire, new CeaseFireSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.EvadeLeft, new EvadeLeftSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.EvadeRight, new EvadeRightSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.StrafeLeft, new StrafeLeftSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.StrafeRight, new StrafeRightSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.AfterBurnersEngage, new EngageAfterBurnersSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.AfterBurnersDisengage, new DisengageAfterBurnersSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.WeaponLeft, new WeaponLeftSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.WeaponRight, new WeaponRightSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.WeaponUp, new WeaponUpSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.WeaponDown, new WeaponDownSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.Forward, new ForwardSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.Reverse, new ReverseSubscriber());

        SelectWeaponBySlot(WeaponSlot.Left);
	}

	@Override 
	public void Update(double DeltaTime)
	{
        m_Engine.SetTurnRate(m_Input.GetTilt());
		super.Update(DeltaTime);	
	}
	
	@Override
	public void CollisionResponse(GameObject Collider, double deltaTime)
	{
		super.CollisionResponse(Collider, deltaTime);
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
}
