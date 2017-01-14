package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ai.AIBehaviours;
import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.ai.FireControlBehaviour;
import com.raggamuffin.protorunnerv2.ai.NavigationalBehaviourInfo;
import com.raggamuffin.protorunnerv2.ai.TargetingBehaviour;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.weapons.Weapon_PunkShot;
import com.raggamuffin.protorunnerv2.weapons.Weapon_RailGun;

public class Vehicle_Tank extends Vehicle
{
	private AIController m_AIController;
	
	public Vehicle_Tank(GameLogic game)
	{
		super(game, ModelType.Byte);

		m_Mass = 2000;

        m_MaxHullPoints = 300;
		m_HullPoints = m_MaxHullPoints;

		m_BoundingRadius = 2.0;

        SetBaseColour(Colours.IndianRed);

		m_Position.SetVector(10, 0, 10);

        m_Engine = new Engine_Standard(this, game);
		m_Engine.SetMaxTurnRate(1.0);
		m_Engine.SetMaxEngineOutput(40000);
        m_Engine.SetDodgeOutput(0);

		SetAffiliation(AffiliationKey.RedTeam); 
		
		SelectWeapon(new Weapon_PunkShot(this, game));

		NavigationalBehaviourInfo navInfo = new NavigationalBehaviourInfo(0.4, 1.0, 0.7, 0.6);
        m_AIController = new AIController(this, game.GetVehicleManager(), game.GetBulletManager(), navInfo, AIBehaviours.EngageTarget, FireControlBehaviour.Telegraphed, TargetingBehaviour.Standard);

		m_OnDeathPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.EnemyDestroyed);
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
		super.CleanUp();
		m_PrimaryWeapon.CleanUp();
    }
} 