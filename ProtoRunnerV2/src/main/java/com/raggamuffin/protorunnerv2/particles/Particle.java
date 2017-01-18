package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class Particle
{
    private ParticleType m_ParticleType;
    private Timer m_LifeTimer;

    private final double m_DragCoefficient;
    private final double m_Mass;

    protected Vector3 m_Position;
    protected Vector3 m_Velocity;

    private final double m_FadeIn;
    private final double m_FadeOut;

    protected Colour m_Colour;
    private Colour m_HotColour;
    private Colour m_ColdColour;

    public Particle(ParticleType particleType, double lifeSpan, double fadeIn, double fadeOut)
    {
        m_ParticleType = particleType;
        m_LifeTimer = new Timer(lifeSpan);

        m_DragCoefficient = 0.9;
        m_Mass = 1.0;

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

        ApplyDrag();
        UpdatePosition(deltaTime);

        UpdateColour();
        UpdateTransparency();
    }

    protected abstract void AdditionalBehaviour(double deltaTime);

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

        if (normLifeSpan >= m_FadeOut)
        {
            alpha = 1.0 - MathsHelper.Normalise(normLifeSpan, m_FadeOut, 1.0);
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

    public void ApplyForce(Vector3 direction, double force)
    {
        m_Velocity.I += (direction.I * force) / m_Mass;
        m_Velocity.J += (direction.J * force) / m_Mass;
        m_Velocity.K += (direction.K * force) / m_Mass;
    }

    public void ApplyForce(Vector3 force)
    {
        m_Velocity.I += force.I / m_Mass;
        m_Velocity.J += force.J / m_Mass;
        m_Velocity.K += force.K / m_Mass;
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

    public ParticleType GetType()
    {
        return m_ParticleType;
    }
}
