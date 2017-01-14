package com.raggamuffin.protorunnerv2.managers;

// Author: Sinclair Ross
// Date:   18/09/2016

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.VehicleType;
import com.raggamuffin.protorunnerv2.utils.FormationHelper;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.SpawnHelper;
import com.raggamuffin.protorunnerv2.utils.Timer_Accumulation;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.ArrayList;

public class EnemyWaveManager
{
    private final double MAX_ENEMIES_IN_GAME = 20;

    private Timer_Accumulation m_SpawnTimer;

    private int m_RegularsPerSquad;
    private int m_ElitesPerSquad;

    private ArrayList<VehicleType> m_Regulars;
    private ArrayList<VehicleType> m_Elites;

    private final VehicleManager m_VManager;

    public EnemyWaveManager(GameLogic game)
    {
        m_RegularsPerSquad = 5;
        m_ElitesPerSquad = 1;

        m_Regulars = new ArrayList<>();
        m_Regulars.add(VehicleType.Bit);
        m_Regulars.add(VehicleType.Bit);

        m_Elites = new ArrayList<>();
        m_Elites.add(VehicleType.Carrier);
        m_Elites.add(VehicleType.LaserStar);
        m_Elites.add(VehicleType.ShieldBearer);
        m_Elites.add(VehicleType.Tank);

        m_VManager = game.GetVehicleManager();

        m_SpawnTimer = new Timer_Accumulation(8);
    }

    public void Update(double deltaTime)
    {
       m_SpawnTimer.Update(deltaTime);

        if(m_SpawnTimer.TimedOut())
        {
            SpawnSquad();
            m_SpawnTimer.ResetTimer();
        }
    }

    private void test()
    {
        m_VManager.SpawnVehicle(VehicleType.WeaponTestBot, 0, 0, 0);
    }

    private void SpawnSquad()
    {
        Vector3 spawnForward = (m_VManager.GetPlayer() != null) ? m_VManager.GetPlayer().GetForward() : new Vector3(0,0,1);

        Vector3 position = SpawnHelper.FindRandomSpawnLocation(m_VManager.GetPlayerPosition(), spawnForward, 100);
        Vector3 forward = SpawnHelper.FindSpawnForward(position, m_VManager.GetPlayerPosition());
        ArrayList<Vector3> formation = FormationHelper.CreateWedgeFormation(position, forward, 7, 3, m_RegularsPerSquad);
        int regularIndex = MathsHelper.RandomInt(0, m_Regulars.size() - 1);

        for(int i = 0; i < m_RegularsPerSquad; ++i)
        {
            Vector3 spawnPos = formation.get(i);
            m_VManager.SpawnVehicle(m_Regulars.get(regularIndex), spawnPos.I, spawnPos.K, forward.Yaw());
        }

        formation.clear();

        position.Subtract(forward.I * 5, forward.J * 5, forward.K * 5);
        formation = FormationHelper.CreateWedgeFormation(position, forward, 7, 3, m_ElitesPerSquad);
        int eliteIndex = MathsHelper.RandomInt(0, m_Elites.size());

        for(int i = 0; i < m_ElitesPerSquad; ++i)
        {
            Vector3 spawnPos = formation.get(i);
            m_VManager.SpawnVehicle(m_Elites.get(eliteIndex), spawnPos.I, spawnPos.K, forward.Yaw());
        }
    }
}
