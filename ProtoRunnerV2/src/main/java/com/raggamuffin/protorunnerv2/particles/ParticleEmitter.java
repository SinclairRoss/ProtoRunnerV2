package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class ParticleEmitter
{
    protected final Vector3 m_Position;
    protected final Vector3 m_Forward;
    private final Vector3 m_Velocity;
    private double m_EmissionForce;
    private double m_LifeSpan;
    private double m_ParticleSize;

    private Colour m_InitialColour;
    private Colour m_FinalColour;

    protected final ParticleManager m_ParticleManager;

    public ParticleEmitter(GameLogic game, Colour initialColour, Colour finalColour, double emissionForce, double lifeSpan)
    {
        m_Position      = new Vector3();
        m_Forward       = new Vector3();
        m_Velocity      = new Vector3();
        m_EmissionForce = emissionForce;
        m_LifeSpan      = lifeSpan;
        m_ParticleSize  = 30.0;

        m_InitialColour = initialColour;
        m_FinalColour   = finalColour;

        m_ParticleManager = game.GetParticleManager();
    }

    public abstract Vector3 CalculateParticleForward();

    public Vector3 CalculateSpawnPoint()
    {
        return m_Position;
    }

    // Getters.

    public Vector3 GetVelocity()
    {
        return m_Velocity;
    }

    public Colour GetInitialColour()
    {
        return m_InitialColour;
    }

    public Colour GetFinalColour()
    {
        return m_FinalColour;
    }

    public double GetEmissionForce()
    {
        return m_EmissionForce;
    }

    public double GetLifeSpan()
    {
        return m_LifeSpan;
    }

    public double GetParticleSize()
    {
        return m_ParticleSize;
    }

    // Setters.

    public void SetPosition(Vector3 position)
    {
        m_Position.SetVector(position);
    }

    public void SetForward(Vector3 forward)
    {
        m_Forward.SetVector(forward);
    }

    public void SetForward(double i, double j, double k)
    {
        m_Position.SetVector(i, j, k);
    }

    public void SetVelocity(Vector3 velocity)
    {
        m_Velocity.SetVector(velocity);
    }

    public void SetInitialColour(Colour colour)
    {
        m_InitialColour = colour;
    }

    public void SetFinalColour(Colour colour)
    {
        m_FinalColour = colour;
    }

    public void SetParticleSize(double size)
    {
        m_ParticleSize = size;
    }
}
