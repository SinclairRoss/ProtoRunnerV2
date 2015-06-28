package com.raggamuffin.protorunnerv2.ai;

import java.util.Vector;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Sensor_SurroundingAwareness extends Sensor
{	
	private Vector<Vehicle> m_VehiclesInWorld;
	private Vector<Vehicle> m_VehiclesInNeighbourhood;
	
	private Vector3 m_ToVehicle;
	
	public Sensor_SurroundingAwareness(Vehicle Anchor, VehicleManager VManager) 
	{
		super(Anchor, 10.0);
		
		m_VehiclesInWorld = VManager.GetVehicles();
		m_VehiclesInNeighbourhood = new  Vector<Vehicle>();

		m_ToVehicle = new Vector3();
	}

	@Override
	public void Update() 
	{
		// Clear the vector containing the minions in the neighbourhood.
		m_VehiclesInNeighbourhood.clear();
		
		// Iterate through all minions.
		for(Vehicle Object : m_VehiclesInWorld)
		{
			// Skips this iteration if the minion is the vehicle this sensor is attached to.
			if(Object == m_Anchor)	
			{
				continue;					
			}
				
			// Sets a vector from the anchor to the vehicle being investigated.
			m_ToVehicle.SetVectorDifference(m_Anchor.GetPosition(), Object.GetPosition());
			
			// If the other vehicle is within sensor range add it to the neighbourhood.
			if(m_ToVehicle.GetLengthSqr() <= (m_SensorRadius * m_SensorRadius))
			{
				m_VehiclesInNeighbourhood.add(Object);
			}
		}
	}
	
	public Vector<Vehicle> GetVehiclesInNeighbourhood()
	{
		return m_VehiclesInNeighbourhood;
	}
}
