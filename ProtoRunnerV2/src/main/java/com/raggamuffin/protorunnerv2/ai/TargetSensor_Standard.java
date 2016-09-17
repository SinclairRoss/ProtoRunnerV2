package com.raggamuffin.protorunnerv2.ai;

// Author: Sinclair Ross
// Date:   17/09/2016

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class TargetSensor_Standard extends TargetSensor
{
    public TargetSensor_Standard(AIController controller, VehicleManager vManager)
    {
        super(controller, vManager, controller.GetEnemyAffiliationKey());
    }

    @Override
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
                    double distanceToEnemySqr = Vector3.DistanceBetweenSqr(anchorPosition, enemy.GetPosition());

                    if(distanceToEnemySqr < distanceToClosestTargetSqr)
                    {
                        target = enemy;
                    }
                }
            }
        }

        return target;
    }
}
