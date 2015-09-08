package com.raggamuffin.protorunnerv2.ai;

import java.util.ArrayList;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Sensor_Target extends Sensor
{
	private Vehicle m_Target;
	private ArrayList<Vehicle> m_Targets;
	private Vector3 m_ToTarget;
	
	public Sensor_Target(AIController Controller, VehicleManager VManager) 
	{
		super(Controller.GetAnchor(), 50.0);
		
		m_Target 	= null;
		m_Targets 	= VManager.GetTeam(Controller.GetEnemyAffiliation());
		m_ToTarget 	= new Vector3();
	}

	@Override
	public void Update() 
	{
		double utility = 0.0;
		double highestUtility = Double.MIN_VALUE;
		
		m_Target = null;

		for(Vehicle possibleTarget : m_Targets)
		{
			m_ToTarget.SetVectorDifference(m_Anchor.GetPosition(), possibleTarget.GetPosition());
			
			// Phase 1: Calculate utility based on distance from target.
			double DistanceSqr = m_ToTarget.GetLengthSqr();
			utility += MathsHelper.Normalise(DistanceSqr, 0.0, m_SensorRadius);
			
			// Phase 2: Calculate utility base on direction to target
			//double deltaHeading = Vector3.RadiansBetween(m_Anchor.GetForward(), m_ToTarget);
			//utility += (1.0 - MathsHelper.Normalise(deltaHeading, 0.0, m_SensorArc));
			
			if(utility > highestUtility)
			{
				highestUtility 	= utility;
				m_Target 		= possibleTarget;
				m_VehicleState.SetTarget(m_Target);
			}
		}
	}

	public Vehicle GetTarget()
	{
		return m_Target;
	}
}