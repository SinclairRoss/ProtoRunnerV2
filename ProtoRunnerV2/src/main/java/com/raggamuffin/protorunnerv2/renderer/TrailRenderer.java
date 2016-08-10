package com.raggamuffin.protorunnerv2.renderer;

import com.raggamuffin.protorunnerv2.particles.TrailNode;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class TrailRenderer
{
    private GLModel_Line m_Trail;

    public TrailRenderer()
    {
        m_Trail = null;
    }

    public void LoadAssets()
    {
        m_Trail = new GLModel_Line();
    }

    public void Initialise(float[] projMatrix, Vector3 eye)
    {
        m_Trail.InitialiseModel(projMatrix, eye);
    }

    public void Draw(TrailNode headNode)
    {
        TrailNode node = headNode;

        while (node != null)
        {
            m_Trail.AddPoint(node.GetPosition(), node.GetColour());
            node = node.GetParent();
        }

        m_Trail.Draw();
    }

    public void Clean()
    {
        m_Trail.CleanModel();
    }
}
