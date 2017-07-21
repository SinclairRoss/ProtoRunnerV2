package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.MultiplierHoover;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.Weapon_ChargeLaser;
import com.raggamuffin.protorunnerv2.weapons.Weapon_PulseLaser;

public class Vehicle_Runner extends Vehicle
{
    private final double STAMINA_COST_DODGE = -0.25;
    private final double STAMINA_COST_BOOST = -0.25;

	private ControlScheme m_Input;
	
	private Publisher m_DamageTakenPublisher;

    private ChaseCamera m_Camera;
    private MultiplierHoover m_MultiplierHoover;

    private double m_Stamina;
    private Weapon_ChargeLaser m_ChargeLaser;

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

    public Vehicle_Runner(GameLogic game, Vector3 position)
	{
		super(game, ModelType.Runner, position, 1.5, 1, VehicleClass.StandardVehicle, true, PublishedTopics.PlayerDestroyed, AffiliationKey.BlueTeam);

        m_Camera = game.GetCamera();

		m_Input = game.GetControlScheme();

        SetColour(Colours.RunnerBlue);

		m_Engine = new Engine_Standard(this, game);
        m_Engine.SetMaxTurnRate(2.0);
		m_Engine.SetMaxEngineOutput(GameLogic.TEST_MODE ? 0 : 70);//70
        m_Engine.SetAfterBurnerOutput(90);

        m_MultiplierHoover = new MultiplierHoover(this, game);
        m_Stamina = 1.0;

      //  game.GetGameObjectManager().AddObject(new Radar(this, game));

        SelectWeapon(new Weapon_PulseLaser(this, game));
        m_ChargeLaser = new Weapon_ChargeLaser(this, game);

		m_DamageTakenPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.PlayerHit);

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
    }

	@Override
	public void Update(double deltaTime)
    {
        m_MultiplierHoover.Update();
        m_Engine.SetTurnRate(m_Input.GetTilt());

        m_ChargeLaser.Update(deltaTime);

        if(m_Engine.AfterburnersEngaged())
        {
            UpdateStaminaValue(STAMINA_COST_BOOST * deltaTime);
        }

        if(m_Stamina <= 0.0)
        {
           // DisengageAfterBurners();

            //if(GameLogic.TEST_MODE)
           // {
                m_Stamina = 1.0;
           // }
        }

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
    }

    @Override
	public void CollisionResponse(double damage)
	{
		super.CollisionResponse(damage);
		m_DamageTakenPublisher.Publish();
	}

    @Override
    public void EngageAfterBurners()
    {
        if(m_Stamina > 0)
        {
            super.EngageAfterBurners();
            m_Camera.SprintCam();
        }
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
        public void Update(Object args)
        {
            OpenFire();
          //  m_ChargeLaser.PullTrigger();
        }
    }

    private class CeaseFireSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            CeaseFire();
           // m_ChargeLaser.ReleaseTrigger();
        }
    }

    private class EvadeLeftSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            if(m_Stamina > 0)
            {
                DodgeLeft();
                UpdateStaminaValue(STAMINA_COST_DODGE);
            }
        }
    }

    private class EvadeRightSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            if(m_Stamina > 0)
            {
                DodgeRight();
                UpdateStaminaValue(STAMINA_COST_DODGE);
            }
        }
    }

    private class StrafeLeftSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            StrafeLeft();
        }
    }

    private class StrafeRightSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            StrafeRight();
        }
    }

    private class EngageAfterBurnersSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            EngageAfterBurners();
        }
    }

    private class DisengageAfterBurnersSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            DisengageAfterBurners();
        }
    }

    private class ForwardSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            MoveForward();
        }
    }

    private class ReverseSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            //m_EndForEndFlip.StartManeuver();
        }
    }

    public double GetStamina() { return m_Stamina; }
    public void UpdateStaminaValue(double change)
    {
        m_Stamina += change;
        m_Stamina = MathsHelper.Clamp(m_Stamina, 0.0, 1.0);
    }
}