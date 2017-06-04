package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ParticleEmitter_HyperLight extends ParticleEmitter
{
    private Vector3 m_ParticleForward;

    private double m_MaxDeltaEmission;
    private double m_DeltaEmission;

    private boolean m_On;

    public ParticleEmitter_HyperLight(GameLogic game, Colour initialColour, Colour finalColour, double emissionForce, double lifeSpan)
    {
        super(game, initialColour, finalColour, emissionForce, lifeSpan, ParticleType.Standard);

        m_ParticleForward = new Vector3();

        m_MaxDeltaEmission = 0.01;
        m_DeltaEmission = 0.0;

        m_On = false;
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
        Vector3 up = m_Forward.GetMajorComponent() != Vector3.Component.Y ? Vector3.UP : Vector3.FORWARD;

        m_ParticleForward.SetVectorAsCrossProduct(m_Forward, up);
        double rotation = MathsHelper.RandomDouble(Math.PI * 2);
        m_ParticleForward.RotateAboutAxis(m_Forward, rotation);

        return m_ParticleForward;
    }

    public void TurnOn()
    {
        m_On = true;
    }

    public void TurnOff()
    {
        m_On = false;
    }
}
