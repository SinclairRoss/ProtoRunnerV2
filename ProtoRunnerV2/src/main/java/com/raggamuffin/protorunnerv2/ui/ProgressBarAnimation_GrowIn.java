package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class ProgressBarAnimation_GrowIn extends ProgressBarAnimation
{
    private double m_Scale;
    private final double SCALE_RATE = 2.0;

    public ProgressBarAnimation_GrowIn(UIProgressBar bar)
    {
        super(bar);

        m_Scale = 0.0;
    }

    @Override
    protected void Initialise()
    {
        m_Scale = 0.0;
        m_Bar.SetBackBarScale(m_Scale);
    }

    @Override
    protected void Run(double deltaTime)
    {
        m_Scale += SCALE_RATE * deltaTime;

        m_Scale = MathsHelper.Clamp(m_Scale, 0, 1);

        m_Bar.SetBackBarScale(m_Scale);

        if(m_Scale >= 1.0)
            AnimationComplete();
    }

    @Override
    protected void Close()
    {
        m_Scale = 1.0;
        m_Bar.SetBackBarScale(m_Scale);
    }
}
