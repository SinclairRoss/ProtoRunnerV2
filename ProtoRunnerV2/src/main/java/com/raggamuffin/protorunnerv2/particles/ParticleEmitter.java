package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class ParticleEmitter
{
    protected final Vector3 m_Position;
    protected final Vector3 m_Forward;
    protected final Vector3 m_Velocity;
    private double m_EmissionForce;
    private double m_LifeSpan;

    private Colour m_InitialColour;
    private Colour m_FinalColour;

    protected final ParticleManager m_ParticleManager;

    ParticleType m_ParticleType;

    public ParticleEmitter(GameLogic game, Colour initialColour, Colour finalColour, double emissionForce, double lifeSpan, ParticleType particleType)
    {
        m_Position      = new Vector3();
        m_Forward       = new Vector3();
        m_Velocity      = new Vector3();
        m_EmissionForce = emissionForce;
        m_LifeSpan      = lifeSpan;

        m_InitialColour = initialColour;
        m_FinalColour   = finalColour;

        m_ParticleType = particleType;

        m_ParticleManager = game.GetParticleManager();
    }

    public abstract Vector3 CalculateParticleForward();

    public Vector3 CalculateSpawnPoint()
    {
        return m_Position;
    }


    protected void CreateParticle()
    {
        switch(m_ParticleType)
        {
            case Standard:
                m_ParticleManager.CreateParticle(this);
                break;
            case Multiplier:
                m_ParticleManager.CreateParticleMultiplier(this);
                break;
        }
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

    public double GetEmissionForce() { return m_EmissionForce; }
    public void SetEmissionForce(double force) { m_EmissionForce = force; }

    public double GetLifeSpan()
    {
        return m_LifeSpan;
    }

    public void SetPosition(Vector3 position) { m_Position.SetVector(position); }
    public void SetPosition(double x, double y, double z) { m_Position.SetVector(x, y, z);}
    public Vector3 GetPosition() { return m_Position; }

    public void SetForward(Vector3 forward) { m_Forward.SetVector(forward); }
    public Vector3 GetForward() { return m_Forward; }

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
}
