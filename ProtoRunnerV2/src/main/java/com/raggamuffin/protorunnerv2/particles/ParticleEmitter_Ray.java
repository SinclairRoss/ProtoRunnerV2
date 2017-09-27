package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ParticleEmitter_Ray extends ParticleEmitter
{
    private double m_Accuracy;
    private double m_Length;
    private double m_EmissionRate;
    private double m_EmissionTimer;

    private Vector3 m_SpawnPoint;
    private Vector3 m_ParticleForward;

    public ParticleEmitter_Ray(GameLogic game, Colour initialColour, Colour finalColour, double emissionForce, double emissionRate, double lifeSpan)
    {
        super(game, initialColour, finalColour, emissionForce, lifeSpan);

        m_Accuracy = Math.PI * 0.5;
        m_Length = 0.0;
        m_EmissionRate = emissionRate;
        m_EmissionTimer = 0.0;

        m_SpawnPoint = new Vector3();
        m_ParticleForward = new Vector3();
    }

    public void Update(double deltaTime)
    {
        m_EmissionTimer += deltaTime * m_Length;

        while(m_EmissionTimer >= m_EmissionRate)
        {
            m_EmissionTimer -= m_EmissionRate;
            CreateParticle();
        }
    }

    @Override
    public Vector3 CalculateSpawnPoint()
    {
        m_SpawnPoint.SetVector(m_Forward);
        m_SpawnPoint.Scale(MathsHelper.RandomDouble(0, m_Length));
        m_SpawnPoint.Add(m_Position);

        return m_SpawnPoint;
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

    public void SetLength(double length)
    {
        m_Length = length;
    }
}