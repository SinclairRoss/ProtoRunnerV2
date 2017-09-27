package com.raggamuffin.protorunnerv2.particles;

// Author: Sinclair Ross
// Date:   15/03/2017

import com.raggamuffin.protorunnerv2.managers.ParticleManager;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Trail
{
    private ParticleManager m_ParticleManager;

    private TrailEmitter m_Anchor;
    private ArrayList<TrailNode> m_Nodes;

    public Trail(TrailEmitter anchor, ParticleManager pManager)
    {
        m_ParticleManager = pManager;

        m_Anchor = anchor;
        m_Nodes = new ArrayList<>();
    }

    public void Update()
    {
        m_Nodes.get(m_Nodes.size() - 1).SetPosition(m_Anchor.GetPosition());

        for(int i = 0; i < m_Nodes.size();)
        {
            TrailNode node = m_Nodes.get(i);
            node.Update();

            if(!node.IsValid())
            {
                node.CleanUp();
                m_ParticleManager.RecycleTrailNode(node);
                m_Nodes.remove(i);
            }
            else
            {
                ++i;
            }
        }
    }

    public ArrayList<TrailNode> GetNodes() { return m_Nodes; }
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
