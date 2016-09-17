package com.raggamuffin.protorunnerv2.particles;

// Author: Sinclair Ross
// Date:   16/06/2016

import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.RopeSpring;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class RopeNode
{
    private enum NodeState
    {
        Alive,
        Dying,
        Dead
    }

    private RopeNode m_Parent;
    private RopeNode m_Child;
    private Vector3 m_Position;

    private double m_Alpha;

    private double m_Mass;
    private Vector3 m_Force;
    private Vector3 m_Velocity;
    private RopeSpring m_Spring;
    private double m_DragCoefficient;
    private Vector3 m_Gravity;

    private double m_RopeLengthSqr;
    private double m_ToNodeLength;
    private double m_NormalisedLength;

    private NodeState m_NodeState;
    private Timer m_LifeSpanTimer;

    public RopeNode(Vector3 position, RopeNode parent, double mass, double springLength, double springStrength, double gravityStrength, double lifeDuration)
    {
        m_Parent = parent;
        m_Child =  null;
        m_Position = new Vector3(position);

        m_Alpha = 1.0;

        m_Mass = mass;
        m_Force = new Vector3();
        m_Velocity = new Vector3();
        m_Gravity = new Vector3(0, gravityStrength, 0);

        if(m_Parent != null)
        {
            m_Spring = new RopeSpring(this, m_Parent, springStrength, springLength);
        }

        m_DragCoefficient = 0.85;

        m_NormalisedLength = 0.0;
        m_ToNodeLength = 0.0;
        m_RopeLengthSqr = 0.0;

        m_NodeState = NodeState.Alive;
        m_LifeSpanTimer = new Timer(lifeDuration);
    }

    public void SetChild(RopeNode child)
    {
        m_Child = child;
    }

    public void UpdatePhysics()
    {
        if(m_Spring != null)
        {
            m_Spring.Update();
        }

        m_Force.Add(m_Gravity);
    }

    public void Update(double deltaTime)
    {
        switch(m_NodeState)
        {
            case Alive:
            {
                CalculateVelocity(deltaTime);
                UpdatePosition(deltaTime);

                break;
            }
            case Dying:
            {
                m_LifeSpanTimer.Update(deltaTime);
                m_Alpha = m_LifeSpanTimer.GetInverseProgress();

                if(m_LifeSpanTimer.TimedOut())
                {
                    if(m_Child != null)
                    {
                        m_Child.KillNode();
                    }

                    m_NodeState = NodeState.Dead;
                }

                break;
            }
            case Dead:
            {
                break;
            }
        }
    }

    private void CalculateVelocity(double deltaTime)
    {
        m_Velocity.I += (m_Force.I / m_Mass) * deltaTime;
        m_Velocity.J += (m_Force.J / m_Mass) * deltaTime;
        m_Velocity.K += (m_Force.K / m_Mass) * deltaTime;

        m_Velocity.Scale(m_DragCoefficient);
    }

    private void UpdatePosition(double deltaTime)
    {
        m_Position.I += m_Velocity.I * deltaTime;
        m_Position.J += m_Velocity.J * deltaTime;
        m_Position.K += m_Velocity.K * deltaTime;
    }

    public void CleanUp()
    {
        InvalidateParent();

        if(m_Child != null)
        {
            m_Child.InvalidateParent();
        }
    }

    public Vector3 GetPosition()
    {
        return m_Position;
    }

    public void SetPosition(Vector3 pos)
    {
        m_Position.SetVector(pos);
    }

    public void SetVelocity(Vector3 velocity)
    {
        m_Velocity.SetVector(velocity);
    }

    public RopeNode GetChild()
    {
        return m_Child;
    }

    public void InvalidateParent()
    {
        m_Parent = null;
    }

    public void ApplyForce(Vector3 force)
    {
        ApplyForce(force.I, force.J, force.K);
    }

    public void ApplyForce(double i, double j, double k)
    {
        m_Force.Add(i, j, k);
    }

    public void ClearForces()
    {
        m_Force.SetVector(0);
    }

    public double GetAlpha()
    {
        return m_Alpha;
    }

    public Vector3 GetVelocity()
    {
        return m_Velocity;
    }

    public void CalculateNormalisedLength()
    {
        m_NormalisedLength = MathsHelper.Normalise(m_ToNodeLength, 0, m_RopeLengthSqr);
    }

    public void SetRopeLengthSqr(double ropeLengthSqr)
    {
        m_RopeLengthSqr = ropeLengthSqr;
    }

    public void SetToNodeLength(double toNodeLength)
    {
        m_ToNodeLength = toNodeLength;
    }

    public double GetNormalisedLength()
    {
        return m_NormalisedLength;
    }

    public void HaltNode()
    {
        m_Force.SetVector(0);
        m_Velocity.SetVector(0);
    }

    public void KillNode()
    {
        m_NodeState = NodeState.Dying;
    }

    public boolean IsDead()
    {
        return m_NodeState == NodeState.Dead;
    }
}

