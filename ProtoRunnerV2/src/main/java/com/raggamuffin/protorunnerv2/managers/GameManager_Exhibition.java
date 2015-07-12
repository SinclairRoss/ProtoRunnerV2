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
        int blueCount = m_VehicleManager.GetTeamCount(AffiliationKey.BlueTeam);
        if(blueCount < 8)
        {
            for(int i = 0; i < 8 - blueCount; i ++)
                m_VehicleManager.SpawnWingmen();
        }

        int redCount = m_VehicleManager.GetTeamCount(AffiliationKey.RedTeam);
        if(redCount < 24)
        {
            for(int i = 0; i < 24 - redCount; i++)
                m_VehicleManager.SpawnSquad();
        }
    }

    @Override
    public void Initialise()
    {

    }

    @Override
    public void CleanUp()
    {

    }
}
