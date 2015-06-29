package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;

public abstract class GameManager
{
    protected GameLogic m_Game;

    public GameManager(GameLogic game)
    {
        m_Game = game;
    }

    public abstract void Update(double deltaTime);

    public abstract void Initialise();
    public abstract void CleanUp();
}
