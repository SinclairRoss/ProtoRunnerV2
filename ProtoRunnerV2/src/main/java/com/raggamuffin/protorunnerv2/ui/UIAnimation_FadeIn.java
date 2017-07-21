package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   15/06/2017

import com.raggamuffin.protorunnerv2.utils.Colours;

public class UIAnimation_FadeIn extends UIAnimation
{
    private static final double FADE_DURATION = 0.75;

    private UIElementColourFader m_ColourFader;
    private UIElementAlphaFader m_AlphaFader;

    public UIAnimation_FadeIn(UIElement... anchors)
    {
        super(null);

        m_ColourFader = new UIElementColourFader(FADE_DURATION, UIConstants.COLOUR_OFF);
        m_ColourFader.AddElements(anchors);

        m_AlphaFader = new UIElementAlphaFader(FADE_DURATION, 0);
        m_AlphaFader.AddElements(anchors);
    }

    @Override
    public void OnUpdate(double deltaTime)
    {
        m_ColourFader.Update();
        m_AlphaFader.Update();
    }

    @Override
    public void Start()
    {
        super.Start();

        m_ColourFader.StartFade(UIConstants.COLOUR_OFF, Colours.White);
        m_AlphaFader.StartFade(0, 1);
    }
}