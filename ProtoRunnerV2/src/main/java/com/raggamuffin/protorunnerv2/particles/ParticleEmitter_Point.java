package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ParticleEmitter_Point extends ParticleEmitter
{
    private Vector3 m_ParticleForward;

    private double m_MaxDeltaEmission;
    private double m_DeltaEmission;

    private boolean m_On;

    public ParticleEmitter_Point(GameLogic game, Colour initialColour, Colour finalColour, double emissionForce, double lifeSpan)
    {
        super(game, initialColour, finalColour, emissionForce, lifeSpan, ParticleType.Standard);

        m_ParticleForward = new Vector3();

        m_MaxDeltaEmission = 0.04;
        m_DeltaEmission = 0.0;

        m_On = true;
    }

    public void Update(double deltaTime)
    {
        if(m_On)
        {
            m_DeltaEmission += deltaTime;

            while (m_DeltaEmission >= m_MaxDeltaEmission)
            {
                m_DeltaEmission -= m_MaxDeltaEmission;
                CreateParticle();
            }
        }
    }

    @Override
    public Vector3 CalculateParticleForward()
    {
        m_ParticleForward.X = MathsHelper.RandomDouble(-1,1);
        m_ParticleForward.Y = MathsHelper.RandomDouble(-1,1);
        m_ParticleForward.Z = MathsHelper.RandomDouble(-1,1);

        m_ParticleForward.Normalise();

        m_ParticleForward.RotateY(m_Forward.Yaw());

        return m_ParticleForward;
    }

    public void On()
    {
        m_On = true;
    }

    public void Off()
    {
        m_On = false;
    }
}
