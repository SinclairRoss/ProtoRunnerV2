package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   07/06/2017

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;

import java.util.ArrayList;
import java.util.Arrays;

public class UIElementColourFader
{
    private ArrayList<UIElement> m_Elements;
    private Timer m_FadeTimer;

    private double[] m_Colour_Origin;
    private double[] m_Colour_Destination;

    public UIElementColourFader(double fadeDuration, double[] colour)
    {
        m_Elements = new ArrayList<>();

        m_FadeTimer = new Timer(fadeDuration);

        m_Colour_Origin = colour;
        m_Colour_Destination = Colours.White;
    }

    public void AddElement(UIElement element)
    {
        m_Elements.add(element);

        Colour colour = element.GetColour();
        colour.Red = m_Colour_Origin[0];
        colour.Green = m_Colour_Origin[1];
        colour.Blue = m_Colour_Origin[2];
    }

    public void AddElements(UIElement... elements)
    {
        int elementCount = elements.length;
        for(int i = 0; i < elementCount; ++i)
        {
            Colour colour = elements[i].GetColour();
            colour.Red = m_Colour_Origin[0];
            colour.Green = m_Colour_Origin[1];
            colour.Blue = m_Colour_Origin[2];
        }

        m_Elements.addAll(Arrays.asList(elements));
    }

    public void Update()
    {
        if(m_FadeTimer.IsActive())
        {
            if (!m_FadeTimer.HasElapsed())
            {
                int numElements = m_Elements.size();
                for (int i = 0; i < numElements; ++i)
                {
                    UIElement element = m_Elements.get(i);
                    Colour colour = element.GetColour();

                    double progress = m_FadeTimer.GetProgress();
                    colour.Red = MathsHelper.Lerp(progress, m_Colour_Origin[0], m_Colour_Destination[0]);
                    colour.Green = MathsHelper.Lerp(progress, m_Colour_Origin[1], m_Colour_Destination[1]);
                    colour.Blue = MathsHelper.Lerp(progress, m_Colour_Origin[2], m_Colour_Destination[2]);
                }
            }
            else
            {
                int numElements = m_Elements.size();
                for (int i = 0; i < numElements; ++i)
                {
                    UIElement element = m_Elements.get(i);
                    Colour colour = element.GetColour();

                    colour.Red = m_Colour_Destination[0];
                    colour.Green = m_Colour_Destination[1];
                    colour.Blue = m_Colour_Destination[2];
                }

                m_FadeTimer.Stop();
            }
        }
    }

    public void StartFade(double[] colour_Origin, double[] colour_Destination)
    {
        m_Colour_Origin = colour_Origin;
        m_Colour_Destination = colour_Destination;
        m_FadeTimer.Start();
    }
}