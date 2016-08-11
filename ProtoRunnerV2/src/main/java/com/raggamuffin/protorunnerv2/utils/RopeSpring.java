package com.raggamuffin.protorunnerv2.utils;

// Author: Sinclair Ross
// Date:   17/06/2016

import android.util.Log;

import com.raggamuffin.protorunnerv2.particles.RopeNode;

public class RopeSpring
{
    private RopeNode m_Child;
    private RopeNode m_Parent;

    private double m_Stiffness;
    private double m_SpringLength;
    private double m_FrictionConstant;

    private Vector3 m_Stretch;
    private Vector3 m_SpringForce;
    private Vector3 m_SpringResistance;

    public RopeSpring(RopeNode child, RopeNode parent, double stiffness, double springLength)
    {
        m_Child = child;
        m_Parent = parent;

        m_Stiffness = stiffness;
        m_SpringLength = springLength;

        m_Stretch = new Vector3();
        m_SpringForce = new Vector3();
        m_SpringResistance = new Vector3();
    }

    public void Update()
    {
        m_Stretch.SetVectorDifference(m_Parent.GetPosition(), m_Child.GetPosition());
        double stretchLength = m_Stretch.GetLength() - m_SpringLength;

        m_Stretch.Normalise();
        m_Stretch.Scale(stretchLength);

        m_SpringForce.SetVector(m_Stretch);
        m_SpringForce.Scale(-m_Stiffness);

        m_Child.ApplyForce(m_SpringForce.I, m_SpringForce.J, m_SpringForce.K);
        m_Parent.ApplyForce(-m_SpringForce.I, -m_SpringForce.J, -m_SpringForce.K);
    }
}
