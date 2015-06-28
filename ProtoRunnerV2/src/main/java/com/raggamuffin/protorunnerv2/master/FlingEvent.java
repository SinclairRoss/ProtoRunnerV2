package com.raggamuffin.protorunnerv2.master;

public class FlingEvent 
{
	private int m_Id;
	private float m_X;
	private float m_Y;
	private long m_TimeStamp;
	
	public FlingEvent(int id, float x, float y, long timeStamp)
	{
		m_Id = id;
		m_X = x;
		m_Y = y;
		m_TimeStamp = timeStamp;
	}
	
	public int GetId()
	{
		return m_Id;
	}
	
	public float GetX()
	{
		return m_X;
	}
	
	public float GetY()
	{
		return m_Y;
	}
	
	public long GetTimeStamp()
	{
		return m_TimeStamp;
	}
}
