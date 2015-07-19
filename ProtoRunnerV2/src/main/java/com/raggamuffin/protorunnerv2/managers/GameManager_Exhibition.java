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

        m_BlueTeamCount = 1;//8;
        m_RedTeamCount =  1;//24;
    }

    @Override
    public void Update(double deltaTime)
    {
        int blueCount = m_VehicleManager.GetTeamCount(AffiliationKey.BlueTeam);
        if(blueCount < m_BlueTeamCount)
        {
            for(int i = 0; i < m_BlueTeamCount - blueCount; i ++)
                m_VehicleManager.SpawnWingmen();
        }

        int redCount = m_VehicleManager.GetTeamCount(AffiliationKey.RedTeam);
        if(redCount < m_RedTeamCount)
        {
            for(int i = 0; i < m_RedTeamCount - redCount; i++)
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
