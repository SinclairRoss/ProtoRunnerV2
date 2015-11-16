package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ai.AIBehaviours;
import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.ai.FireControlBehaviour;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.weapons.Weapon_PunkShot;

public class Bit extends Vehicle
{
	private AIController m_AIController;

	public Bit(GameLogic game)
	{
		super(game);

		m_Model = ModelType.Bit;
        SetBaseColour(Colours.CalvinOrange);
		m_Position.SetVector(10, 0, 10);

        m_Engine = new Engine_Standard(this, game, new EngineUseBehaviour_Null());
		m_Engine.SetMaxTurnRate(1.5);
		m_Engine.SetMaxEngineOutput(2500);
        m_BoundingRadius = 2;

		SetAffiliation(AffiliationKey.RedTeam); 
		
		SelectWeapon(new Weapon_PunkShot(this, game));

		m_AIController = new AIController(this, game.GetVehicleManager(), game.GetBulletManager(), AIBehaviours.EngageTarget, FireControlBehaviour.Standard);

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

    }
} 