package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.weapons.Projectile;
import com.raggamuffin.protorunnerv2.weapons.PulseLaser_Punk;

public class Bit extends Vehicle
{
	private AIController m_AIController;
	private Publisher m_EnemyHitPublisher;
	
	private Vehicle m_Player;
	private VehicleManager m_VehicleManager;
	
	public Bit(GameLogic game)
	{
		super(game);
		
		m_VehicleManager = game.GetVehicleManager();
		m_Player = m_VehicleManager.GetPlayer();
		
		m_Model = ModelType.Bit;

		m_Position.SetVector(10, 0, 10);
		m_Engine.SetMaxTurnRate(1.5);
		m_Engine.SetMaxEngineOutput(1500);
        m_BoundingRadius = 2;

		SetAffiliation(AffiliationKey.RedTeam); 
		
		SelectWeapon(new PulseLaser_Punk(this, game));
		
		m_AIController = new AIController(this, m_VehicleManager, game.GetBulletManager());
		
		m_EnemyHitPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.EnemyHit);
		m_OnDeathPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.EnemyDestroyed);
		
		game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerSpawned, new PlayerSpawnedSubscriber());
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