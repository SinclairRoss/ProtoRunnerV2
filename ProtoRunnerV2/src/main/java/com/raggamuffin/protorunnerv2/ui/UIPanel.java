package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class UIPanel extends UIElement
{
    private double m_MaxAlpha;
    private double m_Counter;
    private double m_CounterMultiplier;

    public UIPanel(UIManager uiManager, Colour colour)
    {
        super(uiManager);

        m_Type = UIElementType.Panel;

        m_Colour = colour;
        m_MaxAlpha = 0.2;
        m_Counter = 0.0;
        m_CounterMultiplier = 1.0;

        m_Size.SetVector(SCREEN_RATIO ,2);
    }

    @Override
    public void Update(double deltaTime)
    {
        m_Counter += deltaTime * m_CounterMultiplier;
        m_Counter = MathsHelper.Clamp(m_Counter, 0, 1);
    }

    @Override
    protected void TriggerOpenAnimation(double delay)
    {
        m_Counter = 0.0;
        m_CounterMultiplier = 0.75;
    }

    @Override
    protected void TriggerClosingAnimation()
    {
        m_CounterMultiplier = -0.75;
    }

    public double GetAlpha()
    {
        return m_Counter * m_MaxAlpha;
    }
}
