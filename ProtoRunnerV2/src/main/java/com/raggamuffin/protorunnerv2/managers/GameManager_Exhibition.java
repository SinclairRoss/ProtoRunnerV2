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

        m_BlueTeamCount = 3;//8;
        m_RedTeamCount =  8;//24;
    }

    @Override
    public void Update(double deltaTime)
    {
        int blueCount = m_VehicleManager.GetTeamCount(AffiliationKey.BlueTeam);

        if(blueCount <= 0)
        {
            for(int i = 0; i < 3; i++)
            {
                m_VehicleManager.SpawnWingmen(i);
            }
        }

        int redCount = m_VehicleManager.GetTeamCount(AffiliationKey.RedTeam);
        if(redCount <= 0)
        {
            m_VehicleManager.SpawnSquad(30);
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
