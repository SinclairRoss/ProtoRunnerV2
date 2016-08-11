package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.VehicleType;
import com.raggamuffin.protorunnerv2.utils.FormationHelper;
import com.raggamuffin.protorunnerv2.utils.SpawnHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.ArrayList;

public class GameManager_Exhibition extends GameManager
{
    private final double SPAWN_DISTANCE_INITIAL = 50;
    private final double SPAWN_DISTANCE = 30.0;

    private VehicleManager m_VehicleManager;

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
            Vector3 position = SpawnHelper.FindRandomSpawnLocation(SPAWN_DISTANCE);
            Vector3 forward = SpawnHelper.FindSpawnForward(position, new Vector3());

            double horizSpacing = 7.0;
            double vertSpacing = 3.0;

            ArrayList<Vector3> formation = FormationHelper.CreateWedgeFormation(position, forward, horizSpacing, vertSpacing, 1);

            for(Vector3 pos : formation)
            {
                m_VehicleManager.SpawnVehicle(VehicleType.Carrier, pos.I, pos.K, forward.Yaw());
            }
        }

        if(m_VehicleManager.GetTeamCount(AffiliationKey.BlueTeam) == 0)
        {
            Vector3 position = SpawnHelper.FindRandomSpawnLocation(SPAWN_DISTANCE);
            Vector3 forward = SpawnHelper.FindSpawnForward(position, new Vector3());

            double horizSpacing = 5.0;
            double vertSpacing = 1.0;

            ArrayList<Vector3> formation = FormationHelper.CreateWedgeFormation(position, forward, horizSpacing, vertSpacing, 3);

            for(Vector3 pos : formation)
            {
                m_VehicleManager.SpawnVehicle(VehicleType.Wingman, pos.I, pos.K, forward.Yaw());
            }
        }
    }

    @Override
    public void Initialise()
    {
        if(m_VehicleManager.GetTeamCount(AffiliationKey.RedTeam) == 0)
        {
            Vector3 position = new Vector3(-SPAWN_DISTANCE_INITIAL,0,0);
            Vector3 forward = new Vector3(1,0,0);

            double horizSpacing = 7.0;
            double vertSpacing = 3.0;

            ArrayList<Vector3> formation = FormationHelper.CreateWedgeFormation(position, forward, horizSpacing, vertSpacing, 7);

            for(Vector3 pos : formation)
            {
                m_VehicleManager.SpawnVehicle(VehicleType.Bit, pos.I, pos.K, forward.Yaw());
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
                m_VehicleManager.SpawnVehicle(VehicleType.Wingman, pos.I, pos.K, forward.Yaw());
            }
        }
    }

    @Override
    public void CleanUp()
    {
    }
}
