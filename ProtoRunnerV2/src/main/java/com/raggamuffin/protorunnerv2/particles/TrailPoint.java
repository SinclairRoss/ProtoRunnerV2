package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class TrailPoint
{
    private TrailPoint m_Parent;
    private TrailPoint m_Child;
    private final Vector3 m_Position;
    private Timer m_LifeTimer;

    private Colour m_Colour;
    private Colour m_HotColour;
    private Colour m_ColdColour;

    private final double m_FadeInLength;

    public TrailPoint()
    {
        m_Parent = null;
        m_Child = null;
        m_Position = new Vector3();
        m_LifeTimer = new Timer(0);

        m_Colour = new Colour(Colours.VioletRed);
        m_HotColour = new Colour(Colours.VioletRed);
        m_ColdColour = new Colour(Colours.PastelBlueDark);

        m_FadeInLength = 0.8;
    }

    public void Activate(Vector3 position, double lifeSpan, TrailPoint parent, Colour hotColour, Colour coldColour)
    {
        m_Position.SetVector(position);
        m_LifeTimer.SetLimit(lifeSpan);
        m_LifeTimer.ResetTimer();
        m_Parent = parent;
        m_Child =  null;

        m_Colour.SetColour(hotColour);
        m_HotColour.SetColour(hotColour);
        m_ColdColour.SetColour(coldColour);
    }

    public void SetChild(TrailPoint child)
    {
        m_Child = child;
    }

    public void Update(double deltaTime)
    {
        m_LifeTimer.Update(deltaTime);

        double lerp = m_LifeTimer.GetProgress();
        m_Colour.Red = MathsHelper.Lerp(lerp, m_HotColour.Red, m_ColdColour.Red);
        m_Colour.Green = MathsHelper.Lerp(lerp, m_HotColour.Green, m_ColdColour.Green);
        m_Colour.Blue = MathsHelper.Lerp(lerp, m_HotColour.Blue, m_ColdColour.Blue);
        m_Colour.Alpha = CalculateAlpha();
    }

    private double CalculateAlpha()
    {
        double val = m_LifeTimer.GetInverseProgress();

        return val < m_FadeInLength ? MathsHelper.Normalise(val, 0, m_FadeInLength):
                                      MathsHelper.Normalise(val, 1.0, 0.0);
    }

    public void CleanUp()
    {
        if(m_Child != null)
            m_Child.InvalidateParent();
    }

    public void InvalidateParent()
    {
        m_Parent = null;
    }

    public Vector3 GetPosition()
    {
        return m_Position;
    }

    public Colour GetColour()
    {
        return m_Colour;
    }

    public TrailPoint GetParent()
    {
        return m_Parent;
    }

    public boolean IsValid()
    {
        return m_Child != null ? !m_Child.HasTimedOut() : true;
    }

    public boolean HasTimedOut()
    {
        return m_LifeTimer.TimedOut();
    }
}
