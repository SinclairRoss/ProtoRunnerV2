package com.raggamuffin.protorunnerv2.ai;

// Author: Sinclair Ross
// Date:   17/09/2016

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;

public class TargetSensor_None extends TargetSensor
{
    public TargetSensor_None(AIController controller, VehicleManager vManager)
    {
        super(controller, vManager, AffiliationKey.Neutral);
    }

    @Override
    protected Vehicle FindTarget()
    {
        return null;
    }
}
