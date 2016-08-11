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

	public Sensor_Target(AIController controller, VehicleManager vManager)
	{
		super(controller.GetAnchor(), 1000.0);

		m_Targets 	= vManager.GetOpposingTeam(controller.GetAnchor().GetAffiliation());
		m_ToTarget 	= new Vector3();
	}

    public Vehicle FindTarget()
    {
        Vehicle target = null;

        if (m_Targets.size() > 0)
        {
            Vector3 anchorPosition = m_Anchor.GetPosition();
            double distanceToClosestTargetSqr = Double.MAX_VALUE;

            for(Vehicle enemy : m_Targets)
            {
                if(enemy.CanBeTargeted())
                {
                    m_ToTarget.SetVectorDifference(anchorPosition, enemy.GetPosition());
                    double distanceToEnemySqr = m_ToTarget.GetLengthSqr();

                    if(distanceToEnemySqr < distanceToClosestTargetSqr)
                    {
                        target = enemy;
                    }
                }
            }
        }

        return target;
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