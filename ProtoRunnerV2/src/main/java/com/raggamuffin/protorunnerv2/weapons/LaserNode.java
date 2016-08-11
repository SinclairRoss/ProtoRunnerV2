package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.utils.Vector3;

public class LaserNode
{
    private Vector3 m_Position;
    private Vector3 m_Velocity;
    private double m_Speed;
    private double m_Range;
    private double m_DistanceTravelled;

    public LaserNode(Vector3 position, Vector3 forward, Vector3 initialVelocity, double speed, double range)
    {
        m_Position = new Vector3(position);

        m_Velocity = new Vector3(forward);
        m_Velocity.Scale(speed);
        m_Velocity.Add(initialVelocity);

        m_Speed = speed;
        m_Range = range;
        m_DistanceTravelled = 0.0;
    }

    public void Update(double deltaTime)
    {
        if (m_DistanceTravelled < m_Range)
        {
            m_Position.I += m_Velocity.I * deltaTime;
            m_Position.J += m_Velocity.J * deltaTime;
            m_Position.K += m_Velocity.K * deltaTime;
        }

        m_DistanceTravelled += m_Speed * deltaTime;
    }

    public Vector3 GetPosition()
    {
        return m_Position;
    }

    public void SetPosition(Vector3 position)
    {
        m_Position.SetVector(position);
    }

    public double GetDistanceTravelled()
    {
        return m_DistanceTravelled;
    }
}

