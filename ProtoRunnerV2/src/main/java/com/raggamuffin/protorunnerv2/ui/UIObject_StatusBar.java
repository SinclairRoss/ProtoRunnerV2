package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   03/06/2017

import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.GameActivity;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class UIObject_StatusBar
{
    private static final double LABEL_OFFSET = 0.04;

    private static final double ALPHA_FOREGROUND = 0.6;
    private static final double ALPHA_BACKGROUND = 0.1;

    private static final double HEIGHT = 0.01;
    private static final double LENGTH = 1.8;

    private static final double FILL_RATE = 1.0;
    private static final double ARRIVAL_DISTANCE = 0.1;

    private static final double LOW_VALUE = 0.5;
    private static final double LOW_TRANSITION_LENGTH = 0.1;

    private static final double PULSE_RATE = 5.0;

    private UIElement_Block m_Background;
    private UIElement_Block m_Bar;
    private UIElement_Label m_Label;

    private final Colour m_HighColour;
    private final Colour m_LowColour;
    private Colour m_Colour;

    private double m_DisplayedValue;
    private double m_TargetValue;

    private double m_PulseValue;

    public UIObject_StatusBar(String title, double[] highColour, double[] lowColour, double x, double y, UIManager uiManager)
    {
        m_HighColour = new Colour(highColour);
        m_LowColour = new Colour(lowColour);
        m_Colour = new Colour(highColour);

        m_Background = new UIElement_Block(m_LowColour, Alignment.Center);
        m_Background.SetScale_UseRatio(LENGTH, HEIGHT);
        m_Background.SetPosition(x, y);
        m_Background.SetAlpha(ALPHA_BACKGROUND);
        uiManager.AddUIElement(m_Background);

        m_Bar = new UIElement_Block(m_LowColour, Alignment.Center);
        m_Bar.SetScale_UseRatio(LENGTH, HEIGHT);
        m_Bar.SetPosition(x, y);
        m_Bar.SetAlpha(ALPHA_FOREGROUND);
        uiManager.AddUIElement(m_Bar);

        if(!title.isEmpty())
        {
            m_Label = new UIElement_Label(title, 0.06, x, y + LABEL_OFFSET, Alignment.Center);
            m_Label.SetColour(lowColour);
            m_Label.SetAlpha(ALPHA_FOREGROUND);
            uiManager.AddUIElement(m_Label);
        }

        m_DisplayedValue = 0.0;
        m_TargetValue = 1.0;

        m_PulseValue = 0;
    }

    public void Update(double deltaTime)
    {
        UpdateDisplayedValue(deltaTime);
        UpdateSize();
        UpdateColour(deltaTime);
    }

    private void UpdateDisplayedValue(double deltaTime)
    {
        double deltaProgress = m_TargetValue - m_DisplayedValue;
        deltaProgress = MathsHelper.SignedNormalise(deltaProgress, -ARRIVAL_DISTANCE, ARRIVAL_DISTANCE);

        m_DisplayedValue += (deltaProgress * FILL_RATE * deltaTime);
    }

    private void UpdateSize()
    {
        double x =  m_DisplayedValue * LENGTH * GameActivity.SCREEN_RATIO;
        m_Bar.SetScale(x, HEIGHT);
    }

    private void UpdateColour(double deltaTime)
    {
        double lerpVal = MathsHelper.Normalise(m_DisplayedValue, LOW_VALUE - LOW_TRANSITION_LENGTH, LOW_VALUE);

        m_Colour.Red = MathsHelper.Lerp(lerpVal, m_LowColour.Red,   m_HighColour.Red);
        m_Colour.Green = MathsHelper.Lerp(lerpVal, m_LowColour.Green, m_HighColour.Green);
        m_Colour.Blue = MathsHelper.Lerp(lerpVal, m_LowColour.Blue,  m_HighColour.Blue);

        if(m_DisplayedValue <= LOW_VALUE)
        {
            double pulseRate = (1.0 - MathsHelper.Normalise(m_DisplayedValue, 0.0, LOW_VALUE)) * PULSE_RATE;
            m_PulseValue += deltaTime * pulseRate;
            m_PulseValue %= Math.PI / 2;
            m_Colour.Alpha = Math.cos(m_PulseValue);
        }
        else
        {
            m_Colour.Alpha = MathsHelper.Lerp(lerpVal, m_LowColour.Alpha,  m_HighColour.Alpha);
        }

        m_Bar.SetColour(m_Colour, ALPHA_FOREGROUND);
        m_Background.SetColour(m_Colour, ALPHA_BACKGROUND);
        m_Label.SetColour(m_Colour, ALPHA_FOREGROUND);
    }

    public void SetValue(double value) { m_TargetValue = value; }
}