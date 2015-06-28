package com.raggamuffin.protorunnerv2.gameobjects;

import android.content.Context;

import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.weapons.PulseLaser;

public class Wingman extends Vehicle
{
	private AIController m_AIController;
	private VehicleManager m_VehicleManager;
	
	public Wingman(BulletManager bManager, GameAudioManager audio, ParticleManager pManager, VehicleManager vManager, PubSubHub pubSub, Context context)
	{
		super( pManager, pubSub, audio);
		
		m_VehicleManager = vManager;

		m_Model = ModelType.Runner;

		m_Position.SetVector(0, 0, 0);
		
		m_Engine.SetMaxTurnRate(2.0);
		m_Engine.SetMaxEngineOutput(1500);
		
		m_MaxHullPoints = 1000;
		m_HullPoints 	= m_MaxHullPoints;

		SetAffiliation(AffiliationKey.BlueTeam);
		
		SelectWeapon(new PulseLaser(this, vManager, bManager, pManager, audio, pubSub, context));
		
		m_AIController = new AIController(this, m_VehicleManager, bManager);		
		
		pubSub.SubscribeToTopic(PublishedTopics.PlayerSpawned, new PlayerSpawnedSubscriber());
		pubSub.SubscribeToTopic(PublishedTopics.PlayerDestroyed, new PlayerDestroyedSubscriber());
		
		m_OnDeathPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.WingmanDestroyed);
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
			m_AIController.SetLeader(m_VehicleManager.GetPlayer());
		}	
	}
	
	private class PlayerDestroyedSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			m_AIController.RemoveLeader();
		}	
	}
}
