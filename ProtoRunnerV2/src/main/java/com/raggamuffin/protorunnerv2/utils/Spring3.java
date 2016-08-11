package com.raggamuffin.protorunnerv2.utils;

public class Spring3
{
	private Vector3 m_Force;

	private double m_Stiffness;
    private double m_SpringLength;

    private Vector3 m_Stretch;

    public Spring3(double stiffness, double springLength)
    {
        m_Force = new Vector3();

        m_Stiffness = stiffness;
        m_SpringLength = springLength;

        m_Stretch = new Vector3();
    }

    public Vector3 CalculateSpringForce(Vector3 position, Vector3 relaxedPosition)
    {
        m_Stretch.SetVectorDifference(relaxedPosition, position);
        double stretchLength = m_Stretch.GetLength() - m_SpringLength;

        m_Stretch.Normalise();
        m_Stretch.Scale(stretchLength);

        m_Force.I = -m_Stiffness * m_Stretch.I;
        m_Force.J = -m_Stiffness * m_Stretch.J;
        m_Force.K = -m_Stiffness * m_Stretch.K;

        return m_Force;
    }

    public void SetStiffness(double stiffness)
    {
        m_Stiffness = stiffness;
    }
}	
