package com.raggamuffin.protorunnerv2.particles;

public class EmissionBehaviour_Timed extends EmissionBehaviour
{
	private double m_EmissionPeriod;
	private double m_EmissionTimer;
	
	public EmissionBehaviour_Timed(double period)
	{
		m_EmissionPeriod = period;
		m_EmissionTimer = 0.0;
	}
	
	@Override
	public int GetEmissionCount(double deltaTime) 
	{
		int count = 0;
		
		m_EmissionTimer += deltaTime;
		
		while(m_EmissionTimer >= m_EmissionPeriod)
		{
			m_EmissionTimer -= m_EmissionPeriod;
			count ++;
		}
		
		return count;
	}
}
