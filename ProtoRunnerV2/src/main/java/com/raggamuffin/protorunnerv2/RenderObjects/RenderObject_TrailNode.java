package com.raggamuffin.protorunnerv2.RenderObjects;

// Author: Sinclair Ross
// Date:   20/08/2017

import com.raggamuffin.protorunnerv2.particles.TrailNode;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class RenderObject_TrailNode
{
    private Vector3 m_Position;
    private Colour m_Colour;

    public RenderObject_TrailNode()
    {
        m_Position = new Vector3();
        m_Colour = new Colour();
    }

    public void SetupForNode(TrailNode node)
    {
        m_Position.SetVector(node.GetPosition());
        m_Colour.SetColour(node.GetColour());
    }

    public Vector3 GetPosition()
    {
        return m_Position;
    }
    public Colour GetColour() { return m_Colour; }
}
