package com.raggamuffin.protorunnerv2.particles;

// Author: Sinclair Ross
// Date:   15/01/2017

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ParticleEmitter_MultiplierPopper extends ParticleEmitter
{
    private Vector3 m_ParticleForward;
    private Timer m_EmissionTimer;

    private int m_NumParticlesToEmit;
    private int m_ParticlesEmitted ;

    private final double m_HeadingDeviation;
    private final double m_EmissionDuration;

    public ParticleEmitter_MultiplierPopper(GameLogic game, int numParticlesToEmit)
    {
        //new Colour(Colours.PastelBlueDark), new Colour(Colours.LauraLynneGreen)
        super(game, new Colour(Colours.PastelGreen), new Colour(Colours.EmeraldGreen), 100, 5.0, ParticleType.Multiplier);

        m_ParticleForward = new Vector3();
        m_HeadingDeviation = Math.toRadians(20);
        m_EmissionDuration = 0.4;

        m_EmissionTimer = new Timer(m_EmissionDuration / numParticlesToEmit);

        m_NumParticlesToEmit = numParticlesToEmit;
        m_ParticlesEmitted = 0;
    }

    public void Start(Vector3 position, Vector3 velocity)
    {
        m_Position.SetVector(position);
        m_Velocity.SetVector(velocity);
        m_ParticlesEmitted = 0;

        m_EmissionTimer.Start();
    }

    @Override
    public Vector3 CalculateParticleForward()
    {
        m_ParticleForward.SetVector(Vector3.UP);
        m_ParticleForward.RotateX(MathsHelper.RandomDouble(-m_HeadingDeviation, m_HeadingDeviation));
        m_ParticleForward.RotateZ(MathsHelper.RandomDouble(-m_HeadingDeviation, m_HeadingDeviation));

        return m_ParticleForward;
    }

    public void Update()
    {
        if (m_EmissionTimer.HasElapsed())
        {
            if(IsActive())
            {
                m_EmissionTimer.Start();
                CreateParticle();

                ++m_ParticlesEmitted;
            }
            else
            {
                m_EmissionTimer.Stop();
            }
        }
    }

    public boolean IsActive()
    {
        return m_ParticlesEmitted < m_NumParticlesToEmit;
    }
}
