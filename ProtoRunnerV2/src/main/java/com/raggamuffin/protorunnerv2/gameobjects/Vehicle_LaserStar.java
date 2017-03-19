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
import com.raggamuffin.protorunnerv2.weapons.Weapon_LaserBurner;
import com.raggamuffin.protorunnerv2.weapons.Weapon_MultiLaser;

public class Vehicle_LaserStar extends Vehicle
{
    private AIController m_AIController;

    private Weapon_LaserBurner[] m_LaserBurners;

    public Vehicle_LaserStar(GameLogic game)
    {
        super(game, ModelType.ThreePointStar, 2);

        m_LaserBurners = new Weapon_LaserBurner[3];

      //  SetColourScheme(Colours.HannahExperimentalBA, Colours.HannahExperimentalBB);

        SetColour(Colours.HannahExperimentalBA);

        m_Engine = new Engine_Standard(this, game);
        m_Engine.SetMaxTurnRate(1.5);
        m_Engine.SetMaxEngineOutput(30);
        m_Engine.SetDodgeOutput(0);

        m_HullPoints = 12;

        SetAffiliation(AffiliationKey.RedTeam);

        SelectWeapon(new Weapon_MultiLaser(this, game, 3));
        m_PrimaryWeapon.DeactivateComponent();

        NavigationalBehaviourInfo navInfo = new NavigationalBehaviourInfo(0.4, 1.0, 0.7, 0.6);
        m_AIController = new AIController(this, game.GetVehicleManager(), game.GetBulletManager(), navInfo, AIBehaviours.Encircle, FireControlBehaviour.LaserSpinner, TargetingBehaviour.Standard);

        m_OnDeathPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.EnemyDestroyed);
    }

    @Override
    public void Update(double deltaTime)
    {
        m_AIController.Update(deltaTime);

        super.Update(deltaTime);
    }
} 