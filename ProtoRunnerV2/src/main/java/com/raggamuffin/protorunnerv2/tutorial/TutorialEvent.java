package com.raggamuffin.protorunnerv2.tutorial;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;

public abstract class TutorialEvent
{
    protected GameLogic m_Game;

    public TutorialEvent(GameLogic game)
    {
        m_Game = game;
    }
    public abstract void On();
    public abstract void Off();
    public abstract void Update();
}
