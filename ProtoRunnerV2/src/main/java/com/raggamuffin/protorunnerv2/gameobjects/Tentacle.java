package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   16/06/2016

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.RopeNode;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.ArrayList;

public class Tentacle extends GameObject
{
    public enum TentacleState
    {
        Normal,
        Frozen
    }

    private TentacleState m_TentacleState;

    private GameObject m_HeadAnchor;
    private GameObject m_TailAnchor;

    private double m_BloomPoint;

    private ArrayList<RopeNode> m_RopeNodes;
    private RopeNode m_HeadNode;
    private RopeNode m_TailNode;

    public Tentacle(GameLogic game, GameObject headAnchor, GameObject tailAnchor, Colour hotColour, Colour coldColour)
    {
        super(game, ModelType.Nothing);

        final double ROPE_MASS = 0.5;
        final double ROPE_LENGTH = 10;
        final double ROPE_RIGIDNESS = 200;
        final int ROPE_RESOLUTION = 30;
        final double GRAVITY_STRENGTH = -2.0;
        final double LIFE_SPAN = 3.0;

        m_HeadAnchor = headAnchor;
        m_TailAnchor = tailAnchor;

        m_TentacleState = TentacleState.Normal;

        m_BaseColour = hotColour;
        m_AltColour = coldColour;

        m_BloomPoint = 0;

        final double nodeMass = ROPE_MASS / ROPE_RESOLUTION;
        final double springLength = ROPE_LENGTH / ROPE_RESOLUTION;
        final double springStrength = ROPE_RIGIDNESS / ROPE_RESOLUTION;
        final double tentacleLifeSpan = LIFE_SPAN / ROPE_RESOLUTION;

        m_RopeNodes = new ArrayList<>(ROPE_RESOLUTION);

        m_HeadNode = new RopeNode(new Vector3(0), null, nodeMass, springLength, springStrength, GRAVITY_STRENGTH, tentacleLifeSpan);
        m_RopeNodes.add(m_HeadNode);

        for(int i = 1; i < ROPE_RESOLUTION; i++)
        {
            RopeNode parent = m_RopeNodes.get(i-1);
            RopeNode child = new RopeNode(m_HeadAnchor.GetPosition(), parent, nodeMass, springLength, springStrength, GRAVITY_STRENGTH, tentacleLifeSpan);

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
                UpdatePhysics();

                break;
            }
            case Frozen:
            {
                break;
            }
        }

        for (RopeNode node : m_RopeNodes)
        {
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

    private void UpdatePhysics()
    {
        for (RopeNode node : m_RopeNodes)
        {
            node.ClearForces();
        }

        for (RopeNode node : m_RopeNodes)
        {
            node.UpdatePhysics();
        }

        UpdateNormalisedNodeLength();
    }

    @Override
    public boolean IsValid()
    {
        if(m_TentacleState == TentacleState.Frozen &&
            m_TailNode.IsDead())
        {
            return false;
        }

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

    public void CleanUp()
    {
        super.CleanUp();

        m_HeadAnchor = null;
        m_TailAnchor = null;

        for(RopeNode node : m_RopeNodes)
        {
            node.CleanUp();
        }

        m_RopeNodes.clear();
    }

    public void KillTentacle()
    {
        m_TentacleState = TentacleState.Frozen;

        for(RopeNode node : m_RopeNodes)
        {
            node.HaltNode();
        }

        m_HeadNode.KillNode();
    }

    public RopeNode GetHeadNode()
    {
        return m_HeadNode;
    }

    public double GetBloomPoint()
    {
        return m_BloomPoint;
    }
}
