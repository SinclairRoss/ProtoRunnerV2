package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ParticleEmitter_Spiral extends ParticleEmitter
{
    private Vector3 m_ParticleForward;

    private double m_MaxDeltaEmission;
    private double m_DeltaEmission;

    private double m_Theta;
    private double m_RotationRate;

    private boolean m_On;

    public ParticleEmitter_Spiral(GameLogic game, Colour initialColour, Colour finalColour, double emissionForce, double lifeSpan)
    {
        super(game, initialColour, finalColour, emissionForce, lifeSpan);

        m_ParticleForward = new Vector3();

        m_MaxDeltaEmission = 0.01;
        m_DeltaEmission = 0.0;

        m_Theta = MathsHelper.RandomDouble(0, Math.PI * 2);
        m_RotationRate = 15.0;

        m_On = false;
    }

    public void Update(double deltaTime)
    {
        if(!m_On)
            return;

        m_Theta = MathsHelper.Wrap(m_Theta + (m_RotationRate * deltaTime), 0, Math.PI * 2);

        m_DeltaEmission += deltaTime;

        while(m_DeltaEmission >= m_MaxDeltaEmission)
        {
            m_DeltaEmission -= m_MaxDeltaEmission;
            m_ParticleManager.CreateParticle(this);
        }
    }

    @Override
    public Vector3 CalculateParticleForward()
    {
        m_ParticleForward.SetVector(0,1,0);
        m_ParticleForward.RotateZ(m_Theta);
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
