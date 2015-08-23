package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.vehicles.Vehicle;

public abstract class Sensor 
{
	protected Vehicle m_Anchor;
	protected VehicleInfo m_VehicleState;
	protected double  m_SensorRadius;
	
	public Sensor(Vehicle anchor, double sensorRadius)
	{
		m_Anchor 		= anchor;
		m_VehicleState	= m_Anchor.GetVehicleInfo();
		m_SensorRadius 	= sensorRadius;
	}
	
	public abstract void Update();
}