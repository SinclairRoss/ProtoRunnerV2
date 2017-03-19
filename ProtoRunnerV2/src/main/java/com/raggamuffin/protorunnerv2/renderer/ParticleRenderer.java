package com.raggamuffin.protorunnerv2.renderer;

import com.raggamuffin.protorunnerv2.particles.Particle;
import com.raggamuffin.protorunnerv2.particles.ParticleType;
import com.raggamuffin.protorunnerv2.particles.Particle_Standard;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.concurrent.CopyOnWriteArrayList;

public class ParticleRenderer
{
    private GLParticleGroup m_ActiveParticleGroup;

    private GLParticleGroup_Standard m_Standard;
    private GLParticleGroup_Multiplier m_MultiplierParticle;

    public ParticleRenderer()
    {
        m_ActiveParticleGroup = null;

        m_Standard = null;
        m_MultiplierParticle = null;
    }

    public void LoadAssets()
    {
        m_Standard = new GLParticleGroup_Standard();
        m_MultiplierParticle = new GLParticleGroup_Multiplier();
    }

    public void Initialise(ParticleType type, float[] projMatrix, Vector3 eye, CopyOnWriteArrayList<Particle> particles)
    {
        m_ActiveParticleGroup = null;

        switch (type)
        {
            case Standard:
            {
                m_ActiveParticleGroup = m_Standard;
                break;
            }
            case Multiplier:
            {
                m_ActiveParticleGroup = m_MultiplierParticle;
                break;
            }
        }

        m_ActiveParticleGroup.InitialiseModel(projMatrix, eye);
        m_ActiveParticleGroup.AddPoints(particles);

    }

    public void Draw()
    {
        m_ActiveParticleGroup.Draw();
    }

    public void Clean()
    {
        m_ActiveParticleGroup.CleanModel();
    }
}