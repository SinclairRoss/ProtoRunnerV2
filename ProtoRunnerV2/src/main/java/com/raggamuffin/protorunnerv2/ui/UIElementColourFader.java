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

    private Colour m_Colour_Origin;
    private Colour m_Colour_Destination;

    public UIElementColourFader(double fadeDuration, double[] colour)
    {
        m_Elements = new ArrayList<>();

        m_FadeTimer = new Timer(fadeDuration);

        m_Colour_Origin = new Colour(colour);
        m_Colour_Destination = new Colour(Colours.White);
    }

    public void AddElement(UIElement element)
    {
        m_Elements.add(element);

        Colour colour = element.GetColour();
        colour.Red = m_Colour_Origin.Red;
        colour.Green = m_Colour_Origin.Green;
        colour.Blue = m_Colour_Origin.Blue;
    }

    public void AddElements(UIElement... elements)
    {
        int elementCount = elements.length;
        for(int i = 0; i < elementCount; ++i)
        {
            Colour colour = elements[i].GetColour();
            colour.Red = m_Colour_Origin.Red;
            colour.Green = m_Colour_Origin.Green;
            colour.Blue  = m_Colour_Origin.Blue;
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
                    colour.Lerp(progress, m_Colour_Origin, m_Colour_Destination);
                }
            }
            else
            {
                int numElements = m_Elements.size();
                for (int i = 0; i < numElements; ++i)
                {
                    UIElement element = m_Elements.get(i);
                    Colour colour = element.GetColour();

                    colour.Red = m_Colour_Destination.Red;
                    colour.Green = m_Colour_Destination.Green;
                    colour.Blue  = m_Colour_Destination.Blue;
                }

                m_FadeTimer.Stop();
            }
        }
    }

    public void StartFade(double[] colour_Origin, double[] colour_Destination)
    {
        m_Colour_Origin.SetColour(colour_Origin);
        m_Colour_Destination.SetColour(colour_Destination);
        m_FadeTimer.Start();
    }
}