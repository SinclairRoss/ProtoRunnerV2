package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ParticleEmitter_Ray extends ParticleEmitter
{
    private double m_Accuracy;
    private double m_Range;
    private double m_MaxDeltaEmission;
    private double m_DeltaEmission;

    private Vector3 m_SpawnPoint;
    private Vector3 m_ParticleForward;

    public ParticleEmitter_Ray(GameLogic game, Colour initialColour, Colour finalColour, double emissionForce, double lifeSpan)
    {
        super(game, initialColour, finalColour, emissionForce, lifeSpan);

        m_Accuracy = Math.PI * 0.5;
        m_Range = 0.0;
        m_MaxDeltaEmission = 0.1;
        m_DeltaEmission = 0.0;

        m_SpawnPoint = new Vector3();
        m_ParticleForward = new Vector3();
    }

    public void Update(double deltaTime)
    {
        m_DeltaEmission += deltaTime;

        while(m_DeltaEmission >= m_MaxDeltaEmission)
        {
            m_DeltaEmission -= m_MaxDeltaEmission;
            m_ParticleManager.CreateParticle(this);
        }
    }

    @Override
    public Vector3 CalculateSpawnPoint()
    {
        m_SpawnPoint.SetVector(m_Forward);
        m_SpawnPoint.Scale(MathsHelper.RandomDouble(0, m_Range));
        m_SpawnPoint.Add(m_Position);

        return m_SpawnPoint;
    }

    @Override
    public Vector3 CalculateParticleForward()
    {
        m_ParticleForward.SetVector(0,1,0);
        m_ParticleForward.RotateZ(MathsHelper.RandomDouble(-m_Accuracy, m_Accuracy));
        m_ParticleForward.RotateY(m_Forward.Yaw());
        return m_ParticleForward;
    }

    public void SetRange(double range)
    {
        m_Range = range;
    }
}
