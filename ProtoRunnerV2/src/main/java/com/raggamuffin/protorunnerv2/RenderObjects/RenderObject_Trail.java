package com.raggamuffin.protorunnerv2.RenderObjects;

// Author: Sinclair Ross
// Date:   07/08/2017

import com.raggamuffin.protorunnerv2.particles.Trail;
import com.raggamuffin.protorunnerv2.particles.TrailNode;

import java.util.ArrayList;

public class RenderObject_Trail
{
    private ArrayList<RenderObject_TrailNode> m_RenderNodes;
    private int m_NodeCount;

    public RenderObject_Trail()
    {
        m_RenderNodes = new ArrayList<>();
        m_NodeCount = 0;
    }

    public void SetupForTrail(Trail trail)
    {
        m_NodeCount = 0;

        ArrayList<TrailNode> trailNodes = trail.GetNodes();
        int numNodes = trailNodes.size();

        for(int i = 0; i < numNodes; ++i)
        {
            RenderObject_TrailNode rNode;

            if(m_RenderNodes.size() > m_NodeCount)
            {
                rNode = m_RenderNodes.get(m_NodeCount);
            }
            else
            {
                rNode = new RenderObject_TrailNode();
                m_RenderNodes.add(rNode);
            }

            TrailNode node = trailNodes.get(i);
            rNode.SetupForNode(node);

            ++ m_NodeCount;
        }
    }

    public ArrayList<RenderObject_TrailNode> GetNodes() { return m_RenderNodes; }
    public int GetNodeCount() { return m_NodeCount; }
}
