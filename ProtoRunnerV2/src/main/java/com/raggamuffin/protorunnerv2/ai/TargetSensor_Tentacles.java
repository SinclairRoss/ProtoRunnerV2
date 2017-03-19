package com.raggamuffin.protorunnerv2.ai;

// Author: Sinclair Ross
// Date:   17/09/2016

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.StatusEffect;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.ArrayList;

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
                double distanceToClosestTargetSqr = Double.MAX_VALUE;

                int numTargets = m_Targets.size();
                for(int i = 0; i < numTargets; ++i)
                {
                    Vehicle friendly = m_Targets.get(i);

                    if(IsValidTarget(friendly))
                    {
                        double distanceToFriendlySqr = Vector3.DistanceBetweenSqr(m_Anchor.GetPosition(), friendly.GetPosition());

                        if(distanceToFriendlySqr < distanceToClosestTargetSqr)
                        {
                            target = friendly;
                            distanceToClosestTargetSqr = distanceToFriendlySqr;
                        }
                    }
                }
            }
        }

        return target;
    }

    private boolean IsValidTarget(Vehicle target)
    {
        boolean isValidTarget = (target.CanBeTargeted() &&
                target.GetModel() != ModelType.ShieldBearer &&
                !target.HasStatusEffect(StatusEffect.Shielded));

        return isValidTarget;
    }
}
