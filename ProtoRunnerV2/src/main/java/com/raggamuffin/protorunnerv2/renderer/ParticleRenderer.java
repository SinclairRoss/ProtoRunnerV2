package com.raggamuffin.protorunnerv2.renderer;

import com.raggamuffin.protorunnerv2.RenderObjects.RenderObject_Particle;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.ArrayList;

public class ParticleRenderer
{
    private GLParticleGroup_Standard m_Standard;

    public ParticleRenderer()
    {
        m_Standard = null;
    }

    public void LoadAssets()
    {
        m_Standard = new GLParticleGroup_Standard();
    }

    public void Initialise(float[] projMatrix, Vector3 eye, ArrayList<RenderObject_Particle> particles, int particleCount)
    {
        m_Standard.InitialiseModel(projMatrix, eye);
        m_Standard.AddPoints(particles, particleCount);
    }

    public void Draw()
    {
        m_Standard.Draw();
    }

    public void Clean()
    {
        m_Standard.CleanModel();
    }
}