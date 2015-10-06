package com.raggamuffin.protorunnerv2.ai;

import java.util.ArrayList;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.gameobjects.Wingman;
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
        double utility = 0.0;
        double highestUtility = Double.MIN_VALUE;

        m_Target = null;

        for(Vehicle possibleTarget : m_Targets)
        {
            if(!possibleTarget.CanBeTargeted())
                continue;

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

        return m_Target;
    }

    public Vehicle FindLeaderCompatibleTarget()
    {
        Vehicle leader = m_Controller.GetLeader();

        double utility = 0.0;
        double highestUtility = 0;

        m_Target = null;

        for(Vehicle possibleTarget : m_Targets)
        {
            m_ToTarget.SetVectorDifference(leader.GetPosition(), possibleTarget.GetPosition());

            // Phase 1: Calculate utility based on distance from target.
            double DistanceSqr = m_ToTarget.GetLengthSqr();
            utility += MathsHelper.Normalise(DistanceSqr, 0.0, m_SensorRadius);

            // Phase 2: Make sure target is infront of leader.
            double deltaHeading = Vector3.RadiansBetween(m_Anchor.GetForward(), m_ToTarget);

            if(deltaHeading > m_LeaderTolerance)
                continue;

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
      //  if(m_Controller.GetLeader() == null)
            m_Target = FindTarget();
    //    else
    //        m_Target = FindLeaderCompatibleTarget();
    }

    public Vehicle GetTarget()
    {
        return m_Target;
    }
}