package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   12/06/2017

import com.raggamuffin.protorunnerv2.utils.Timer;

public class UIAnimation_Grow extends UIAnimation
{
    private final double BURST_RATE_GROW = 0.75;

    private Timer m_LifeTimer;

    public UIAnimation_Grow(UIElement anchor, double fadeDuration)
    {
        super(anchor);

        m_LifeTimer = new Timer(fadeDuration);
    }

    @Override
    public void OnUpdate(double deltaTime)
    {
        UIElement anchor = GetAnchor();

        double scale = anchor.GetScale().X + (BURST_RATE_GROW * deltaTime);
        anchor.SetScale(scale);
    }

    @Override
    public void Start()
    {
        super.Start();
        m_LifeTimer.Start();
    }
}
