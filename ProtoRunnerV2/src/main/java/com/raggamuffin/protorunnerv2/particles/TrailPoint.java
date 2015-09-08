package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class TrailPoint
{
    private TrailPoint m_Child;
    private final Vector3 m_Position;
    private double m_LifeSpan;

    private Colour m_Colour;
    private Colour m_HotColour;
    private Colour m_ColdColour;

    public TrailPoint()
    {
        m_Colour = new Colour(Colours.VioletRed);
        m_HotColour = new Colour(Colours.VioletRed);
        m_ColdColour = new Colour(Colours.PastelBlueDark);

        m_Position = new Vector3();
        m_LifeSpan = 0.0;
        m_Child = null;
    }

    public void Activate(Vector3 position, double lifeSpan, TrailPoint child, Colour hotColour, Colour coldColour)
    {
        m_Position.SetVector(position);
        m_LifeSpan = lifeSpan;
        m_Child = child;

        m_Colour.SetColour(hotColour);
        m_HotColour.SetColour(hotColour);
        m_ColdColour.SetColour(coldColour);
    }

    public void Update(double deltaTime)
    {
        m_LifeSpan -= deltaTime;

        HandleChild(deltaTime);
    }

    private void HandleChild(double deltaTime)
    {
        if(m_Child != null)
            m_Child.Update(deltaTime);
        else
            InvalidateChildren();
    }

    public Vector3 GetPosition()
    {
        return m_Position;
    }

    public void InvalidateChildren()
    {
        if(m_Child == null)
            return;

        m_Child.InvalidateChildren();
        m_Child = null;
    }

    public TrailPoint GetChild()
    {
        return m_Child;
    }

    public boolean IsValid()
    {
        return m_LifeSpan > 0;
    }
}
