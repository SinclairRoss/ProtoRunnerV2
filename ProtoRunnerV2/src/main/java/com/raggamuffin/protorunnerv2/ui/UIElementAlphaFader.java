package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   07/06/2017

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;

import java.util.ArrayList;

public class UIElementAlphaFader
{
    private ArrayList<UIElement> m_Elements;
    private Timer m_FadeTimer;

    private double m_Alpha_Origin;
    private double m_Alpha_Destination;

    public UIElementAlphaFader(double fadeDuration)
    {
        m_Elements = new ArrayList<>();

        m_FadeTimer = new Timer(fadeDuration);
        m_FadeTimer.StartElapsed();

        m_Alpha_Origin = 1.0;
        m_Alpha_Destination = 0.0;
    }

    public void AddElement(UIElement element)
    {
        m_Elements.add(element);
    }

    public void Update()
    {
        boolean hasTimerElapsed = m_FadeTimer.HasElapsed();

        int numElements = m_Elements.size();
        for (int i = 0; i < numElements; ++i)
        {
            UIElement element = m_Elements.get(i);
            Colour colour = element.GetColour();

            if(!hasTimerElapsed)
            {
                double progress = m_FadeTimer.GetProgress();
                colour.Alpha = MathsHelper.Lerp(progress, m_Alpha_Origin, m_Alpha_Destination);
            }
            else
            {
                colour.Alpha = m_Alpha_Destination;
            }
        }
    }

    public void StartFade(double alpha_Origin, double alpha_Destination)
    {
        m_Alpha_Origin = alpha_Origin;
        m_Alpha_Destination = alpha_Destination;
        m_FadeTimer.Start();
    }
}