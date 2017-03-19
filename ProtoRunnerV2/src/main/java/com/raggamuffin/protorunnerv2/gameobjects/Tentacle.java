package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   16/06/2016

import android.util.Log;

import com.raggamuffin.protorunnerv2.particles.RopeNode;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.ArrayList;

public class Tentacle extends GameObject
{
    private enum TentacleState
    {
        Normal,
        Frozen
    }

    public static final int ROPE_RESOLUTION = 30;

    private TentacleState m_TentacleState;

    private GameObject m_HeadAnchor;
    private GameObject m_TailAnchor;

    private double m_BloomPoint;

    private ArrayList<RopeNode> m_RopeNodes;
    private RopeNode m_HeadNode;
    private RopeNode m_TailNode;

    private Colour m_ColdColour;

    public Tentacle(GameObject headAnchor, GameObject tailAnchor, Colour hotColour, Colour coldColour)
    {
        super(ModelType.Nothing, 0);

        final double ROPE_LENGTH = 10;
        final double ROPE_RIGIDNESS = 9000;
        final double GRAVITY_STRENGTH = -25.0;
        final double LIFE_SPAN = 3.0;

        m_HeadAnchor = headAnchor;
        m_TailAnchor = tailAnchor;

        m_TentacleState = TentacleState.Normal;

        SetColour(hotColour);
        m_ColdColour = new Colour(coldColour);
        m_ColdColour.Alpha = 0.2;

        m_BloomPoint = 0;

        final double springLength = ROPE_LENGTH / ROPE_RESOLUTION;
        final double springStrength = ROPE_RIGIDNESS / ROPE_RESOLUTION;
        final double tentacleLifeSpan = LIFE_SPAN / ROPE_RESOLUTION;

        m_RopeNodes = new ArrayList<>(ROPE_RESOLUTION);

        m_HeadNode = new RopeNode(new Vector3(0), null, springLength, springStrength, GRAVITY_STRENGTH, tentacleLifeSpan);
        m_RopeNodes.add(m_HeadNode);

        for(int i = 1; i < ROPE_RESOLUTION; i++)
        {
            RopeNode parent = m_RopeNodes.get(i-1);
            RopeNode child = new RopeNode(m_HeadAnchor.GetPosition(), parent, springLength, springStrength, GRAVITY_STRENGTH, tentacleLifeSpan);

            m_RopeNodes.add(child);
            parent.SetChild(child);
        }

        m_TailNode = m_RopeNodes.get(m_RopeNodes.size() - 1);
    }

    public void Update(double deltaTime)
    {
        switch (m_TentacleState)
        {
            case Normal:
            {
                UpdateHead();
                UpdateTail();
                UpdateBloomPoint(deltaTime);
                UpdateNormalisedNodeLength();

                break;
            }
            case Frozen:
            {
                break;
            }
        }

        int numRopeNodes = m_RopeNodes.size();
        for(int i = 0; i < numRopeNodes; ++i)
        {
            RopeNode node = m_RopeNodes.get(i);
            node.Update(deltaTime);
        }
    }

    private void UpdateHead()
    {
        if (m_HeadAnchor != null)
        {
            m_HeadNode.SetVelocity(m_HeadAnchor.GetVelocity());
            m_HeadNode.SetPosition(m_HeadAnchor.GetPosition());
        }
    }

    private void UpdateTail()
    {
        if (m_TailAnchor != null)
        {
            m_TailNode.SetVelocity(m_TailAnchor.GetVelocity());
            m_TailNode.SetPosition(m_TailAnchor.GetPosition());
        }
    }

    private void UpdateBloomPoint(double deltaTime)
    {
        m_BloomPoint += deltaTime;
        m_BloomPoint %= 1.0f;
    }

    @Override
    public boolean IsValid()
    {
        return m_TentacleState != TentacleState.Frozen || !m_TailNode.IsDead();
    }

    private void UpdateNormalisedNodeLength()
    {
        double length = 0.0;

        int numRopeNodes = m_RopeNodes.size();
        for(int i = 0; i < numRopeNodes; ++i)
        {
            RopeNode node = m_RopeNodes.get(i);
            node.SetToNodeLength(length);

            RopeNode child = node.GetChild();

            if(child != null)
            {
                Vector3 nodePosition = node.GetPosition();
                Vector3 childPosition = child.GetPosition();

                double distanceSqr = Vector3.DistanceBetweenSqr(nodePosition, childPosition);
                length += distanceSqr;
            }
        }

        for(int i = 0; i < numRopeNodes; ++i)
        {
            RopeNode node = m_RopeNodes.get(i);
            node.SetRopeLengthSqr(length);
            node.CalculateNormalisedLength();
        }
    }

    public void CleanUp()
    {
        m_HeadAnchor = null;
        m_TailAnchor = null;

        int numRopeNodes = m_RopeNodes.size();
        for(int i = 0; i < numRopeNodes; ++i)
        {
            RopeNode node = m_RopeNodes.get(i);
            node.CleanUp();
        }

        m_RopeNodes.clear();
    }

    public void KillTentacle()
    {
        m_TentacleState = TentacleState.Frozen;

        int numRopeNodes = m_RopeNodes.size();
        for(int i = 0; i < numRopeNodes; ++i)
        {
            RopeNode node = m_RopeNodes.get(i);
            node.FreezeNode();
        }

        m_HeadNode.KillNode();
    }

    public Colour GetColdColour() { return m_ColdColour; }

    public RopeNode GetHeadNode() { return m_HeadNode; }

    public double GetBloomPoint() {  return m_BloomPoint; }
}
