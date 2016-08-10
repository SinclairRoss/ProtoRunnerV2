package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   16/06/2016

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.RopeNode;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.ArrayList;

public class Rope extends GameObject
{
    public enum RopeState
    {
        Normal,
        Frozen
    }

    private RopeState m_RopeState;

    private final double ROPE_MASS = 1.0;
    private final double ROPE_LENGTH = 10;
    private final int ROPE_RESOLUTION = 30;
    private final double GRAVITY_STRENGTH = -1.0;

    private GameObject m_HeadAnchor;
    private GameObject m_TailAnchor;

    private Colour m_HotColour;
    private Colour m_ColdColour;
    private double m_BloomPoint;

    private ArrayList<RopeNode> m_RopeNodes;
    private RopeNode m_HeadNode;
    private RopeNode m_TailNode;

    public Rope(GameObject headAnchor, GameObject tailAnchor, Colour hotColour, Colour coldColour)
    {
        super(null, null);

        m_HeadAnchor = headAnchor;
        m_TailAnchor = tailAnchor;

        m_RopeState = RopeState.Normal;

        m_HotColour = new Colour(hotColour);
        m_ColdColour = new Colour(coldColour);
        m_BloomPoint = 0;

        final double nodeMass = ROPE_MASS / ROPE_RESOLUTION;
        final double springLength = ROPE_LENGTH / ROPE_RESOLUTION;
        final double springStrength = 10;

        m_RopeNodes = new ArrayList<>(ROPE_RESOLUTION);

        m_HeadNode = new RopeNode(new Vector3(0), null, m_HotColour, m_ColdColour, nodeMass, springLength, springStrength, GRAVITY_STRENGTH);
        m_RopeNodes.add(m_HeadNode);

        for(int i = 1; i < ROPE_RESOLUTION; i++)
        {
            RopeNode parent = m_RopeNodes.get(i-1);
            RopeNode child = new RopeNode(m_HeadAnchor.GetPosition(), parent, m_HotColour, m_ColdColour, nodeMass, springLength, springStrength, GRAVITY_STRENGTH);

            m_RopeNodes.add(child);
            parent.SetChild(child);
        }

        m_TailNode = m_RopeNodes.get(m_RopeNodes.size() - 1);
    }

    public void Update(double deltaTime)
    {
        switch (m_RopeState)
        {
            case Normal:
            {
                if (m_HeadAnchor != null)
                {
                    m_HeadNode.SetVelocity(m_HeadAnchor.GetVelocity());
                    m_HeadNode.SetPosition(m_HeadAnchor.GetPosition());
                }

                if (m_TailAnchor != null)
                {
                    m_TailNode.SetVelocity(m_TailAnchor.GetVelocity());
                    m_TailNode.SetPosition(m_TailAnchor.GetPosition());
                }

                m_BloomPoint += deltaTime;
                m_BloomPoint %= 1.0f;

                for (RopeNode node : m_RopeNodes)
                {
                    node.ClearForces();
                }

                for (RopeNode node : m_RopeNodes)
                {
                    node.UpdatePhysics();
                }

                UpdateNormalisedNodeLength();

                for (RopeNode node : m_RopeNodes)
                {
                    node.Update(deltaTime);
                }

                break;
            }
            case Frozen:
            {
                break;
            }
        }
    }

    @Override
    public boolean IsValid()
    {
        if(m_ForciblyInvalidated)
        {
            return false;
        }

        return true;
    }

    private void UpdateNormalisedNodeLength()
    {
        double length = 0.0;

        for(RopeNode node : m_RopeNodes)
        {
            node.SetToNodeLength(length);

            RopeNode child = node.GetChild();

            if(child != null)
            {
                Vector3 nodePosition = node.GetPosition();
                Vector3 childPosition = child.GetPosition();

                double i = childPosition.I - nodePosition.I;
                double j = childPosition.J - nodePosition.J;
                double k = childPosition.K - nodePosition.K;

                length += (i * i) + (j * j) + (k * k);
            }
        }

        for(RopeNode node : m_RopeNodes)
        {
            node.SetRopeLengthSqr(length);
            node.CalculateNormalisedLength();
        }
    }

    public void Destroy()
    {

    }

    public void CleanUp()
    {
        for(RopeNode node : m_RopeNodes)
        {
            node.CleanUp();
        }

        m_RopeNodes.clear();
    }

    public void SetRopeState(RopeState state)
    {
        m_RopeState = state;
    }

    public void SetHeadAnchor(GameObject anchor)
    {
        m_HeadAnchor = anchor;
    }

    public void SetTailAncor(GameObject anchor)
    {
        m_TailAnchor = anchor;
    }

    public RopeNode GetHeadNode()
    {
        return m_HeadNode;
    }

    public Colour GetHotColour()
    {
        return m_HotColour;
    }

    public Colour GetColdColour()
    {
        return m_ColdColour;
    }

    public double GetBloomPoint()
    {
        return m_BloomPoint;
    }
}
