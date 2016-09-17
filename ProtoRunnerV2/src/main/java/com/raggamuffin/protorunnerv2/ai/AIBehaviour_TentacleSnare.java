package com.raggamuffin.protorunnerv2.ai;

// Author: Sinclair Ross
// Date:   13/09/2016

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class AIBehaviour_TentacleSnare extends AIBehaviour
{
    private Vector3 m_Goal;

    public AIBehaviour_TentacleSnare(AIController controller)
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
