package com.raggamuffin.protorunnerv2.managers;

// Author: Sinclair Ross
// Date:   04/06/2017

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.ui.UIScreens;

public class GameManager_Quiet extends GameManager
{
    public GameManager_Quiet(GameLogic game)
    {
        super(game);
    }

    @Override
    public void Update(double deltaTime)
    {}

    @Override
    public void Initialise()
    {
        m_Game.GetUIManager().ShowScreen(UIScreens.LearnToTouch);
    }

    @Override
    public void CleanUp()
    {}
}
