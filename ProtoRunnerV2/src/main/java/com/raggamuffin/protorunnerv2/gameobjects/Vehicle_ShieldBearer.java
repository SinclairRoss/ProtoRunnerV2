package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   25/06/2016

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
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.Weapon_None;

import java.util.ArrayList;

public class Vehicle_ShieldBearer extends Vehicle
{
    private AIController m_AIController;

    private final int TENTACLE_COUNT = 5;
    private final double TENTACLE_RANGE = 60.0;

    private ArrayList<Tentacle> m_Tentacles;

    private GameLogic m_Game;

    public Vehicle_ShieldBearer(GameLogic game, Vector3 position)
    {
        super(game, ModelType.ShieldBearer, position, 5.0, 12, VehicleClass.StandardVehicle, true, PublishedTopics.EnemyDestroyed, AffiliationKey.RedTeam);

        m_Game = game;

        SetScale(3);

        SelectWeapon(new Weapon_None(this, game));

        SetColour(Colours.CalvinOrange);

        m_Engine = new Engine_Standard(this, game);
        m_Engine.SetMaxTurnRate(1.5);
        m_Engine.SetMaxEngineOutput(90);
        m_Engine.SetDodgeOutput(0);

        NavigationalBehaviourInfo navInfo = new NavigationalBehaviourInfo(0.4, 1.0, 0.7, 0.6);
        m_AIController = new AIController(this, game.GetVehicleManager(), game.GetBulletManager(), navInfo, AIBehaviours.StickWithThePack, FireControlBehaviour.None, TargetingBehaviour.Standard);

        CreateTentacles(game);
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

        for(int i = 0; i < TENTACLE_COUNT; ++i)
        {
            Tentacle tentacle = m_Tentacles.get(i);
            tentacle.KillTentacle();
        }

        m_Tentacles.clear();

        m_AIController.CleanUp();
    }

    private void CreateTentacles(GameLogic game)
    {
        m_Tentacles = new ArrayList<>(TENTACLE_COUNT);

        double delta = (Math.PI * 2) / TENTACLE_COUNT;

        for(int i = 0; i < TENTACLE_COUNT; ++i)
        {
            Vehicle_TentacleController controller = new Vehicle_TentacleController(m_Game, this, TENTACLE_RANGE);
            controller.SetPosition(Math.sin(delta * i) * 5, 0.0, Math.cos(delta * i) * 5);
            controller.GetPosition().Add(GetPosition());
            m_Game.GetVehicleManager().AddVehicle(controller);

            Tentacle tentacle = new Tentacle(this, controller, GetColour(), GetColour());
            m_Tentacles.add(tentacle);

            game.GetRopeManager().AddObject(tentacle);
        }
    }
}
