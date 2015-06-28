package com.raggamuffin.protorunnerv2.tutorial;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.utils.Timer;

public class TutorialCondition_Time extends TutorialCondition
{
    private Timer m_Timer;

    public TutorialCondition_Time(GameLogic game, String message, double time)
    {
        super(game, message);

        m_Timer = new Timer(time);
    }

    @Override
    public void Update(double deltaTime)
    {
        m_Timer.Update(deltaTime);
    }

    @Override
    public boolean ConditionComplete()
    {
        return m_Timer.TimedOut();
    }

    @Override
    public double GetProgress()
    {
        return m_Timer.GetProgress();
    }
}
