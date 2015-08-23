package com.raggamuffin.protorunnerv2.utils;

public class Spring1
{
    private double m_Position;
    private double m_Velocity;
    private double m_Acceleration;
    private double m_RelaxedPosition;
    private double m_Force;

    private double m_Stiffness;
    private double m_Damping;
    private double m_Mass;

    public Spring1(double stiffness, double damping, double mass)
    {
        m_Position = 0.0;
        m_Velocity = 0.0;
        m_Acceleration = 0.0;
        m_RelaxedPosition = 0.0;
        m_Force = 0.0;

        m_Stiffness = stiffness;
        m_Damping = damping;
        m_Mass = mass;
    }

    public double Update(double deltaTime, double currentPos)
    {
        CalculateForce(currentPos);
        CalculateAcceleration();
        m_Velocity += m_Acceleration;
        m_Position += m_Velocity * deltaTime;

        return m_Position;
    }

    private void CalculateForce(double currentPos)
    {
        double stretch = currentPos - m_RelaxedPosition;
        m_Force = (-m_Stiffness * stretch) - (m_Damping * m_Velocity);
    }

    private void CalculateAcceleration()
    {
        m_Acceleration = (m_Force / m_Mass);
    }

    public void SetRelaxedPosition(double pos)
    {
        m_RelaxedPosition = pos;
    }

    public double GetPosition()
    {
        return m_Position;
    }
}
