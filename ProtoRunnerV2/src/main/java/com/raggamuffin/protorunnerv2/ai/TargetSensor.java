package com.raggamuffin.protorunnerv2.ai;

import java.util.ArrayList;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class TargetSensor extends Sensor
{
    protected Vehicle m_Target;

    protected ArrayList<Vehicle> m_Targets;

	public TargetSensor(AIController controller, VehicleManager vManager, AffiliationKey targetedTeam)
	{
		super(controller.GetAnchor(), 1000.0);

		m_Targets = vManager.GetTeam(targetedTeam);
	}

    protected abstract Vehicle FindTarget();

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