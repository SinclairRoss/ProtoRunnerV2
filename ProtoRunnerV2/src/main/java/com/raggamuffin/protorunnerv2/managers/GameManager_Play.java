package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gamelogic.IncommingMissileAlarm;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.gameobjects.VehicleType;
import com.raggamuffin.protorunnerv2.utils.FormationHelper;
import com.raggamuffin.protorunnerv2.utils.SpawnHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.ArrayList;

public class GameManager_Play extends GameManager
{
    private VehicleManager m_VehicleManager;
    private IncommingMissileAlarm m_MissileAlarm;

    public GameManager_Play(GameLogic game)
    {
        super(game);
        m_VehicleManager = m_Game.GetVehicleManager();
        m_MissileAlarm = new IncommingMissileAlarm(game);
    }

    @Override
    public void Update(double deltaTime)
    {
        m_MissileAlarm.Update();

        if(m_VehicleManager.GetTeam(AffiliationKey.RedTeam).size() == 0)
        {
            m_VehicleManager.SpawnVehicle(VehicleType.SweeperBot, 0, 5, 10);

            /*
            double spawnDistance = 100;
            Vector3 position = SpawnHelper.FindRandomSpawnLocation(m_VehicleManager.GetPlayerPosition(), spawnDistance);
            Vector3 forward = SpawnHelper.FindSpawnForward(position, m_VehicleManager.GetPlayer().GetPosition());

            double horizSpacing = 7.0;
            double vertSpacing = 3.0;

            ArrayList<Vector3> formation = FormationHelper.CreateWedgeFormation(position, forward, horizSpacing, vertSpacing, 7);

            for(Vector3 pos : formation)
            {
                m_VehicleManager.SpawnVehicle(VehicleType.Bit, pos.I, pos.K, forward.Yaw());
            }

            formation.clear();

            Vector3 offset = new Vector3(forward);
            offset.Scale(-3);
            position.Add(offset);

            formation = FormationHelper.CreateLineFormation(position, forward, horizSpacing, 1);

            for(Vector3 pos : formation)
            {
                m_VehicleManager.SpawnVehicle(VehicleType.Carrier, pos.I, pos.K, forward.Yaw());
            }

            for(Vector3 pos : formation)
            {
                m_VehicleManager.SpawnVehicle(VehicleType.Tank, pos.I, pos.K, forward.Yaw());
            }

            for(Vector3 pos : formation)
            {
                m_VehicleManager.SpawnVehicle(VehicleType.LaserStar, pos.I, pos.K, forward.Yaw());
            }
            */
        }
    }

    @Override
    public void Initialise()
    {
        m_VehicleManager.SpawnPlayer();
      //  m_VehicleManager.SpawnVehicle(VehicleType.Wingman, 4, 0, 0);
     //   m_VehicleManager.SpawnVehicle(VehicleType.Wingman, -4, 0, 0);

        Vector3 position = new Vector3(0,0,40);
        Vector3 forward = new Vector3(0,0,1);

        double horizSpacing = 7.0;
        double vertSpacing = 3.0;

        ArrayList<Vector3> formation = FormationHelper.CreateWedgeFormation(position, forward, horizSpacing, vertSpacing, 7);

        for(Vector3 pos : formation)
        {
       //     m_VehicleManager.SpawnVehicle(VehicleType.Bit, pos.I, pos.K, forward.Yaw());
        }
    }

    @Override
    public void CleanUp()
    {

    }
}
