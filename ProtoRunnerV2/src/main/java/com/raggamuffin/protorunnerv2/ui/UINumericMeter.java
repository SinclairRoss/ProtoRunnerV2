package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   13/01/2017

import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class UINumericMeter
{
    private UILabel m_NumericMeter;
    private String m_Label;

    private double m_CurrentValue;
    private double m_TargetValue;

    private final double MIN_INCREMENT_SPEED = 500;

    public UINumericMeter(GameAudioManager audio, UIManager uiManager, Font.Alignment alignment, double fontSize, double xPos, double yPos, String label, Colour colour)
    {
        m_Label = label;

        m_NumericMeter = new UILabel(audio, uiManager);
        m_NumericMeter.SetText(m_Label + m_CurrentValue);
        m_NumericMeter.SetPosition(xPos, yPos);
        m_NumericMeter.SetFontSize(fontSize);
        m_NumericMeter.GetFont().SetAlignment(alignment);
        m_NumericMeter.GetFont().SetColour(colour);
        UpdateText();

        m_CurrentValue = 0;
        m_TargetValue = 0;
    }

    public void Update(double deltaTime)
    {
        if(m_CurrentValue < m_TargetValue)
        {
            double incrementSpeed = CalculateIncrementSpeed();
            m_CurrentValue += incrementSpeed * deltaTime;
            m_CurrentValue = MathsHelper.Clamp(m_CurrentValue, 0, m_TargetValue);

            UpdateText();
        }
    }

    public void SetValue(int value)
    {
        m_TargetValue = value;
    }

    private double CalculateIncrementSpeed()
    {
        double difference = m_TargetValue - m_CurrentValue;
        return MathsHelper.Clamp(difference, MIN_INCREMENT_SPEED, Double.MAX_VALUE);
    }

    private void UpdateText()
    {
        switch(m_NumericMeter.GetFont().GetAlignment())
        {
            case Left:
            case Center:
                m_NumericMeter.SetText(m_Label + ":" + (int)m_CurrentValue);
                break;
            case Right:
                m_NumericMeter.SetText((int)m_CurrentValue + ":" + m_Label);
                break;
        }
    }

    public UILabel GetLabel()
    {
        return m_NumericMeter;
    }
}
