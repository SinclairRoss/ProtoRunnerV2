package com.raggamuffin.protorunnerv2.utils;

public class CollisionReport 
{
	private Vector3 m_CollisionPointA;
	private Vector3 m_CollisionPointB;
	
	public CollisionReport()
	{
		m_CollisionPointA = new Vector3();
		m_CollisionPointB = new Vector3();
	}
	
	public Vector3 GetCollisionPointA()
	{
		return m_CollisionPointA;
	}
	
	public Vector3 GetCollisionPointB()
	{
		return m_CollisionPointB;
	}
}
