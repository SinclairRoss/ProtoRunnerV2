package com.raggamuffin.protorunnerv2.utils;

public class Spring3
{
	private Vector3 m_Position;
	private Vector3 m_Velocity;
	private Vector3 m_Acceleration;
	private Vector3 m_RelaxedPosition;
	private Vector3 m_Stretch;
	private Vector3 m_Force;
	
	private double m_Stiffness;
	private double m_Damping;
	private double m_Mass;
	
	public Spring3(Vector3 position)
	{
		m_Position = position;
		m_Velocity = new Vector3();
		m_Acceleration = new Vector3();
		m_RelaxedPosition = new Vector3();
		m_Stretch = new Vector3();
		m_Force = new Vector3();
		
		m_Stiffness = 0.16;
		m_Damping = 0.06;
		m_Mass = 1;
	}
	
	public void Update(double deltaTime)
	{
		CalculateForce();
		CalculateAcceleration();
		m_Velocity.Add(m_Acceleration);
		UpdatePosition(deltaTime);
	}
	
	private void CalculateForce()
	{
		m_Stretch.SetVectorDifference(m_RelaxedPosition, m_Position);
		
		m_Force.I = (-m_Stiffness * m_Stretch.I) - (m_Damping * m_Velocity.I);
		m_Force.J = (-m_Stiffness * m_Stretch.J) - (m_Damping * m_Velocity.J);
		m_Force.K = (-m_Stiffness * m_Stretch.K) - (m_Damping * m_Velocity.K);	
	}
	
	private void CalculateAcceleration()
	{
		m_Acceleration.I = (m_Force.I / m_Mass);
		m_Acceleration.J = (m_Force.J / m_Mass);
		m_Acceleration.K = (m_Force.K / m_Mass);
	}

	private void UpdatePosition(double deltaTime)
	{
		m_Position.I += m_Velocity.I * deltaTime;
		m_Position.J += m_Velocity.J * deltaTime;
		m_Position.K += m_Velocity.K * deltaTime;
	}
	
	public void SetRelaxedPosition(double i, double j, double k)
	{
		m_RelaxedPosition.SetVector(i,j,k);
	}
	
	public Vector3 GetPosition()
	{
		return m_Position;
	}
}	
