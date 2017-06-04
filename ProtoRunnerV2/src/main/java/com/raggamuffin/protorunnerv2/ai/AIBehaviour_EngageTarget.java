package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.gameobjects.StatusEffect;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class AIBehaviour_EngageTarget extends AIBehaviour
{
    private Vector3 m_Goal;

    public AIBehaviour_EngageTarget(AIController controller)
    {
        super(controller);

        m_Goal = new Vector3();
    }

    @Override
    public Vector3 GetNavigationCoordinates()
    {
        m_Goal.SetVector(0);

        Vehicle target = m_Controller.GetSituationalAwareness().GetTargetSensor().GetTarget();

        if(target != null)
        {
            m_Goal.SetVector(target.GetPosition());
        }

        return m_Goal;
    }
}
