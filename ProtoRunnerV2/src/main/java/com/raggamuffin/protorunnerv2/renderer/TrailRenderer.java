package com.raggamuffin.protorunnerv2.renderer;

import com.raggamuffin.protorunnerv2.particles.TrailPoint;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class TrailRenderer
{
    private GLModel_Trail m_Trail;

    public TrailRenderer()
    {
        m_Trail = null;
    }

    public void LoadAssets()
    {
        m_Trail = new GLModel_Trail();
    }

    public void Initialise(float[] projMatrix, Vector3 eye)
    {
        m_Trail.InitialiseModel(projMatrix, eye);
    }

    public void Draw(TrailPoint headNode)
    {
        TrailPoint node = headNode;

        while (node != null)
        {
            m_Trail.AddPoint(node);
            node = node.GetParent();
        }

        m_Trail.Draw();
    }

    public void Clean()
    {
        m_Trail.CleanModel();
    }
}
