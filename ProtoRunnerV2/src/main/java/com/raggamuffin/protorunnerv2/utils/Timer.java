package com.raggamuffin.protorunnerv2.utils;

public class Timer 
{
	private double m_Timer;
	private double m_Limit;
	
	public Timer(double time)
	{
		m_Timer = 0.0;
		m_Limit = time;
	}
	
	public void Update(final double deltaTime)
	{
		m_Timer += deltaTime;
		MathsHelper.Clamp(m_Timer, 0, m_Limit);
	}
	
	public void ResetTimer()
	{
		m_Timer = 0.0;
	}
	
	public void MaxOutTimer()
	{
		m_Timer = m_Limit;
	}
	
	public boolean TimedOut()
	{
		return m_Timer >= m_Limit;
	}

	public void SetLimit(double limit)
	{
		m_Limit = limit;
	}
	
	public double GetProgress()
	{
		return MathsHelper.Normalise(m_Timer, 0.0, m_Limit);
	}
	
	public double GetInverseProgress()
	{
		return 1.0 - MathsHelper.Normalise(m_Timer, 0.0, m_Limit);
	}
}
