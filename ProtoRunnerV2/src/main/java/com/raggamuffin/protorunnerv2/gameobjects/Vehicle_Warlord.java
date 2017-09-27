package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   08/04/2017

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
import com.raggamuffin.protorunnerv2.utils.Spring1;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.HexplosiveLauncher;
import com.raggamuffin.protorunnerv2.weapons.Weapon_None;

import java.util.ArrayList;

public class Vehicle_Warlord extends Vehicle
{
    private enum WarlordState
    {
        Normal,
        Crashing,
        Crashed
    }
    private WarlordState m_State;

    private int NUM_DRONES = 3;

    private VehicleManager m_VehicleManager;
    private AIController m_AIController;
    private Spring1 m_HoverSpring;
    private ArrayList<Vehicle_WarlordDrone> m_Drones;
    private Timer m_RestoreTimer;

    private HexplosiveLauncher m_HexplosiveLauncher;

    public Vehicle_Warlord(GameLogic game, Vector3 position)
    {
        super(game, ModelType.WeaponDrone, position, 5.0, 12, VehicleClass.StandardVehicle, true, PublishedTopics.EnemyDestroyed, AffiliationKey.RedTeam);
        SetScale(3);

        m_State = WarlordState.Normal;

        m_VehicleManager = game.GetVehicleManager();

        m_HoverSpring = new Spring1(45);

        SetColour(Colours.HannahExperimentalAB);

        m_Engine = new Engine(game, this);
        m_Engine.SetMaxTurnRate(1.0);
        m_Engine.SetMaxEngineOutput(70);
        m_Engine.SetDodgeOutput(0);

        SelectWeapon(new Weapon_None(this, game));

        VehicleManager vehicleManager = game.GetVehicleManager();
        NavigationalBehaviourInfo navInfo = new NavigationalBehaviourInfo(0.4, 1.0, 0.7, 0.6);
        m_AIController = new AIController(this, vehicleManager, game.GetBulletManager(), navInfo, AIBehaviours.Encircle, FireControlBehaviour.None, TargetingBehaviour.Standard);

        m_Drones = new ArrayList(NUM_DRONES);
        SpawnDrones();

        m_RestoreTimer = new Timer(5.0);

        m_HexplosiveLauncher = new HexplosiveLauncher(this, game);
    }

    @Override
    public void Update(double deltaTime)
    {
        float hoverHeight = -1;

        switch(m_State)
        {
            case Normal:
            {
                hoverHeight = 10;

               // Vehicle target = GetTarget();
                //if(target != null)
             //   {
                    m_HexplosiveLauncher.Fire(new Vector3());//target.GetPosition());
                //}

                if (!AnyDronesRemaining())
                {
                    m_State = WarlordState.Crashing;
                }

                break;
            }
            case Crashing:
            {
                Vector3 position = GetPosition();
                if(position.Y <= 0)
                {
                    position.Y = 0;
                    m_State = WarlordState.Crashed;
                    m_RestoreTimer.Start();

                    m_Engine.TurnOff();
                }

                break;
            }
            case Crashed:
            {
                if(m_RestoreTimer.HasElapsed())
                {
                    SpawnDrones();
                    m_State = WarlordState.Normal;

                    m_Engine.TurnOn();
                }

                break;
            }
        }

        m_HexplosiveLauncher.Update();

        double force = m_HoverSpring.CalculateSpringForce(GetPosition().Y, hoverHeight);
        ApplyForce(Vector3.UP, force * deltaTime);

        m_AIController.Update(deltaTime);
        super.Update(deltaTime);
    }

    private boolean AnyDronesRemaining()
    {
        boolean dronesRemaining = false;

        for(int i = 0; i < NUM_DRONES; ++i)
        {
            if(m_Drones.get(i).IsValid())
            {
                dronesRemaining = true;
                break;
            }
        }

        return dronesRemaining;
    }

    private void SpawnDrones()
    {
        m_Drones.clear();
        for(int i = 0; i < NUM_DRONES; ++i)
        {
            m_Drones.add(m_VehicleManager.SpawnDrone(this, i, NUM_DRONES));
        }
    }

    private Vehicle GetTarget()
    {
        return m_AIController.GetSituationalAwareness().GetTargetSensor().GetTarget();
    }

    @Override
    public void CleanUp()
    {
        super.CleanUp();
        m_AIController.CleanUp();
    }
}