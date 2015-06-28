package com.raggamuffin.protorunnerv2.tutorial;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;

public abstract class TutorialCondition
{
    protected GameLogic m_Game;
    private String m_Message;

    public TutorialCondition(GameLogic game, String message)
    {
        m_Game = game;
        m_Message = message;
    }

    public abstract void Update(double deltaTime);
    public abstract boolean ConditionComplete();
    public abstract double GetProgress();
    public abstract void Reset();


    public String GetMessage()
    {
        return m_Message;
    }
}
