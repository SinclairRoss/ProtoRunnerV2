package com.raggamuffin.protorunnerv2.tutorial;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.Vehicles.Runner;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.utils.Timer;

public class TutorialCondition_TurnAmount extends TutorialCondition
{
    private VehicleManager m_VehicleManager;
    private Timer m_TurnTimer;

    public TutorialCondition_TurnAmount(GameLogic game, String message, double amount, TutorialEffect... effects)
    {
        super(game, message, OptionalElement.ProgressBar, effects);

        m_TurnTimer = new Timer(amount);
        m_VehicleManager = m_Game.GetVehicleManager();
    }

    @Override
    public void Update(double deltaTime)
    {
        Runner player = m_VehicleManager.GetPlayer();

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
