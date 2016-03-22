package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ai.AIBehaviours;
import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.ai.FireControlBehaviour;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.weapons.Weapon_LaserBurner;
import com.raggamuffin.protorunnerv2.weapons.Weapon_MultiLaser;
import com.raggamuffin.protorunnerv2.weapons.Weapon_PunkShot;

public class Vehicle_LaserStar extends Vehicle
{
    private AIController m_AIController;

    private Weapon_LaserBurner[] m_LaserBurners;

    public Vehicle_LaserStar(GameLogic game)
    {
        super(game);

        m_LaserBurners = new Weapon_LaserBurner[3];

        m_Model = ModelType.ThreePointStar;
        SetBaseColour(Colours.Orange);
        m_Position.SetVector(10, 0, 10);

        m_Engine = new Engine_Standard(this, game);
        m_Engine.SetMaxTurnRate(1.5);
        m_Engine.SetMaxEngineOutput(2500);
        m_Engine.SetDodgeOutput(16000);
        m_BoundingRadius = 2;

        SetAffiliation(AffiliationKey.RedTeam);

        SelectWeapon(new Weapon_MultiLaser(this, game, 3));

        m_AIController = new AIController(this, game.GetVehicleManager(), game.GetBulletManager(), AIBehaviours.Encircle, FireControlBehaviour.LaserSpinner);

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