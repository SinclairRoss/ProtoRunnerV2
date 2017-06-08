package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   04/06/2017

import android.util.Log;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Vector2;

import java.util.ArrayList;

public class UIObject_TouchMe
{
    private static final double PULSE_RATE_GROW = 6.0;
    private static final double PULSE_RATE_SHRINK = 2.0;
    private static final double PULSE_MAGNITUDE = 0.015;
    private static final double SCALE = 0.2;

    public static final double TRIANGLE_SCALE = 0.2;

    private GameAudioManager m_AudioManager;

    private UIElement_Triangle m_TouchTriangle;
    private ArrayList<UIObject_ChevronRow> m_ChevronRows;
    private UITouchArea m_TouchArea;

    private double m_PulseCounter;
    private boolean m_AudioPlayed;

    private final double[] m_Colour_Idle;
    private final double[] m_Colour_Hover;

    private UIElementColourFader m_ColourFader;

    private boolean m_HoverOn;

    public UIObject_TouchMe(UIManager uiManager, GameAudioManager audioManager)
    {
        m_AudioManager = audioManager;

        m_Colour_Idle = Colours.CalvinOrange;
        m_Colour_Hover = Colours.RunnerBlue;

        m_ColourFader = new UIElementColourFader(0.25, m_Colour_Idle);

        m_TouchTriangle = new UIElement_Triangle(30, uiManager);
        m_TouchTriangle.SetScale(SCALE);
        m_TouchTriangle.SetColour(m_Colour_Idle);
        uiManager.AddUIElement(m_TouchTriangle);

        m_ColourFader.AddElement(m_TouchTriangle);

        m_ChevronRows = new ArrayList<>();
        Vector2 pos = new Vector2(0, -0.5);
        double rotation = 0;

        for(int i = 0; i < 3; ++i)
        {
            UIObject_ChevronRow chevronRow = new UIObject_ChevronRow(pos, rotation, SCALE / 2, uiManager);
            chevronRow.SetColour(m_Colour_Idle);

            double deltaRotation = (Math.PI * 2) / 3;
            rotation += deltaRotation;
            pos.Rotate(deltaRotation);

            ArrayList<UIElement_Chevron> chevrons = chevronRow.GetChevrons();
            int numChevrons = chevrons.size();
            for(int j = 0; j < numChevrons; ++j)
            {
                m_ColourFader.AddElement(chevrons.get(j));
            }

            m_ChevronRows.add(chevronRow);
        }

        m_PulseCounter = 0;

        m_TouchArea = new UITouchArea(0.3, -0.3, -0.3, 0.3);

        m_AudioPlayed = false;
        m_HoverOn = false;
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

                int numRows = m_ChevronRows.size();
                for(int i = 0; i < numRows; ++i)
                {
                    m_ChevronRows.get(i).ClearChevrons();
                }
            }
        }
        else
        {
            m_AudioPlayed = false;
        }


        int numRows = m_ChevronRows.size();
        for(int i = 0; i < numRows; ++i)
        {
            m_ChevronRows.get(i).Update();
        }

        m_ColourFader.Update();
    }

    public void OnPress()
    {}

    public void OnHover()
    {
        if(!m_HoverOn)
        {
            m_ColourFader.StartFade(m_Colour_Idle, m_Colour_Hover);
            m_HoverOn = true;

            Log.e("touch me", "Hover On");
        }
    }

    public void OnHoverOff()
    {
        if(m_HoverOn)
        {
            m_ColourFader.StartFade(m_Colour_Hover, m_Colour_Idle);
            m_HoverOn = false;

            Log.e("touch me", "Hover Off");
        }
    }

    public UITouchArea GetTouchArea()
    {
        return m_TouchArea;
    }
}
