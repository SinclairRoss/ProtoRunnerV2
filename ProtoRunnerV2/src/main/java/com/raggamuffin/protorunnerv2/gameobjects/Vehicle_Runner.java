package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.MultiplierHoover;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.weapons.Weapon;
import com.raggamuffin.protorunnerv2.weapons.Weapon_BitLaser;
import com.raggamuffin.protorunnerv2.weapons.Weapon_PulseLaser;

public class Vehicle_Runner extends Vehicle
{
	private ControlScheme m_Input;

	// Weapons.
	private Weapon m_PrimaryWeapon;
	
	private Publisher m_DamageTakenPublisher;

    private ChaseCamera m_Camera;
    private MultiplierHoover m_MultiplierHoover;

    private Subscriber FireSubscriber;
    private Subscriber CeaseFireSubscriber;
    private Subscriber EvadeLeftSubscriber;
    private Subscriber EvadeRightSubscriber;
    private Subscriber StrafeLeftSubscriber;
    private Subscriber StrafeRightSubscriber;
    private Subscriber EngageAfterBurnersSubscriber;
    private Subscriber DisengageAfterBurnersSubscriber;
    private Subscriber ForwardSubscriber;
    private Subscriber ReverseSubscriber;
    private Subscriber EnemyDestroyedSubscriber;

    public Vehicle_Runner(GameLogic game)
	{
		super(game, ModelType.Runner, 1.5);

        m_Camera = game.GetCamera();

		m_Input = game.GetControlScheme();

        SetColour(game.GetColourManager().GetPrimaryColour());

        Shield_Timed shield = new Shield_Timed(this);
        game.GetGameObjectManager().AddObject(shield);

		m_Engine = new Engine_Standard(this, game);
        m_Engine.SetMaxTurnRate(2.0);//2
		m_Engine.SetMaxEngineOutput(GameLogic.TEST_MODE ? 0 : 70);//10000
        m_Engine.SetAfterBurnerOutput(90); // 15000
		
		m_MaxHullPoints = 1;
		m_HullPoints = m_MaxHullPoints;

        game.GetGameObjectManager().AddObject(new Radar(this, game));

        m_Faction = AffiliationKey.BlueTeam;

        m_PrimaryWeapon	= new Weapon_PulseLaser(this, game);
        SelectWeapon(m_PrimaryWeapon);

		m_DamageTakenPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.PlayerHit);
		m_OnDeathPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.PlayerDestroyed);

        FireSubscriber = new FireSubscriber();
        CeaseFireSubscriber = new CeaseFireSubscriber();
        EvadeLeftSubscriber = new EvadeLeftSubscriber();
        EvadeRightSubscriber =  new EvadeRightSubscriber();
        StrafeLeftSubscriber = new StrafeLeftSubscriber();
        StrafeRightSubscriber = new StrafeRightSubscriber();
        EngageAfterBurnersSubscriber = new EngageAfterBurnersSubscriber();
        DisengageAfterBurnersSubscriber = new DisengageAfterBurnersSubscriber();
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
        m_PubSubHub.SubscribeToTopic(PublishedTopics.Forward, ForwardSubscriber);
        m_PubSubHub.SubscribeToTopic(PublishedTopics.Reverse, ReverseSubscriber);
        m_PubSubHub.SubscribeToTopic(PublishedTopics.EnemyDestroyed, EnemyDestroyedSubscriber);

        m_MultiplierHoover = new MultiplierHoover(GetPosition(), game);
    }

	@Override
	public void Update(double deltaTime)
	{
        m_MultiplierHoover.Update();
        m_Engine.SetTurnRate(m_Input.GetTilt());

		super.Update(deltaTime);
	}

    @Override
    public void CleanUp()
    {
        super.CleanUp();

        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.Fire, FireSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.CeaseFire, CeaseFireSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.EvadeLeft, EvadeLeftSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.EvadeRight, EvadeRightSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.StrafeLeft, StrafeLeftSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.StrafeRight, StrafeRightSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.AfterBurnersEngage, EngageAfterBurnersSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.AfterBurnersDisengage, DisengageAfterBurnersSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.Forward, ForwardSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.Reverse, ReverseSubscriber);
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.EnemyDestroyed, EnemyDestroyedSubscriber);

        m_PrimaryWeapon.CleanUp();
    }

    @Override
	public void CollisionResponse(double damage)
	{
		super.CollisionResponse(damage);
		m_DamageTakenPublisher.Publish();
	}

    @Override
    public void SelectWeapon(Weapon newWeapon)
    {
        super.SelectWeapon(newWeapon);
        //m_SwitchWeaponsPublisher.Publish();
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