package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ai.AIBehaviours;
import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.ai.FireControlBehaviour;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.weapons.Weapon_None;

import java.util.ArrayList;

public class Carrier extends Vehicle
{
	private AIController m_AIController;
	private VehicleManager m_VehicleManager;

    private ArrayList<Drone> m_Drones;
    private final int DRONE_CAPACITY = 3;

    private Timer m_SpawnTimer;

	public Carrier(GameLogic game)
	{
		super(game);
		
		m_VehicleManager = game.GetVehicleManager();
		
		m_Model = ModelType.Carrier;
        SetBaseColour(Colours.PastelRed);

        m_MaxHullPoints = 600;
        m_HullPoints = m_MaxHullPoints;

		m_Position.SetVector(10, 0, 10);

        m_Engine = new Engine_Cycling(this, game, new EngineUseBehaviour_Null());
		m_Engine.SetMaxTurnRate(1.5);
		m_Engine.SetMaxEngineOutput(10000); // 10000
        m_BoundingRadius = 3;

		SetAffiliation(AffiliationKey.RedTeam);

        SelectWeapon(new Weapon_None(this, game));

		m_AIController = new AIController(this, m_VehicleManager, game.GetBulletManager(), AIBehaviours.Encircle, FireControlBehaviour.None);

		m_OnDeathPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.EnemyDestroyed);

        m_Drones = new ArrayList<>();
        m_SpawnTimer = new Timer(0.75);
    }
	
	@Override 
	public void Update(double deltaTime)
	{
        m_SpawnTimer.Update(deltaTime);

        if (m_SpawnTimer.TimedOut() && m_Drones.size() < DRONE_CAPACITY)
        {
            m_SpawnTimer.ResetTimer();
            m_Drones.add(m_VehicleManager.SpawnDrone(this, m_Position));
        }

		m_AIController.Update(deltaTime);

		super.Update(deltaTime);
	}
} 