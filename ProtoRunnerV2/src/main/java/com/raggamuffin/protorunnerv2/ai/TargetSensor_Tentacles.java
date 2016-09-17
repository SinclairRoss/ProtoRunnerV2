package com.raggamuffin.protorunnerv2.ai;

// Author: Sinclair Ross
// Date:   17/09/2016

import com.raggamuffin.protorunnerv2.gameobjects.StatusEffect;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class TargetSensor_Tentacles extends TargetSensor
{
    AIController m_Controller;

    public TargetSensor_Tentacles(AIController controller, VehicleManager vManager)
    {
        super(controller, vManager, controller.GetTeamAffiliationKey());

        m_Controller = controller;
    }

    @Override
    protected Vehicle FindTarget()
    {
        Vehicle target = null;

        Vehicle leader = m_Controller.GetLeader();

        if(leader != null)
        {
            if (m_Targets.size() > 0)
            {
                Vector3 leaderPosition = leader.GetPosition();
                double distanceToClosestTargetSqr = Double.MAX_VALUE;

                for (Vehicle friendly : m_Targets)
                {
                    if (friendly.CanBeTargeted() &&
                            !friendly.HasStatusEffect(StatusEffect.Shielded) &&
                                friendly != leader)
                    {
                        double distanceToEnemySqr = Vector3.DistanceBetweenSqr(leaderPosition, friendly.GetPosition());

                        if (distanceToEnemySqr < distanceToClosestTargetSqr)
                        {
                            target = friendly;
                        }
                    }
                }
            }
        }

        return target;
    }
}
