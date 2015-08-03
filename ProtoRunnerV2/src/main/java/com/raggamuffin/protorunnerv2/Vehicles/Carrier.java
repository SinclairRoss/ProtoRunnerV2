package com.raggamuffin.protorunnerv2.Vehicles;

import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.ai.AIGoalSet;
import com.raggamuffin.protorunnerv2.ai.AIPersonalityAttributes;
import com.raggamuffin.protorunnerv2.ai.GoalState;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.CyclingEngine;
import com.raggamuffin.protorunnerv2.gameobjects.EngineUseBehaviour_Null;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.weapons.Projectile;
import com.raggamuffin.protorunnerv2.weapons.Weapon_None;

public class Carrier extends Vehicle
{
	private AIController m_AIController;
	private Publisher m_EnemyHitPublisher;

	private Vehicle m_Player;
	private VehicleManager m_VehicleManager;

    private final int DRONE_CAPACITY = 3;

	public Carrier(GameLogic game)
	{
		super(game);
		
		m_VehicleManager = game.GetVehicleManager();
		m_Player = m_VehicleManager.GetPlayer();
		
		m_Model = ModelType.Carrier;

		m_Position.SetVector(10, 0, 10);

        m_Engine = new CyclingEngine(this, game.GetParticleManager(), new EngineUseBehaviour_Null());
		m_Engine.SetMaxTurnRate(1.5);
		m_Engine.SetMaxEngineOutput(1500);
        m_BoundingRadius = 2;

		SetAffiliation(AffiliationKey.RedTeam);

        SelectWeapon(new Weapon_None(this, game));

        AIPersonalityAttributes attributes = new AIPersonalityAttributes(0.7, 1.0, 0.2, 1.0, 0.7);
        AIGoalSet goalSet = new AIGoalSet(GoalState.EngageTarget);
		m_AIController = new AIController(this, m_VehicleManager, game.GetBulletManager(), attributes, goalSet);
		
		m_EnemyHitPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.EnemyHit);
		m_OnDeathPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.EnemyDestroyed);
		
		game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerSpawned, new PlayerSpawnedSubscriber());

      //  for(int i = 0; i < DRONE_CAPACITY; i++)
      //      game.GetVehicleManager().SpawnVehicle(VehicleType.Drone);
	}
	
	@Override
	public void CollisionResponse(GameObject Collider, double deltaTime)
	{
		super.CollisionResponse(Collider, deltaTime);

        // For calculating accuracy.
		if(Collider instanceof Projectile)
		{
			Projectile proj = (Projectile) Collider;

			if(m_Player == null)
			    return;

            if(proj.GetFiringWeapon().GetAnchor() != m_Player)
                return;

            m_EnemyHitPublisher.Publish();
		}
	}
	
	@Override 
	public void Update(double DeltaTime)
	{
		m_AIController.Update(DeltaTime);
		
		super.Update(DeltaTime);	
	}
	
	private class PlayerSpawnedSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			m_Player = m_VehicleManager.GetPlayer();
		}	
	}
} 