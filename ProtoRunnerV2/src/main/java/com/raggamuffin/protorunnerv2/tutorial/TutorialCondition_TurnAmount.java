package com.raggamuffin.protorunnerv2.tutorial;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle_Runner;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.utils.Timer_Accumulation;

public class TutorialCondition_TurnAmount extends TutorialCondition
{
    private VehicleManager m_VehicleManager;
    private Timer_Accumulation m_TurnTimer;

    public TutorialCondition_TurnAmount(GameLogic game, String message, double amount, TutorialEffect... effects)
    {
        super(game, message, OptionalElement.ProgressBar, effects);

        m_TurnTimer = new Timer_Accumulation(amount);
        m_VehicleManager = m_Game.GetVehicleManager();
    }

    @Override
    public void Update(double deltaTime)
    {
        Vehicle_Runner player = m_VehicleManager.GetPlayer();

        if(player == null)
            return;

        double turnRate = Math.abs(player.GetTurnRate());
        m_TurnTimer.Update( turnRate * deltaTime);
    }

    @Override
    public boolean ConditionComplete()
    {
        return m_TurnTimer.TimedOut();
    }

    @Override
    public double GetProgress()
    {
        return m_TurnTimer.GetProgress();
    }

    @Override
    public void Reset()
    {
        super.Reset();
        m_TurnTimer.ResetTimer();
    }
}
