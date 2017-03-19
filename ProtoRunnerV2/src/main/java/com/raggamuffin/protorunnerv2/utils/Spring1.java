package com.raggamuffin.protorunnerv2.utils;

// Author: Sinclair Ross
// Date:   19/03/2017

public class Spring1
{
    private double m_Stiffness;

    public Spring1(double stiffness)
    {
        m_Stiffness = stiffness;
    }

    public double CalculateSpringForce(double position, double relaxedPosition)
    {
        double stretch = position - relaxedPosition;
        double force = -m_Stiffness * stretch;

        return force;
    }

    public void SetStiffness(double stiffness)
    {
        m_Stiffness = stiffness;
    }
}
