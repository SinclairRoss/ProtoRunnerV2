package com.raggamuffin.protorunnerv2.particles;

// Author: Sinclair Ross
// Date:   16/06/2016

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.RopeSpring;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class RopeNode
{
    private RopeNode m_Parent;
    private RopeNode m_Child;
    private Vector3 m_Position;

    private Colour m_Colour;
    private Colour m_HotColour;
    private Colour m_ColdColour;

    private double m_Mass;
    private Vector3 m_Force;
    private Vector3 m_Velocity;
    private RopeSpring m_Spring;
    private double m_DragCoefficient;
    private Vector3 m_Gravity;

    private double m_RopeLengthSqr;
    private double m_ToNodeLength;
    private double m_NormalisedLength;

    public RopeNode(Vector3 position, RopeNode parent, Colour hotColour, Colour coldColour, double mass, double springLength, double springStrength, double gravityStrength)
    {
        m_Parent = parent;
        m_Child =  null;
        m_Position = new Vector3(position);

        m_Colour = new Colour(hotColour);
        m_HotColour = new Colour(hotColour);
        m_ColdColour = new Colour(coldColour);

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
        CalculateVelocity(deltaTime);
        UpdatePosition(deltaTime);

        double lerp = m_NormalisedLength;
        m_Colour.Red = MathsHelper.Lerp(lerp, m_HotColour.Red, m_ColdColour.Red);
        m_Colour.Green = MathsHelper.Lerp(lerp, m_HotColour.Green, m_ColdColour.Green);
        m_Colour.Blue = MathsHelper.Lerp(lerp, m_HotColour.Blue, m_ColdColour.Blue);
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

    public Colour GetColour()
    {
        return m_Colour;
    }

    public void SetColour(double[] colour)
    {
        m_HotColour.SetColour(colour);
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
}

