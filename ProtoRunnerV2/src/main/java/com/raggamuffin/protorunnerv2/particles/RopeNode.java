package com.raggamuffin.protorunnerv2.particles;

// Author: Sinclair Ross
// Date:   16/06/2016

import android.util.Log;

import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.RopeSpring;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Timer_Accumulation;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class RopeNode
{
    private enum NodeState
    {
        Alive,
        Frozen,
        Dying,
        Dead
    }

    private RopeNode m_Parent;
    private RopeNode m_Child;
    private Vector3 m_Position;

    private double m_Alpha;

    private Vector3 m_Velocity;
    private RopeSpring m_Spring;
    private double m_DragCoefficient;
    private Vector3 m_Gravity;

    private double m_RopeLengthSqr;
    private double m_ToNodeLength;
    private double m_NormalisedLength;

    private NodeState m_NodeState;
    private Timer m_LifeSpanTimer;

    public RopeNode(Vector3 position, RopeNode parent, double springLength, double springStrength, double gravityStrength, double lifeDuration)
    {
        m_Parent = parent;
        m_Child =  null;
        m_Position = new Vector3(position);

        m_Alpha = 1.0;

        m_Velocity = new Vector3();
        m_Gravity = new Vector3(0, gravityStrength, 0);

        if(m_Parent != null)
        {
            m_Spring = new RopeSpring(this, m_Parent, springStrength, springLength);
        }

        m_DragCoefficient = 5;

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

    public void Update(double deltaTime)
    {
        switch(m_NodeState)
        {
            case Alive:
            {
                if(m_Spring != null)
                {
                    m_Spring.Update(deltaTime);
                }

                ApplyForce(m_Gravity, deltaTime);
                ApplyDrag(deltaTime);

                UpdatePosition(deltaTime);

                break;
            }
            case Frozen:
            {
                if(m_Parent.IsDead())
                {
                    m_NodeState = NodeState.Dying;
                }

                break;
            }
            case Dying:
            {
                m_Alpha = m_LifeSpanTimer.GetInverseProgress();

                if(m_LifeSpanTimer.HasElapsed())
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

    public void ApplyForce(Vector3 force, double deltaTime) { ApplyForce(force.X, force.Y, force.Z, deltaTime); }
    public void ApplyForce(double force_x, double force_y, double force_z, double deltaTime)
    {
        m_Velocity.X += force_x * deltaTime;
        m_Velocity.Y += force_y * deltaTime;
        m_Velocity.Z += force_z * deltaTime;
    }

    private void ApplyDrag(double deltaTime)
    {
        double drag_x = (m_DragCoefficient * m_Velocity.X);
        double drag_y = (m_DragCoefficient * m_Velocity.Y);
        double drag_z = (m_DragCoefficient * m_Velocity.Z);

        ApplyForce(-drag_x, -drag_y, -drag_z, deltaTime);
    }

    private void UpdatePosition(double deltaTime)
    {
        m_Position.X += m_Velocity.X * deltaTime;
        m_Position.Y += m_Velocity.Y * deltaTime;
        m_Position.Z += m_Velocity.Z * deltaTime;
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

    public double GetAlpha() { return m_Alpha; }

    public Vector3 GetVelocity() { return m_Velocity; }

    public void CalculateNormalisedLength() { m_NormalisedLength = MathsHelper.Normalise(m_ToNodeLength, 0, m_RopeLengthSqr); }

    public void SetRopeLengthSqr(double ropeLengthSqr) { m_RopeLengthSqr = ropeLengthSqr; }

    public void SetToNodeLength(double toNodeLength) { m_ToNodeLength = toNodeLength; }

    public double GetNormalisedLength()
    {
        return m_NormalisedLength;
    }

    public void FreezeNode()
    {
        m_NodeState = NodeState.Frozen;
    }

    public void KillNode()
    {
        m_LifeSpanTimer.Start();
        m_NodeState = NodeState.Dying;

        Log.e("ten", "Ded");
    }

    public boolean IsDead()
    {
        return m_NodeState == NodeState.Dead;
    }
}

