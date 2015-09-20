package com.raggamuffin.protorunnerv2.renderer;

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ParticleRenderer
{
    private GLParticle m_Point;

    public ParticleRenderer()
    {
        m_Point = null;
    }

    public void LoadAssets()
    {
        m_Point = new GLParticle();
    }

    public void Initialise(float[] projMatrix, Vector3 eye)
    {
        m_Point.InitialiseModel(projMatrix, eye);
    }

    public void Draw(Vector3 pos, Colour colour)
    {
        m_Point.AddPoint(pos, colour);
    }

    public void Clean()
    {
        m_Point.Draw();
        m_Point.CleanModel();
    }
}
