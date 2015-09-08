package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.ai.AIGoalSet;
import com.raggamuffin.protorunnerv2.ai.AIPersonalityAttributes;
import com.raggamuffin.protorunnerv2.ai.GoalState;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.EngineUseBehaviour_Null;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.Engine_Standard;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.weapons.Projectile;
import com.raggamuffin.protorunnerv2.weapons.RailGun_Tank;

public class Tank extends Vehicle
{
	private AIController m_AIController;
	private Publisher m_EnemyHitPublisher;
	
	private Vehicle m_Player;
	private VehicleManager m_VehicleManager;
	
	public Tank(GameLogic game)
	{
		super(game);
		
		m_VehicleManager = game.GetVehicleManager();
		m_Player = m_VehicleManager.GetPlayer();

		m_Mass = 2000;

        m_MaxHullPoints = 300;
		m_HullPoints = m_MaxHullPoints;

		m_BoundingRadius = 2.0;
		
		m_Model = ModelType.Byte;
        SetBaseColour(Colours.IndianRed);

		m_Position.SetVector(10, 0, 10);

        m_Engine = new Engine_Standard(this, game, new EngineUseBehaviour_Null());
		m_Engine.SetMaxTurnRate(1.0);
		m_Engine.SetMaxEngineOutput(500);

		SetAffiliation(AffiliationKey.RedTeam); 
		
		SelectWeapon(new RailGun_Tank(this, game));

        AIPersonalityAttributes attributes = new AIPersonalityAttributes(0.5, 1.0, 0.2, 1.0, 0.2);
        AIGoalSet goalSet = new AIGoalSet(GoalState.EngageTarget);
        m_AIController = new AIController(this, m_VehicleManager, game.GetBulletManager(), attributes, goalSet);
		
		m_EnemyHitPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.EnemyHit);
		m_OnDeathPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.EnemyDestroyed);
		
		game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerSpawned, new PlayerSpawnedSubscriber());
	}
	
	@Override 
	public void Update(double DeltaTime)
	{
		m_AIController.Update(DeltaTime);
		
		super.Update(DeltaTime);	
	}
	
	@Override
	public void CollisionResponse(GameObject Collider, double deltaTime)
	{
		super.CollisionResponse(Collider, deltaTime);
		
		if(Collider instanceof Projectile)
		{
			Projectile proj = (Projectile) Collider;
			
			if(m_Player != null)
			{
				if(proj.GetFiringWeapon().GetAnchor() == m_Player)
				{
					m_EnemyHitPublisher.Publish();
				}
			}
		}
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