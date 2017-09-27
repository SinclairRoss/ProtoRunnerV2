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

    public GameManager_Exhibition(GameLogic game)
    {
        super(game);

        m_VehicleManager = m_Game.GetVehicleManager();
    }

    @Override
    public void Update(double deltaTime)
    {
        if(m_VehicleManager.GetTeamCount(AffiliationKey.RedTeam) == 0)
        {
            Vector3 position = new Vector3(-SPAWN_DISTANCE_INITIAL, 0, 0);
            Vector3 forward = new Vector3(1, 0, 0);

            double horizSpacing = 7.0;
            double vertSpacing = 3.0;

            ArrayList<Vector3> formation = FormationHelper.CreateWedgeFormation(position, forward, horizSpacing, vertSpacing, 1);

            int numBaddies = formation.size();
            for (int i = 0; i < numBaddies; ++i)
            {
                Vector3 pos = formation.get(i);
                m_VehicleManager.SpawnVehicle(VehicleType.Bit, pos.X, pos.Z, forward);
            }
        }

        // if (m_VehicleManager.GetTeamCount(AffiliationKey.RedTeam) == 0)
       // {
       //     Vector3 position = new Vector3(SpawnHelper.FindRandomSpawnLocation(SPAWN_DISTANCE));
       //     Vector3 forward = new Vector3(SpawnHelper.FindSpawnForward(position, new Vector3()));
//
       //     double horizSpacing = 7.0;
       //     double vertSpacing = 3.0;
//
       //     ArrayList<Vector3> formation = FormationHelper.CreateWedgeFormation(position, forward, horizSpacing, vertSpacing, 12);
//
       //     int numBaddies = formation.size();
       //     for (int i = 0; i < numBaddies; ++i)
       //     {
       //         Vector3 pos = formation.get(i);
       //         m_VehicleManager.SpawnVehicle(VehicleType.Bit, pos.X, pos.Z, forward);
       //     }
       // }
//
       // if (m_VehicleManager.GetTeamCount(AffiliationKey.BlueTeam) == 0)
       // {
       //     Vector3 position = new Vector3(SpawnHelper.FindRandomSpawnLocation(SPAWN_DISTANCE));
       //     Vector3 forward = new Vector3(SpawnHelper.FindSpawnForward(position, new Vector3()));
//
       //     double horizSpacing = 5.0;
       //     double vertSpacing = 1.0;
//
       //     ArrayList<Vector3> formation = FormationHelper.CreateWedgeFormation(position, forward, horizSpacing, vertSpacing, 3);
//
       //     int numWingment = formation.size();
       //     for (int i = 0; i < numWingment; ++i)
       //     {
       //         Vector3 pos = formation.get(i);
       //       //  m_VehicleManager.SpawnVehicle(VehicleType.Wingman, pos.X, pos.Z, forward);
       //     }
       // }
    }

    @Override
    public void Initialise()
    {
      Vector3 position = new Vector3(-SPAWN_DISTANCE_INITIAL,0,0);
      Vector3 forward = new Vector3(1,0,0);

      double horizSpacing = 7.0;
      double vertSpacing = 3.0;

      ArrayList<Vector3> formation = FormationHelper.CreateWedgeFormation(position, forward, horizSpacing, vertSpacing, 1);

      int numBaddies = formation.size();
      for(int i = 0; i < numBaddies; ++i)
      {
          Vector3 pos = formation.get(i);
          m_VehicleManager.SpawnVehicle(VehicleType.Bit, 20, 20, forward);
      }

       //m_VehicleManager.SpawnVehicle(VehicleType.Bit, 2, 0, new Vector3(0,0,1));
       //m_VehicleManager.SpawnVehicle(VehicleType.Bit, 0, 0, new Vector3(0,0,1));
       //m_VehicleManager.SpawnVehicle(VehicleType.Bit, -2, 0, new Vector3(0,0,1));
       //m_VehicleManager.SpawnVehicle(VehicleType.Bit, -4, 0, new Vector3(0,0,1));
       //m_VehicleManager.SpawnVehicle(VehicleType.Bit, -6, 0, new Vector3(0,0,1));
       //m_VehicleManager.SpawnVehicle(VehicleType.Bit, -8, 0, new Vector3(0,0,1));
       //m_VehicleManager.SpawnVehicle(VehicleType.Bit, -10, 0, new Vector3(0,0,1));

       // if(m_VehicleManager.GetTeamCount(AffiliationKey.BlueTeam) == 0)
       // {
       //     position.SetVector(SPAWN_DISTANCE_INITIAL,0,0);
       //     forward.SetVector(-1,0,0);
//
       //     horizSpacing = 5.0;
       //     vertSpacing = 1.0;
//
       //     formation = FormationHelper.CreateWedgeFormation(position, forward, horizSpacing, vertSpacing, 3);
//
       //     int numWingmen = formation.size();
       //     for(int i = 0; i < numWingmen; ++i)
       //     {
       //         Vector3 pos = formation.get(i);
       //       //  m_VehicleManager.SpawnVehicle(VehicleType.Wingman, pos.X, pos.Z, forward);
       //     }
       // }
    }

    @Override
    public void CleanUp()
    {}
}
