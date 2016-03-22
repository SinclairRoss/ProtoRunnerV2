package com.raggamuffin.protorunnerv2.tutorial;

import com.raggamuffin.protorunnerv2.ai.VehicleInfo;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle_Runner;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.utils.Timer;

public class TutorialCondition_Boost extends TutorialCondition
{
    private VehicleManager m_VehicleManager;
    private Timer m_BoostTimer;

    public TutorialCondition_Boost(GameLogic game, String message, double time, TutorialEffect... effects)
    {
        super(game, message, OptionalElement.ProgressBar, effects);

        m_VehicleManager = m_Game.GetVehicleManager();
        m_BoostTimer = new Timer(time);
    }

    @Override
    public void Update(double deltaTime)
    {
        Vehicle_Runner player = m_VehicleManager.GetPlayer();

        if(player == null)
            return;

        if(player.GetVehicleInfo().GetAfterBurnerState() != VehicleInfo.AfterBurnerStates.Engaged)
            return;

        m_BoostTimer.Update(deltaTime);
    }

    @Override
    public boolean ConditionComplete()
    {
        return m_BoostTimer.TimedOut();
    }

    @Override
    public double GetProgress()
    {
        return m_BoostTimer.GetProgress();
    }

    @Override
    public void Reset()
    {
        super.Reset();
        m_BoostTimer.ResetTimer();
    }
}
