package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gamelogic.IncommingMissileAlarm;
import com.raggamuffin.protorunnerv2.gameobjects.VehicleType;
import com.raggamuffin.protorunnerv2.utils.FormationHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.ArrayList;

public class GameManager_Play extends GameManager
{
    private VehicleManager m_VehicleManager;
    private IncommingMissileAlarm m_MissileAlarm;

    private EnemyWaveManager m_WaveManager;

    public GameManager_Play(GameLogic game)
    {
        super(game);

        m_VehicleManager = m_Game.GetVehicleManager();
        m_MissileAlarm = new IncommingMissileAlarm(game);
        m_WaveManager = new EnemyWaveManager(game);
    }

    @Override
    public void Initialise()
    {
        m_VehicleManager.SpawnPlayer();

        m_VehicleManager.SpawnVehicle(VehicleType.Wingman, 5, 0, 0);
        m_VehicleManager.SpawnVehicle(VehicleType.Wingman, -5, 0, 0);

        Vector3 position = new Vector3(0,0,40);
        Vector3 forward = new Vector3(0,0,1);

        ArrayList<Vector3> formation = FormationHelper.CreateWedgeFormation(position, forward, 7, 3, 7);
        for(Vector3 vector : formation)
        {
            m_VehicleManager.SpawnVehicle(VehicleType.Bit, vector.I, vector.K, forward.Yaw());
        }
    }

    @Override
    public void Update(double deltaTime)
    {
        m_MissileAlarm.Update();

        m_WaveManager.Update(deltaTime);
    }

    @Override
    public void CleanUp()
    {

    }
}
