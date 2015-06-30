package com.raggamuffin.protorunnerv2.tutorial;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.ui.TutorialScreen;

public class TutorialEvent_HealthBar extends TutorialEvent
{
    private TutorialScreen m_Screen;

    public TutorialEvent_HealthBar(GameLogic game, TutorialScreen screen)
    {
        super(game);

        m_Screen = screen;
    }

    @Override
    public void On()
    {
        m_Screen.ShowHealthBar();
    }

    @Override
    public void Off()
    {
        m_Screen.HideHealthBar();
    }

    @Override
    public void Update()
    {

    }
}
