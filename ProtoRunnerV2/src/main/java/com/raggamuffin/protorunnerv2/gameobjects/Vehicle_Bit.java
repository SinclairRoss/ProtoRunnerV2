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
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.Weapon_BitLaser;
import com.raggamuffin.protorunnerv2.weapons.Weapon_None;
import com.raggamuffin.protorunnerv2.weapons.Weapon_PulseLaser;

public class Vehicle_Bit extends Vehicle
{
	private AIController m_AIController;
	private Timer m_Timer;

	public Vehicle_Bit(GameLogic game, Vector3 position)
	{
		super(game, ModelType.Byte, position, 2, 4, VehicleClass.StandardVehicle, true, PublishedTopics.EnemyDestroyed, AffiliationKey.RedTeam);

        SetScale(0.5);

		SetColour(Colours.Pink70);

        m_Engine = new Engine_Standard(this, game);
        m_Engine.SetMaxTurnRate(GameLogic.TEST_MODE ? 0 : 2);
        m_Engine.SetMaxEngineOutput(GameLogic.TEST_MODE ? 0 : 90); // 90
        m_Engine.SetAfterBurnerOutput(90);

        //SelectWeapon(new Weapon_BitLaser(this, game));
		SelectWeapon(new Weapon_None(this, game));

		m_Timer = new Timer(5.0);
		m_Timer.Start();

		NavigationalBehaviourInfo navInfo = new NavigationalBehaviourInfo(0.4, 1.0, 0.7, 0.6);
		m_AIController = new AIController(this, game.GetVehicleManager(), game.GetBulletManager(), navInfo, AIBehaviours.EngageTarget, FireControlBehaviour.Telegraphed, TargetingBehaviour.Standard);
	}
	
	@Override 
	public void Update(double deltaTime)
	{
		m_AIController.Update(deltaTime);

		if(m_Timer.HasElapsed())
		{
		//	DrainEnergy(4543);
		}

		super.Update(deltaTime);
	}

	@Override
	public void CleanUp()
	{
		super.CleanUp();
		m_AIController.CleanUp();
	}
} 