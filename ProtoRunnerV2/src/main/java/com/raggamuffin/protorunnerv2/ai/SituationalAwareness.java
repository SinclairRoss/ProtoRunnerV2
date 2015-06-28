package com.raggamuffin.protorunnerv2.ai;

import java.util.Vector;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.weapons.Projectile;

public class SituationalAwareness
{
	private AIController m_Controller;
	
	///// Sensors.
	private Sensor_SurroundingAwareness m_SurroundingAwarenessSensor;
	private Sensor_Target m_TargetSensor;
	private Sensor_IncomingDanger m_DangerSensor;

	public SituationalAwareness(AIController controller, VehicleManager vManager, BulletManager bManager)
	{
		m_Controller = controller;
		
		Vehicle anchor = m_Controller.GetAnchor();
		
		///// Sensors.
		m_SurroundingAwarenessSensor 	= new Sensor_SurroundingAwareness(anchor, vManager);
		m_TargetSensor					= new Sensor_Target(m_Controller, vManager);
		m_DangerSensor					= new Sensor_IncomingDanger(m_Controller, bManager);
	}
	
	public void Update()
	{
		m_SurroundingAwarenessSensor.Update();
		m_TargetSensor.Update();
		m_DangerSensor.Update();
	}
	
	public Vector<Vehicle> GetSurroundingVehicles()
	{
		return m_SurroundingAwarenessSensor.GetVehiclesInNeighbourhood();
	}
	
	public Vehicle GetTarget()
	{
		return m_TargetSensor.GetTarget();
	}
	
	public Vector<Projectile> GetIncomingProjectiles()
	{
		return m_DangerSensor.GetIncomingProjectiles();
	}
}
