package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class Particle
{
    private Timer m_LifeTimer;

    private final double m_DragCoefficient;

    protected Vector3 m_Position;
    protected Vector3 m_Velocity;

    private final double m_FadeIn;
    private final double m_FadeOut;

    protected Colour m_Colour;
    private Colour m_HotColour;
    private Colour m_ColdColour;

    public Particle(double lifeSpan, double fadeIn, double fadeOut)
    {
        m_LifeTimer = new Timer(lifeSpan);

        m_DragCoefficient = 5;

        m_Position = new Vector3();
        m_Velocity = new Vector3();

        m_FadeIn = fadeIn;
        m_FadeOut = fadeOut;

        m_Colour = new Colour();
        m_HotColour = new Colour();
        m_ColdColour = new Colour();
    }

    public void Update(double deltaTime)
    {
        AdditionalBehaviour(deltaTime);

        ApplyDrag(deltaTime);
        UpdatePosition(deltaTime);

        UpdateColour();
        UpdateTransparency();
    }

    protected abstract void AdditionalBehaviour(double deltaTime);

    private void ApplyDrag(double deltaTime)
    {
        double drag_x = (m_DragCoefficient * m_Velocity.X);
        double drag_y = (m_DragCoefficient * m_Velocity.Y);
        double drag_z = (m_DragCoefficient * m_Velocity.Z);

        ApplyForce(-drag_x, -drag_y, -drag_z, deltaTime);
    }

    private void UpdatePosition(double deltaTime)
    {
        m_Position.X += m_Velocity.X * deltaTime;
        m_Position.Y += m_Velocity.Y * deltaTime;
        m_Position.Z += m_Velocity.Z * deltaTime;
    }

    private void UpdateColour()
    {
        double normLifeSpan = m_LifeTimer.GetProgress();

        m_Colour.Red = MathsHelper.Lerp(normLifeSpan, m_HotColour.Red, m_ColdColour.Red);
        m_Colour.Green = MathsHelper.Lerp(normLifeSpan, m_HotColour.Green, m_ColdColour.Green);
        m_Colour.Blue = MathsHelper.Lerp(normLifeSpan, m_HotColour.Blue, m_ColdColour.Blue);
    }

    private void UpdateTransparency()
    {
        double normLifeSpan = m_LifeTimer.GetProgress();
        double alpha = 1.0;

        if (normLifeSpan <= m_FadeIn)
        {
            alpha = MathsHelper.Normalise(normLifeSpan, 0, m_FadeIn);
        }
        else
        {
            if (normLifeSpan >= m_FadeOut)
            {
                alpha = 1.0 - MathsHelper.Normalise(normLifeSpan, m_FadeOut, 1.0);
            }
        }

        m_Colour.Alpha = alpha;
    }

    public void Activate(Vector3 pos, Vector3 vel, Vector3 forward, double emissionForce, Colour hot, Colour cold, double lifeSpan)
    {
        m_Position.SetVector(pos);
        m_Velocity.SetVector(vel);
        ApplyForce(forward, emissionForce);

        m_HotColour.SetColour(hot);
        m_ColdColour.SetColour(cold);

        m_LifeTimer.SetDuration(lifeSpan);
        m_LifeTimer.Start();
    }

    // <----- Forces -----> \\
    public void ApplyForce(Vector3 force) { ApplyForce(force.X, force.Y, force.Z, 1); }
    public void ApplyForce(Vector3 force, double deltaTime) { ApplyForce(force.X, force.Y, force.Z, deltaTime); }
    public void ApplyForce(double force_x, double force_y, double force_z, double deltaTime)
    {
        m_Velocity.X += force_x * deltaTime;
        m_Velocity.Y += force_y * deltaTime;
        m_Velocity.Z += force_z * deltaTime;
    }

    public void ForceInvalidation()
    {
        m_LifeTimer.ElapseTimer();
    }

    public boolean IsValid()
    {
        return !m_LifeTimer.HasElapsed();
    }

    public abstract void OnInvalidation();

    public Vector3 GetPosition()
    {
        return m_Position;
    }

    public Colour GetColour()
    {
        return m_Colour;
    }
}
