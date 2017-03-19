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
import com.raggamuffin.protorunnerv2.weapons.Weapon_BitLaser;
import com.raggamuffin.protorunnerv2.weapons.Weapon_PunkShot;

public class Vehicle_Bit extends Vehicle
{
	private AIController m_AIController;

	public Vehicle_Bit(GameLogic game)
	{
		super(game, ModelType.Bit, 2);

       // SetColourScheme(Colours.Pink70, Colours.HotPink);

		SetColour(Colours.Pink70);

        m_Engine = new Engine_Standard(this, game);
		m_Engine.SetMaxTurnRate(1.5); //1.5
		m_Engine.SetMaxEngineOutput(50); //10000
		m_Engine.SetDodgeOutput(700);

		SetAffiliation(AffiliationKey.RedTeam); 
		
		SelectWeapon(new Weapon_BitLaser(this, game));

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