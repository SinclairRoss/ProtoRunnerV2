package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;

public class SituationalAwareness
{
	///// Sensors.
	private Sensor_SurroundingAwareness m_SurroundingAwarenessSensor;
	private TargetSensor m_TargetSensor;
	private Sensor_IncomingDanger m_DangerSensor;

	public SituationalAwareness(AIController controller, VehicleManager vManager, BulletManager bManager, TargetingBehaviour targetingBehaviour)
	{
		///// Sensors.
		m_SurroundingAwarenessSensor = new Sensor_SurroundingAwareness(controller.GetAnchor(), vManager);
        m_TargetSensor = CreateTargetSensor(controller, vManager, targetingBehaviour);
		m_DangerSensor = new Sensor_IncomingDanger(controller, bManager);
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
	
    public TargetSensor GetTargetSensor()
    {
        return m_TargetSensor;
    }

    public Sensor_IncomingDanger GetDangerSensor()
    {
        return m_DangerSensor;
    }

	private TargetSensor CreateTargetSensor(AIController controller, VehicleManager vManager, TargetingBehaviour targetingBehaviour)
	{
		switch (targetingBehaviour)
		{
			case Standard:
				return new TargetSensor_Standard(controller, vManager);
			case Tentacle:
                return new TargetSensor_Tentacles(controller, vManager);
            case None:
                return new TargetSensor_None(controller, vManager);
            default:
                return null;
		}
	}
}
