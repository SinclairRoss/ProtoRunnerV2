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

public class WeaponTestBot extends Vehicle
{
    private AIController m_AIController;

    public WeaponTestBot(GameLogic game)
    {
        super(game, ModelType.Bit);

        SetColourScheme(Colours.Pink70, Colours.RunnerBlue);

        m_Position.SetVector(10, 0, 10);

        m_Mass = 100;
        m_Engine = new Engine_Standard(this, game);
        m_Engine.SetMaxTurnRate(0); //1.5
        m_Engine.SetMaxEngineOutput(0); //10000
        m_Engine.SetDodgeOutput(0);
        m_BoundingRadius = 2;

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