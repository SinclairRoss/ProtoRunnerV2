package com.raggamuffin.protorunnerv2.tutorial;

import com.raggamuffin.protorunnerv2.ai.VehicleInfo;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public class TutorialEvent_LockStrafing extends TutorialEvent
{
    private boolean m_On;

    public TutorialEvent_LockStrafing(GameLogic game)
    {
        super(game);

        m_On = false;
    }

    @Override
    public void On()
    {
        m_On = true;
    }

    @Override
    public void Off()
    {
        m_On = false;
    }

    @Override
    public void Update()
    {
        if(!m_On)
            return;

        Vehicle player = m_Game.GetVehicleManager().GetPlayer();

        if(player == null)
            return;

        if(player.GetVehicleInfo().GetMovmentState() != VehicleInfo.MovementStates.Normal)
            player.UseRearEngine();
    }
}
