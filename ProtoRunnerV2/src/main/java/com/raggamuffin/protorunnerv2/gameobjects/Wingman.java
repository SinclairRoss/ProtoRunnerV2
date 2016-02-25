package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ai.AIBehaviours;
import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.ai.FireControlBehaviour;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.weapons.Weapon_PulseLaserWingman;

public class Wingman extends Vehicle
{
	private AIController m_AIController;
	private VehicleManager m_VehicleManager;

    private Subscriber m_PlayerSpawnedSubscriber;
	
	public Wingman(GameLogic game)
	{
		super(game);
		
		m_VehicleManager = game.GetVehicleManager();

		m_Model = ModelType.Runner;

		m_Position.SetVector(0, 0, 0);

        m_BaseColour = game.GetColourManager().GetPrimaryColour();
        m_AltColour = game.GetColourManager().GetAccentingColour();

        m_BurstEmitter.SetInitialColour(m_BaseColour);
        m_BurstEmitter.SetFinalColour(m_AltColour);

        m_Engine = new Engine_Standard(this, game);
		m_Engine.SetMaxTurnRate(2.0);
		m_Engine.SetMaxEngineOutput(3000);
		
		m_MaxHullPoints = 1000;
		m_HullPoints 	= m_MaxHullPoints;

		SetAffiliation(AffiliationKey.BlueTeam);
		
		SelectWeapon(new Weapon_PulseLaserWingman(this, game));

        m_AIController = new AIController(this, m_VehicleManager, game.GetBulletManager(), AIBehaviours.FollowTheLeader, FireControlBehaviour.Standard);
		m_AIController.SetLeader(m_VehicleManager.GetPlayer());

        m_PlayerSpawnedSubscriber = new PlayerSpawnedSubscriber();
		game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerSpawned, m_PlayerSpawnedSubscriber);

		m_OnDeathPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.WingmanDestroyed);
	}

	@Override 
	public void Update(double deltaTime)
	{
		m_AIController.Update(deltaTime);
		
		super.Update(deltaTime);
	}

    @Override
    public void CleanUp()
    {
        m_PubSubHub.UnsubscribeFromTopic(PublishedTopics.PlayerSpawned, m_PlayerSpawnedSubscriber);
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
