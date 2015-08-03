package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;


public class GameManager_Exhibition extends GameManager
{
    private VehicleManager m_VehicleManager;
    private int m_BlueTeamCount;
    private int m_RedTeamCount;

    public GameManager_Exhibition(GameLogic game)
    {
        super(game);

        m_VehicleManager = m_Game.GetVehicleManager();

        m_BlueTeamCount = 8;
        m_RedTeamCount =  24;
    }

    @Override
    public void Update(double deltaTime)
    {
        int blueCount = m_VehicleManager.GetTeamCount(AffiliationKey.BlueTeam);

        if(blueCount < 8)
        {
            for(int i = 0; i < 8 - blueCount; i++)
            {
                m_VehicleManager.SpawnWingmen(i);
            }
        }

        int redCount = m_VehicleManager.GetTeamCount(AffiliationKey.RedTeam);
        if(redCount < 24)
        {
            for(int i = 0; i < 24 - redCount; i++)
            {
               m_VehicleManager.SpawnSquad(30);
            }
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
