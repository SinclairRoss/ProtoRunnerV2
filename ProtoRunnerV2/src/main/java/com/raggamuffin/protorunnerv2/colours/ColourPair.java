package com.raggamuffin.protorunnerv2.colours;

// Author: Sinclair Ross
// Date:   08/08/2016

import com.raggamuffin.protorunnerv2.utils.Colour;

public class ColourPair
{
    private Colour m_Primary;
    private Colour m_Secondary;

    public ColourPair(double[] primary, double[] secondary)
    {
        m_Primary = new Colour(primary);
        m_Secondary = new Colour(secondary);
    }

    public ColourPair(ColourPair pair)
    {
        m_Primary = new Colour(pair.GetPrimary());
        m_Secondary = new Colour(pair.GetSecondary());
    }

    public ColourPair()
    {
        m_Primary = new Colour();
        m_Secondary = new Colour();
    }

    public Colour GetPrimary()
    {
        return m_Primary;
    }

    public Colour GetSecondary()
    {
        return m_Secondary;
    }

    public void SetPrimaryColour(Colour primary)
    {
        m_Primary.SetColour(primary);
    }

    public void SetSecondaryColour(Colour secondary)
    {
        m_Secondary.SetColour(secondary);
    }
}
