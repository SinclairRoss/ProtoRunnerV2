package com.raggamuffin.protorunnerv2.ai;

import java.util.ArrayList;
import java.util.Vector;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.weapons.Projectile;

public class SituationalAwareness
{
	///// Sensors.
	private Sensor_SurroundingAwareness m_SurroundingAwarenessSensor;
	private Sensor_Target m_TargetSensor;
	private Sensor_IncomingDanger m_DangerSensor;

	public SituationalAwareness(AIController controller, VehicleManager vManager, BulletManager bManager)
	{
		///// Sensors.
		m_SurroundingAwarenessSensor 	= new Sensor_SurroundingAwareness(controller.GetAnchor(), vManager);
		m_TargetSensor					= new Sensor_Target(controller, vManager);
		m_DangerSensor					= new Sensor_IncomingDanger(controller, bManager);
	}
	
	public void Update()
	{
		m_SurroundingAwarenessSensor.Update();
		m_TargetSensor.Update();
		m_DangerSensor.Update();
	}

    public Sensor_SurroundingAwareness GetSurroundingAwarenessSensor()
    {
        return m_SurroundingAwarenessSensor;
    }
	
    public Sensor_Target GetTargetSensor()
    {
        return m_TargetSensor;
    }

    public Sensor_IncomingDanger GetDangerSensor()
    {
        return m_DangerSensor;
    }
}
