package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ParticleEmitter_Burst extends ParticleEmitter
{
    private int m_EmissionCount;
    private Vector3 m_ParticleForward;

    public ParticleEmitter_Burst(GameLogic game, Colour initialColour, Colour finalColour, int emissionCount)
    {
        super(game, initialColour, finalColour, 9000, 2);

        m_EmissionCount = emissionCount;
        m_ParticleForward = new Vector3();

        SetForward(0,1,0);
    }

    public void Burst()
    {
        for (int i = 0; i < m_EmissionCount; i++)
            m_ParticleManager.CreateParticle(this);
    }

    @Override
    public Vector3 CalculateParticleForward()
    {
        double I = MathsHelper.RandomDouble(-1.0, 1.0);
        double J = MathsHelper.RandomDouble( 0.0, 1.0);
        double K = MathsHelper.RandomDouble(-1.0, 1.0);

        m_ParticleForward.SetVector(I, J, K);
        m_ParticleForward.Normalise();

        return m_ParticleForward;
    }
}
