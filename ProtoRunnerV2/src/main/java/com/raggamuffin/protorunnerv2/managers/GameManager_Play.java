package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.VehicleType;
import com.raggamuffin.protorunnerv2.utils.FormationHelper;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
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
    }

    static int i = 0;

    @Override
    public void Update(double deltaTime)
    {
        m_WaveManager.Update();
    }

    @Override
    public void CleanUp()
    {}
}
