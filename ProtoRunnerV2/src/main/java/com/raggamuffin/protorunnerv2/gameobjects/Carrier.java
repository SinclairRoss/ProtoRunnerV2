package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.ai.AIGoalSet;
import com.raggamuffin.protorunnerv2.ai.AIPersonalityAttributes;
import com.raggamuffin.protorunnerv2.ai.GoalState;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Engine_Cycling;
import com.raggamuffin.protorunnerv2.gameobjects.EngineUseBehaviour_Null;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.weapons.Projectile;
import com.raggamuffin.protorunnerv2.weapons.Weapon_None;

public class Carrier extends Vehicle
{
	private AIController m_AIController;
	private Publisher m_EnemyHitPublisher;

	private Vehicle m_Player;
	private VehicleManager m_VehicleManager;

    private final int DRONE_CAPACITY = 3;
    private int m_NumDrones;

    private Timer m_SpawnTimer;

	public Carrier(GameLogic game)
	{
		super(game);
		
		m_VehicleManager = game.GetVehicleManager();
		m_Player = m_VehicleManager.GetPlayer();
		
		m_Model = ModelType.Carrier;
        SetBaseColour(Colours.PastelRed);

        m_MaxHullPoints = 600;
        m_HullPoints = m_MaxHullPoints;

		m_Position.SetVector(10, 0, 10);

        m_Engine = new Engine_Cycling(this, game, new EngineUseBehaviour_Null());
		m_Engine.SetMaxTurnRate(1.5);
		m_Engine.SetMaxEngineOutput(10000);
        m_BoundingRadius = 2;

		SetAffiliation(AffiliationKey.RedTeam);

        SelectWeapon(new Weapon_None(this, game));

        AIPersonalityAttributes attributes = new AIPersonalityAttributes(0.7, 1.0, 0.2, 1.0, 0.7);
        AIGoalSet goalSet = new AIGoalSet(GoalState.Encircle);
		m_AIController = new AIController(this, m_VehicleManager, game.GetBulletManager(), attributes, goalSet);
		
		m_EnemyHitPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.EnemyHit);
		m_OnDeathPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.EnemyDestroyed);
		
		game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerSpawned, new PlayerSpawnedSubscriber());

        m_SpawnTimer = new Timer(0.75);
        m_NumDrones = 0;
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
	public void Update(double deltaTime)
	{
        m_SpawnTimer.Update(deltaTime);

        if (m_SpawnTimer.TimedOut() && m_NumDrones < DRONE_CAPACITY)
        {
            m_SpawnTimer.ResetTimer();
            m_NumDrones ++;
            m_VehicleManager.SpawnDrone(this, m_Position);
        }

		m_AIController.Update(deltaTime);

		super.Update(deltaTime);
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