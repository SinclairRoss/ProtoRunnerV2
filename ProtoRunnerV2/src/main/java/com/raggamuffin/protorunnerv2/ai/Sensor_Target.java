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
    private AIController m_Controller;

    private double m_LeaderTolerance;
	
	public Sensor_Target(AIController controller, VehicleManager vManager)
	{
		super(controller.GetAnchor(), 1000.0);

        m_Controller = controller;

		m_Target 	= null;
		m_Targets 	= vManager.GetTeam(m_Controller.GetEnemyAffiliation());
		m_ToTarget 	= new Vector3();

        m_LeaderTolerance = Math.toRadians(45);
	}

    public Vehicle FindTarget()
    {
        m_Target = null;
        double highestUtility = Double.MIN_VALUE;

        for(Vehicle possibleTarget : m_Targets)
        {
            if(!possibleTarget.CanBeTargeted())
                continue;

            double utility = 0.0;

            m_ToTarget.SetVectorDifference(m_Anchor.GetPosition(), possibleTarget.GetPosition());

            // Phase 1: Calculate utility based on distance from target.
            double DistanceSqr = m_ToTarget.GetLengthSqr();
            utility += 1.0 - MathsHelper.Normalise(DistanceSqr, 0.0, m_SensorRadius * m_SensorRadius);

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

        return m_Target;
    }

    @Override
    public void Update()
    {
            m_Target = FindTarget();
    }

    public Vehicle GetTarget()
    {
        return m_Target;
    }
}