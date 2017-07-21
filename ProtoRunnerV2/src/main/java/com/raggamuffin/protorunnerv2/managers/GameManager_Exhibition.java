package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.gameobjects.VehicleType;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Burst;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.FormationHelper;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.SpawnHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.ArrayList;

public class GameManager_Exhibition extends GameManager
{
    private final double SPAWN_DISTANCE_INITIAL = 50;
    private final double SPAWN_DISTANCE = 30.0;

    private static VehicleManager m_VehicleManager;

    private Timer m_EmissionTimer;
    private ParticleEmitter_Burst m_TestEmitter;

    public GameManager_Exhibition(GameLogic game)
    {
        super(game);

        m_VehicleManager = m_Game.GetVehicleManager();

        m_EmissionTimer = new Timer(0.1);
    }

    @Override
    public void Update(double deltaTime)
    {
        if(m_Game.TEST_MODE)
        {
           // if(m_EmissionTimer.HasElapsed())
          //  {
              //  m_EmissionTimer.Start();
            //    m_TestEmitter.Burst();
        //    }
        }
        else
        {
            if (m_VehicleManager.GetTeamCount(AffiliationKey.RedTeam) == 0)
            {
                Vector3 position = SpawnHelper.FindRandomSpawnLocation(SPAWN_DISTANCE);
                Vector3 forward = SpawnHelper.FindSpawnForward(position, new Vector3());

                double horizSpacing = 7.0;
                double vertSpacing = 3.0;

                ArrayList<Vector3> formation = FormationHelper.CreateWedgeFormation(position, forward, horizSpacing, vertSpacing, 5);

                for (Vector3 pos : formation)
                {
                    //      m_VehicleManager.SpawnVehicle(VehicleType.Bit, pos.X, pos.Z, forward.Yaw());
                }

                formation.clear();

                Vector3 offset = new Vector3(forward);
                offset.Scale(-3);
                position.Add(offset);

                formation = FormationHelper.CreateLineFormation(position, forward, horizSpacing, 1);

                for (Vector3 pos : formation)
                {
           //         m_VehicleManager.SpawnVehicle(VehicleType.Carrier, pos.X, pos.Z, forward.Yaw());
                }

                for (Vector3 pos : formation)
                {
           //         m_VehicleManager.SpawnVehicle(VehicleType.ShieldBearer, pos.X, pos.Z, forward.Yaw());
                }

                for (Vector3 pos : formation)
                {
            //        m_VehicleManager.SpawnVehicle(VehicleType.LaserStar, pos.X, pos.Z, forward.Yaw());
                }
            }

            if (m_VehicleManager.GetTeamCount(AffiliationKey.BlueTeam) == 0)
            {
                Vector3 position = SpawnHelper.FindRandomSpawnLocation(SPAWN_DISTANCE);
                Vector3 forward = SpawnHelper.FindSpawnForward(position, new Vector3());

                double horizSpacing = 5.0;
                double vertSpacing = 1.0;

                ArrayList<Vector3> formation = FormationHelper.CreateWedgeFormation(position, forward, horizSpacing, vertSpacing, 3);

                for (Vector3 pos : formation)
                {
                    //m_VehicleManager.SpawnVehicle(VehicleType.Wingman, pos.X, pos.Z, forward.Yaw());
                }
            }
        }
    }

    @Override
    public void Initialise()
    {
        if(GameLogic.TEST_MODE)
        {
            //m_EmissionTimer.Start();
            //m_TestEmitter = new ParticleEmitter_Burst(m_Game, new Colour(Colours.VioletRed), new Colour(Colours.Cyan), 100);

            m_VehicleManager.SpawnVehicle(VehicleType.Warlord, 0, 0, 0);

            return;
        }

       // m_VehicleManager.SpawnVehicle(VehicleType.Warlord, 0, 0, 0);

        if(m_VehicleManager.GetTeamCount(AffiliationKey.RedTeam) == 0)
        {
            Vector3 position = new Vector3(-SPAWN_DISTANCE_INITIAL,0,0);
            Vector3 forward = new Vector3(1,0,0);

            double horizSpacing = 7.0;
            double vertSpacing = 3.0;

            ArrayList<Vector3> formation = FormationHelper.CreateWedgeFormation(position, forward, horizSpacing, vertSpacing, 7);

            for(Vector3 pos : formation)
            {
       //         m_VehicleManager.SpawnVehicle(VehicleType.Bit, pos.X, pos.Z, forward.Yaw());
        //        m_VehicleManager.SpawnVehicle(VehicleType.ShieldBearer, pos.X, pos.Z, forward.Yaw());
            }
        }

        if(m_VehicleManager.GetTeamCount(AffiliationKey.BlueTeam) == 0)
        {
            Vector3 position = new Vector3(SPAWN_DISTANCE_INITIAL,0,0);
            Vector3 forward = new Vector3(-1,0,0);

            double horizSpacing = 5.0;
            double vertSpacing = 1.0;

            ArrayList<Vector3> formation = FormationHelper.CreateWedgeFormation(position, forward, horizSpacing, vertSpacing, 3);

            for(Vector3 pos : formation)
            {
                //m_VehicleManager.SpawnVehicle(VehicleType.Wingman, pos.X, pos.Z, forward.Yaw());
            }
        }
    }

    @Override
    public void CleanUp()
    {}
}
