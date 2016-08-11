package com.raggamuffin.protorunnerv2.renderer;

// Author: Sinclair Ross
// Date:   16/06/2016

import com.raggamuffin.protorunnerv2.gameobjects.Rope;
import com.raggamuffin.protorunnerv2.particles.RopeNode;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class RopeRenderer
{
    private GLModel_Rope m_Rope;

    public RopeRenderer()
    {
        m_Rope = null;
    }

    public void LoadAssets()
    {
        m_Rope = new GLModel_Rope();
    }

    public void Initialise(float[] projMatrix, Vector3 eye)
    {
        m_Rope.InitialiseModel(projMatrix, eye);
    }

    public void Draw(Rope rope)
    {
        RopeNode node = rope.GetHeadNode();

        while (node != null)
        {
            m_Rope.AddPoint(node.GetPosition(), node.GetNormalisedLength());
            node = node.GetChild();
        }

        m_Rope.Draw(rope.GetColdColour(), rope.GetHotColour(), rope.GetBloomPoint());
    }

    public void Clean()
    {
        m_Rope.CleanModel();
    }
}
