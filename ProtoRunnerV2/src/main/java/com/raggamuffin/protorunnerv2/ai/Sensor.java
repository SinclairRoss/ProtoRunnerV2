package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public abstract class Sensor 
{
	protected Vehicle m_Anchor;
	protected double  m_SensorRadius;
	
	public Sensor(Vehicle anchor, double sensorRadius)
	{
		m_Anchor 		= anchor;
		m_SensorRadius 	= sensorRadius;
	}
	
	public abstract void Update();
}