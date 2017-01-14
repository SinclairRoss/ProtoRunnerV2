package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.VehicleType;

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

        if(GameLogic.TEST_MODE)
        {
            m_VehicleManager.SpawnVehicle(VehicleType.WeaponTestBot, 0, 0, 15);
        }
        else
        {
            m_VehicleManager.SpawnVehicle(VehicleType.Wingman, 5, 0, 0);
            m_VehicleManager.SpawnVehicle(VehicleType.Wingman, -5, 0, 0);
        }
    }

    @Override
    public void Update(double deltaTime)
    {
        if(!GameLogic.TEST_MODE)
        {
            m_WaveManager.Update(deltaTime);
        }
    }

    @Override
    public void CleanUp()
    {}
}
