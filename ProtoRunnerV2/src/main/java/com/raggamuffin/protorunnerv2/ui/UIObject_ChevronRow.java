package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   07/06/2017

import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector2;

import java.util.ArrayList;

public class UIObject_ChevronRow
{
    private static final int NUM_CHEVRONS = 3;
    private static final double SPACING = 1;

    private ArrayList<UIElement_Chevron> m_Chevrons;
    private Timer m_TickTimer;

    private int m_DisplayIndex;

    private ArrayList<UIElementAlphaFader> m_AlphaFaders;

    public UIObject_ChevronRow(Vector2 position, double rotation, double scale, UIManager uiManager)
    {
        m_AlphaFaders = new ArrayList<>(NUM_CHEVRONS);
        m_Chevrons = new ArrayList<>(NUM_CHEVRONS);

        for(int i = 0; i < NUM_CHEVRONS; ++i)
        {
            UIElement_Chevron chevron = new UIElement_Chevron(uiManager);

            chevron.SetScale(scale);

            chevron.SetPosition(0, i * SPACING * scale);
            chevron.GetPosition().Rotate(rotation);
            chevron.GetPosition().Add(position);

            chevron.Rotate(-rotation);

            m_Chevrons.add(chevron);
            uiManager.AddUIElement(chevron);

            UIElementAlphaFader alphaFader = new UIElementAlphaFader(0.4);
            alphaFader.AddElement(chevron);
            m_AlphaFaders.add(alphaFader);
        }

        m_DisplayIndex = 0;

        m_TickTimer = new Timer(0.5);
        m_TickTimer.Start();
    }

    public void Update()
    {
        if(m_TickTimer.HasElapsed())
        {
            if(m_DisplayIndex < NUM_CHEVRONS)
            {
                m_AlphaFaders.get(m_DisplayIndex).StartFade(0.0, 1.0);
                ++m_DisplayIndex;

                if(m_DisplayIndex < NUM_CHEVRONS)
                {
                    m_TickTimer.Start();
                }
                else
                {
                    m_TickTimer.Stop();
                }
            }
        }

        for(int i = 0; i < NUM_CHEVRONS; ++i)
        {
            m_AlphaFaders.get(i).Update();
        }
    }

    public void SetColour(double[] colour)
    {
        for(int i = 0; i < NUM_CHEVRONS; ++i)
        {
            m_Chevrons.get(i).SetColour(colour);
        }
    }

    public void ClearChevrons()
    {
        m_DisplayIndex = 0;
        for(int i = 0; i < NUM_CHEVRONS; ++i)
        {
            m_AlphaFaders.get(i).StartFade(1, 0);
        }

        m_TickTimer.Start();
    }

    public ArrayList<UIElement_Chevron> GetChevrons()
    {
        return m_Chevrons;
    }
}
