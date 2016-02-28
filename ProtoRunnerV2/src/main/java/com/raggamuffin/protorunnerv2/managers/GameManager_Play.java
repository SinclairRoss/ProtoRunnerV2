package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gamelogic.IncommingMissileAlarm;
import com.raggamuffin.protorunnerv2.gameobjects.VehicleType;
import com.raggamuffin.protorunnerv2.utils.HelperFunctions;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class GameManager_Play extends GameManager
{
    private VehicleManager m_VehicleManager;
    private IncommingMissileAlarm m_MissileAlarm;

    public GameManager_Play(GameLogic game)
    {
        super(game);
        m_VehicleManager = m_Game.GetVehicleManager();
        m_MissileAlarm = new IncommingMissileAlarm(game);
    }

    @Override
    public void Update(double deltaTime)
    {
        m_MissileAlarm.Update();

        if(m_VehicleManager.GetTeamCount(AffiliationKey.RedTeam) == 0)
        {
           // m_VehicleManager.SpawnSquad(120);
           // m_VehicleManager.SpawnVehicle(VehicleType.WeaponTestBot, new Vector3(0,0,60));
            //m_VehicleManager.SpawnVehicle(VehicleType.TargetBot, new Vector3(-10,0,40));
           // m_VehicleManager.SpawnVehicle(VehicleType.TargetBot, new Vector3(-20,0,40));
           // m_VehicleManager.SpawnVehicle(VehicleType.TargetBot, new Vector3(10,0,40));
          //  m_VehicleManager.SpawnVehicle(VehicleType.TargetBot, new Vector3(20,0,40));
        }
    }

    @Override
    public void Initialise()
    {
     //   m_VehicleManager.SpawnWingmen(-3);
      //  m_VehicleManager.SpawnWingmen(3);
        m_VehicleManager.SpawnPlayer();
    }

    @Override
    public void CleanUp()
    {

    }
}
