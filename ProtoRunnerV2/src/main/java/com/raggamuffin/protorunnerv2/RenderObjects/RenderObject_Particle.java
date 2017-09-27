package com.raggamuffin.protorunnerv2.RenderObjects;

// Author: Sinclair Ross
// Date:   13/08/2017

import com.raggamuffin.protorunnerv2.particles.Particle;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class RenderObject_Particle
{
    private Vector3 m_Position;
    private Colour m_Colour;

    public RenderObject_Particle()
    {
        m_Position = new Vector3();
        m_Colour = new Colour();
    }

    public void SetupForParticle(Particle particle)
    {
        m_Position.SetVector(particle.GetPosition());
        m_Colour.SetColour(particle.GetColour());
    }

    public Vector3 GetPosition() { return m_Position; }
    public Colour GetColour() { return m_Colour; }
}
