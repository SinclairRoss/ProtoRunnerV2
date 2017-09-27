package com.raggamuffin.protorunnerv2.managers;

// Author: Sinclair Ross
// Date:   18/09/2016

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.gameobjects.VehicleType;
import com.raggamuffin.protorunnerv2.utils.FormationHelper;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.SpawnHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class EnemyWaveManager
{
    private int m_RegularsPerSquad;
    private int m_ElitesPerSquad;

    private ArrayList<VehicleType> m_Regulars;
    private ArrayList<VehicleType> m_Elites;

    private final VehicleManager m_VManager;

    private Vector3 m_SpawnForward;

    public EnemyWaveManager(GameLogic game)
    {
        m_RegularsPerSquad = 5;
        m_ElitesPerSquad = 0;

        m_Regulars = new ArrayList<>();
        m_Regulars.add(VehicleType.Bit);
        m_Regulars.add(VehicleType.Bit);

        m_Elites = new ArrayList<>();

        m_Elites.add(VehicleType.Carrier);
        m_Elites.add(VehicleType.LaserStar);
        m_Elites.add(VehicleType.ShieldBearer);
        m_Elites.add(VehicleType.Warlord);

        m_VManager = game.GetVehicleManager();

        m_SpawnForward = new Vector3();
    }

    public void Update()
    {
        if(m_VManager.GetTeam(AffiliationKey.RedTeam).isEmpty())
        {
            SpawnSquad();
        }
    }

    private void SpawnSquad()
    {
        Vehicle player = m_VManager.GetPlayer();
        Vector3 playerForward = (player != null) ? player.GetForward() : new Vector3(0,0,1);

        Vector3 position = SpawnHelper.FindRandomSpawnLocation(m_VManager.GetPlayerPosition(), playerForward, 100);
        Vector3 forward = SpawnHelper.FindSpawnForward(position, m_VManager.GetPlayerPosition());

        ArrayList<Vector3> formation = FormationHelper.CreateWedgeFormation(position, forward, 7, 3, m_RegularsPerSquad);
        int regularIndex = MathsHelper.RandomInt(0, m_Regulars.size() - 1);

        for(int i = 0; i < m_RegularsPerSquad; ++i)
        {
            Vector3 spawnPos = formation.get(i);
            m_VManager.SpawnVehicle(m_Regulars.get(regularIndex), spawnPos.X, spawnPos.Z, forward);
        }

        formation.clear();

        position.Subtract(forward.X * 5, forward.Y * 5, forward.Z * 5);
        formation = FormationHelper.CreateWedgeFormation(position, forward, 7, 3, m_ElitesPerSquad);
        int eliteIndex = MathsHelper.RandomInt(0, m_Elites.size());

        for(int i = 0; i < m_ElitesPerSquad; ++i)
        {
            Vector3 spawnPos = formation.get(i);
            m_VManager.SpawnVehicle(m_Elites.get(eliteIndex), spawnPos.X, spawnPos.Z, forward);
        }
    }
}
