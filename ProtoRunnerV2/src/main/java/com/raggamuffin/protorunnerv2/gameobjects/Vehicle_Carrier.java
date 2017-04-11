package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ai.AIBehaviours;
import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.ai.FireControlBehaviour;
import com.raggamuffin.protorunnerv2.ai.NavigationalBehaviourInfo;
import com.raggamuffin.protorunnerv2.ai.TargetingBehaviour;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.weapons.Weapon_None;


public class Vehicle_Carrier extends Vehicle
{
	private AIController m_AIController;
	private VehicleManager m_VehicleManager;

    private int NUM_DRONES = 5;

	public Vehicle_Carrier(GameLogic game)
	{
		super(game, ModelType.Carrier, 5);

		SetColour(Colours.BlockPurple);

        m_MaxHullPoints = 12;
        m_HullPoints = m_MaxHullPoints;

        m_Engine = new Engine_Cycling(this, game);
		m_Engine.SetMaxTurnRate(1.0);
		m_Engine.SetMaxEngineOutput(70); // 10000
        m_Engine.SetDodgeOutput(0);

		m_Faction = AffiliationKey.RedTeam;

        SelectWeapon(new Weapon_None(this, game));

        VehicleManager vehicleManager = game.GetVehicleManager();
        NavigationalBehaviourInfo navInfo = new NavigationalBehaviourInfo(0.4, 1.0, 0.7, 0.6);
		m_AIController = new AIController(this, vehicleManager, game.GetBulletManager(), navInfo, AIBehaviours.Encircle, FireControlBehaviour.MissileLauncher, TargetingBehaviour.Standard);

		m_OnDeathPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.EnemyDestroyed);

		for(int i = 0; i < NUM_DRONES; ++i)
        {
            Vehicle drone = vehicleManager.SpawnDrone(this);
            drone.SetPosition(GetPosition());
        }
    }
	
	@Override 
	public void Update(double deltaTime)
	{
		m_AIController.Update(deltaTime);

        super.Update(deltaTime);
	}
} 