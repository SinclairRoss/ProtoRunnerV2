package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;

public class GameManager_Play extends GameManager
{
    private VehicleManager m_VehicleManager;

    public GameManager_Play(GameLogic game)
    {
        super(game);
        m_VehicleManager = m_Game.GetVehicleManager();
    }

    @Override
    public void Update(double deltaTime)
    {
        if(m_VehicleManager.GetTeamCount(AffiliationKey.RedTeam) == 0)
        {
            m_VehicleManager.SpawnSquad();
        }
    }

    @Override
    public void Initialise()
    {
        m_VehicleManager.SpawnWingmen();
        m_VehicleManager.SpawnWingmen();
        m_VehicleManager.SpawnPlayer();
    }
}
