package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ParticleEmitter_Ripple extends ParticleEmitter
{
    private final int NUM_PARTICLES = 50;

    private Vector3 m_ParticleForward;

    private double m_MaxDeltaEmission;
    private double m_DeltaEmission;

    public ParticleEmitter_Ripple(GameLogic game, Colour initialColour, Colour finalColour, double emissionForce, double lifeSpan)
    {
        super(game, initialColour, finalColour, emissionForce, lifeSpan);

        m_ParticleForward = new Vector3();

        m_MaxDeltaEmission = 0.01;
        m_DeltaEmission = 0.0;
    }

    public void Update(double deltaTime)
    {}

    public void Burst()
    {
        for(int i = 0; i < NUM_PARTICLES; ++i)
        {
            CreateParticle();
        }
    }

    @Override
    public Vector3 CalculateParticleForward()
    {
        m_ParticleForward.SetVector(1, 0, 0);
        double rotation = MathsHelper.RandomDouble(Math.PI * 2);
        m_ParticleForward.RotateY(rotation);

        return m_ParticleForward;
    }
}