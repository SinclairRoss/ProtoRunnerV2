package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer_Accumulation;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Particle
{
    private Timer_Accumulation m_LifeTimer;

    private final double m_DragCoefficient;
    private final double m_Mass;

    private Vector3 m_Position;
    private Vector3 m_Velocity;

    private final double m_FadeIn;
    private final double m_FadeOut;

    private Colour m_Colour;
    private Colour m_HotColour;
    private Colour m_ColdColour;

    public Particle()
    {
        m_LifeTimer = new Timer_Accumulation(5);

        m_DragCoefficient = 0.9;
        m_Mass = 1.0;

        m_Position = new Vector3();
        m_Velocity = new Vector3();

        m_FadeIn = 0.2;
        m_FadeOut = 0.7;

        m_Colour = new Colour();
        m_HotColour = new Colour();
        m_ColdColour = new Colour();
    }

    public void Update(double deltaTime)
    {
        m_LifeTimer.Update(deltaTime);

        ApplyDrag();
        UpdatePosition(deltaTime);

        UpdateColour();
        UpdateTransparency();
    }

    private void ApplyDrag()
    {
        m_Velocity.Scale(m_DragCoefficient);
    }

    private void UpdatePosition(double deltaTime)
    {
        m_Position.I += m_Velocity.I * deltaTime;
        m_Position.J += m_Velocity.J * deltaTime;
        m_Position.K += m_Velocity.K * deltaTime;
    }

    private void UpdateColour()
    {
        m_Colour.Red = MathsHelper.Lerp(0, m_HotColour.Red, m_ColdColour.Red);
        m_Colour.Green = MathsHelper.Lerp(0, m_HotColour.Green, m_ColdColour.Green);
        m_Colour.Blue = MathsHelper.Lerp(0, m_HotColour.Blue, m_ColdColour.Blue);
    }

    private void UpdateTransparency()
    {
        double normLifeSpan = m_LifeTimer.GetProgress();
        double alpha = 1.0;

        if (normLifeSpan <= m_FadeIn)
            alpha = MathsHelper.Normalise(normLifeSpan, 0, m_FadeIn);

        if (normLifeSpan >= m_FadeOut)
            alpha = 1.0 - MathsHelper.Normalise(normLifeSpan, m_FadeOut, 1.0);

        m_Colour.Alpha = alpha;
    }

    public void Activate(Vector3 pos, Vector3 vel, Vector3 forward, double emissionForce, Colour hot, Colour cold, double lifeSpan)
    {
        m_Position.SetVector(pos);
        m_Velocity.SetVector(vel);
        ApplyForce(forward, emissionForce);

        m_HotColour.SetColour(hot);
        m_ColdColour.SetColour(cold);

        m_LifeTimer.SetLimit(lifeSpan);
        m_LifeTimer.ResetTimer();
    }

    void ApplyForce(Vector3 direction, double force)
    {
        m_Velocity.I += (direction.I * force) / m_Mass;
        m_Velocity.J += (direction.J * force) / m_Mass;
        m_Velocity.K += (direction.K * force) / m_Mass;
    }

    public boolean IsValid()
    {
        return !m_LifeTimer.TimedOut();
    }

    public Vector3 GetPosition()
    {
        return m_Position;
    }

    public Colour GetColour()
    {
        return m_Colour;
    }
}
