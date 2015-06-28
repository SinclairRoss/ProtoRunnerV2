package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;


public class GameManager_Exhibition extends GameManager
{
    private VehicleManager m_VehicleManager;

    public GameManager_Exhibition(GameLogic game)
    {
        super(game);

        m_VehicleManager = m_Game.GetVehicleManager();
    }

    @Override
    public void Update(double deltaTime)
    {
        if(m_VehicleManager.GetTeamCount(AffiliationKey.BlueTeam) == 0)
        {
            for(int i = 0; i < 3; i ++)
                m_VehicleManager.SpawnWingmen();
        }

        if(m_VehicleManager.GetTeamCount(AffiliationKey.RedTeam) == 0)
        {
            m_VehicleManager.SpawnSquad();
        }
    }

    @Override
    public void Initialise()
    {

    }
}
