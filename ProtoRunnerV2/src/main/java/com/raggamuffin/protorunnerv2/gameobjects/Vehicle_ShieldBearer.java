package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   25/06/2016

import com.raggamuffin.protorunnerv2.ai.AIBehaviours;
import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.ai.FireControlBehaviour;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.weapons.Weapon_None;

import java.util.ArrayList;

public class Vehicle_ShieldBearer extends Vehicle
{
    private AIController m_AIController;

    private final int TENTACLE_COUNT = 5;
    private ArrayList<Rope> m_Tentacles;
    private ArrayList<TentacleController> m_TentacleControllers;

    public Vehicle_ShieldBearer(GameLogic game)
    {
        super(game);

        m_Scale.SetVector(3);

        SelectWeapon(new Weapon_None(this, game));

        m_Model = ModelType.ShieldBearer;
        SetBaseColour(Colours.BlockPurple);

        m_Mass = 1000;
        m_Engine = new Engine_Standard(this, game);
        m_Engine.SetMaxTurnRate(1.5);
        m_Engine.SetMaxEngineOutput(100000);
        m_Engine.SetDodgeOutput(16000);
        m_BoundingRadius = 2;

        SetAffiliation(AffiliationKey.RedTeam);

        m_AIController = new AIController(this, game.GetVehicleManager(), game.GetBulletManager(), AIBehaviours.Encircle, FireControlBehaviour.Standard);

        m_OnDeathPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.EnemyDestroyed);

       CreateTentacles(TENTACLE_COUNT, game);
    }

    @Override
    public void Update(double deltaTime)
    {
        m_AIController.Update(deltaTime);

        for(TentacleController controller : m_TentacleControllers)
        {
            controller.Update(deltaTime);
        }

        super.Update(deltaTime);
    }

    @Override
    public void CleanUp()
    {
        for(Rope tentacle : m_Tentacles)
        {
            tentacle.SetHeadAnchor(null);
        }
    }

    private void CreateTentacles(int count, GameLogic game)
    {
        m_TentacleControllers = new ArrayList<>(count);
        m_Tentacles = new ArrayList<>(count);

        for(int i = 0; i < count; i++)
        {
            TentacleController controller = new TentacleController(this);
            m_TentacleControllers.add(controller);

            Rope tentacle = new Rope(this, controller, m_BaseColour, m_AltColour);
            m_Tentacles.add(tentacle);

            game.GetGameObjectManager().AddObject(tentacle);
            game.AddRopeToRenderer(tentacle);
        }
    }
}
