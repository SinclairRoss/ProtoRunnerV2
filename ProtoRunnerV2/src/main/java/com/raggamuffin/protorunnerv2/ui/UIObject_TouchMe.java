package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   04/06/2017

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class UIObject_TouchMe
{
    private static final double PULSE_RATE_GROW = 6.0;
    private static final double PULSE_RATE_SHRINK = 2.0;
    private static final double PULSE_MAGNITUDE = 0.015;

    public static final double TRIANGLE_SCALE = 0.2;

    private GameAudioManager m_AudioManager;

    private UIElement_Triangle m_TouchTriangle;
    private UIElement_Triangle m_OnTouchBurst;

    private UITouchArea m_TouchArea;

    private double m_PulseCounter;

    private boolean m_AudioPlayed;

    public UIObject_TouchMe(UIManager uiManager, GameAudioManager audioManager)
    {
        m_AudioManager = audioManager;

        m_TouchTriangle = new UIElement_Triangle(30, uiManager);
        m_TouchTriangle.SetScale(0.2);
        m_TouchTriangle.SetColour(Colours.CalvinOrange);
        uiManager.AddUIElement(m_TouchTriangle);


        m_PulseCounter = 0;

        m_TouchArea = new UITouchArea(0.3, -0.3, -0.3, 0.3);

        m_AudioPlayed = false;
    }

    public void Update(double deltaTime)
    {
        double pulseRate = ((m_PulseCounter < Math.PI / 2) || (m_PulseCounter > Math.PI * 1.5)) ? PULSE_RATE_GROW : PULSE_RATE_SHRINK;
        m_PulseCounter += deltaTime * pulseRate;
        m_PulseCounter %= Math.PI * 2;

        double pulseAmount = Math.sin(m_PulseCounter);
        m_TouchTriangle.SetScale(TRIANGLE_SCALE + (pulseAmount * PULSE_MAGNITUDE));

        if(m_PulseCounter > Math.PI * 1.75)
        {
            if(!m_AudioPlayed)
            {
                m_AudioManager.PlaySound(AudioClips.TouchMe);
                m_AudioPlayed = true;
            }
        }
        else
        {
            m_AudioPlayed = false;
        }
    }

    public void OnPress()
    {

    }

    public void OnHover()
    {
        m_TouchTriangle.SetColour(Colours.RunnerBlue);
    }

    public void OnHoverOff()
    {
        m_TouchTriangle.SetColour(Colours.CalvinOrange);
    }

    public UITouchArea GetTouchArea()
    {
        return m_TouchArea;
    }
}
