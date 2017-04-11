package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   10/04/2017

import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Spring1;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.HexPlosive;

public class HexPlosiveRing extends GameObject
{
    private HexPlosive m_Anchor;

    private Spring1 m_Spring;
    private double m_RestingHeight;
    private double m_HighHeight;

    private Colour m_Colour_On;
    private Colour m_Colour_Off;

    private boolean m_IsValid;

    public HexPlosiveRing(HexPlosive anchor, double radius, double restingHeight, double[] colourOn, double[] colourOff)
    {
        super(ModelType.Ring, 0);

        m_Anchor = anchor;

        SetScale(radius);

        m_Spring = new Spring1(25);

        m_HighHeight = restingHeight;
        m_RestingHeight = 0;

        m_Colour_On = new Colour(colourOn);
        m_Colour_On.Alpha = 0.6;

        m_Colour_Off = new Colour(colourOff);
        m_Colour_Off.Alpha = 0.6;

        SetColour(m_Colour_Off);

        m_IsValid = true;
    }

    @Override
    public boolean IsValid()
    {
        return m_IsValid;
    }

    @Override
    public void Update(double deltaTime)
    {
        double force = m_Spring.CalculateSpringForce(GetPosition().Y, m_RestingHeight);
        ApplyForce(Vector3.UP, force, deltaTime);

        super.Update(deltaTime);
    }

    public void TurnOn()
    {
        SetColour(m_Colour_On);
    }

    public void Rise()
    {
        m_RestingHeight = m_HighHeight;
    }
    public void Invalidate() { m_IsValid = false; }
}
