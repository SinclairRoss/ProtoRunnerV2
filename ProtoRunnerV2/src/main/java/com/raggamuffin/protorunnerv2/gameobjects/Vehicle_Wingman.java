package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ai.AIBehaviours;
import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.ai.FireControlBehaviour;
import com.raggamuffin.protorunnerv2.ai.NavigationalBehaviourInfo;
import com.raggamuffin.protorunnerv2.ai.TargetingBehaviour;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.MultiplierHoover;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.weapons.Weapon_PulseLaser;

public class Vehicle_Wingman extends Vehicle
{
	private AIController m_AIController;
	private VehicleManager m_VehicleManager;

    private MultiplierHoover m_MultiplierHoover;

    private Subscriber m_PlayerSpawnedSubscriber;
	
	public Vehicle_Wingman(GameLogic game)
	{
		super(game, ModelType.Runner);

		m_BoundingRadius = 1.5;
		
		m_VehicleManager = game.GetVehicleManager();

		m_Position.SetVector(0, 0, 0);

        m_BaseColour = game.GetColourManager().GetPrimaryColour();
        m_AltColour = game.GetColourManager().GetSecondaryColour();

        m_BurstEmitter.SetInitialColour(m_BaseColour);
        m_BurstEmitter.SetFinalColour(m_AltColour);

		m_Mass = 100;
        m_Engine = new Engine_Standard(this, game);
		m_Engine.SetMaxTurnRate(2.0);
		m_Engine.SetMaxEngineOutput(10000);
        m_Engine.SetAfterBurnerOutput(5000);
		
		m_MaxHullPoints = 1000;
		m_HullPoints 	= m_MaxHullPoints;

		SetAffiliation(AffiliationKey.BlueTeam);
		
		SelectWeapon(new Weapon_PulseLaser(this, game));

		NavigationalBehaviourInfo navInfo = new NavigationalBehaviourInfo(0.4, 1.0, 0.7, 0.6);
        m_AIController = new AIController(this, m_VehicleManager, game.GetBulletManager(), navInfo, AIBehaviours.FollowTheLeader, FireControlBehaviour.Standard, TargetingBehaviour.Standard);
		m_AIController.SetLeader(m_VehicleManager.GetPlayer());

        m_PlayerSpawnedSubscriber = new PlayerSpawnedSubscriber();
		game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerSpawned, m_PlayerSpawnedSubscriber);

		m_OnDeathPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.WingmanDestroyed);

		Shield_Timed shield = new Shield_Timed(game, this);
		AddObjectToGameObjectManager(shield);

		m_MultiplierHoover = new MultiplierHoover(m_Position, game);
	}

	@Override 
	public void Update(double deltaTime)
	{
		m_AIController.Update(deltaTime);
        m_MultiplierHoover.Update();
		
		super.Update(deltaTime);
	}

    @Override
    public void CleanUp()
    {
		super.CleanUp();
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.PlayerSpawned, m_PlayerSpawnedSubscriber);
        m_PrimaryWeapon.CleanUp();
    }

    private class PlayerSpawnedSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			m_AIController.SetLeader(m_VehicleManager.GetPlayer());
		}	
	}
}
