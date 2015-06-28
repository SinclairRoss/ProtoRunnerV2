package com.raggamuffin.protorunnerv2.utils;

public class DecayCounter 
{
	private double m_Counter;
	private double m_Max;
	private double m_DecayRate;
	
	public DecayCounter(double max, double decayRate)
	{
		m_Counter = 0.0;
		m_Max = max;
		m_DecayRate = decayRate;
	}
	
	public void Update(double deltaTime)
	{
		m_Counter -= m_DecayRate * deltaTime;
		m_Counter  = MathsHelper.Clamp(m_Counter, 0, m_Max);
	}
	
	public void AddValue(double value)
	{
		m_Counter += value;
		m_Counter = MathsHelper.Clamp(m_Counter, 0, m_Max);
	}
	
	public double GetValue()
	{
		return m_Counter;
	}
}
