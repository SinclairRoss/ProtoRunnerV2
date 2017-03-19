package com.raggamuffin.protorunnerv2.particles;

// Author: Sinclair Ross
// Date:   15/03/2017

import java.util.concurrent.CopyOnWriteArrayList;

public class Trail
{
    private TrailEmitter m_Anchor;
    private CopyOnWriteArrayList<TrailNode> m_Nodes;

    public Trail(TrailEmitter anchor)
    {
        m_Anchor = anchor;
        m_Nodes = new CopyOnWriteArrayList<>();
    }

    public void Update()
    {
        int nodeCount = m_Nodes.size();

        m_Nodes.get(m_Nodes.size() - 1).SetPosition(m_Anchor.GetPosition());

        for(int i = 0; i < nodeCount; ++i)
        {
            TrailNode node = m_Nodes.get(i);

            if(node.IsValid())
            {
                node.Update();
            }
            else
            {
                node.CleanUp();
                m_Nodes.remove(i);

                --i;
                --nodeCount;
            }
        }
    }

    public CopyOnWriteArrayList<TrailNode> GetNodes() { return m_Nodes; }
    public TrailNode GetHeadNode() { return m_Nodes.isEmpty() ? null : m_Nodes.get(0); }

    public void AddNode(TrailNode node)
    {
        m_Nodes.add(node);
    }

    public boolean IsValid()
    {
        return m_Nodes.size() > 0;
    }
}
