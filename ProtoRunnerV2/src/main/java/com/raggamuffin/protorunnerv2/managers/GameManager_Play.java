package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gamelogic.IncommingMissileAlarm;
import com.raggamuffin.protorunnerv2.gameobjects.SpawnEffect;
import com.raggamuffin.protorunnerv2.gameobjects.VehicleType;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.FormationHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.ArrayList;

public class GameManager_Play extends GameManager
{
    private VehicleManager m_VehicleManager;
    private EnemyWaveManager m_WaveManager;

    public GameManager_Play(GameLogic game)
    {
        super(game);

        m_VehicleManager = m_Game.GetVehicleManager();
        m_WaveManager = new EnemyWaveManager(game);
    }

    @Override
    public void Initialise()
    {
        m_VehicleManager.SpawnPlayer();

        m_VehicleManager.SpawnVehicle(VehicleType.Wingman, 5, 0, 0);
        m_VehicleManager.SpawnVehicle(VehicleType.Wingman, -5, 0, 0);

       // m_VehicleManager.SpawnVehicle(VehicleType.Bit, 10, 0, 0);
    }

    @Override
    public void Update(double deltaTime)
    {
        m_WaveManager.Update(deltaTime);
    }

    @Override
    public void CleanUp()
    {}
}
