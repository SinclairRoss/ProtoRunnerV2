package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class AIBehaviour_FollowTheLeader extends AIBehaviour
{
    private AIBehaviour m_EngageTargetBehaviour;
    private Vector3 m_Goal;

    public AIBehaviour_FollowTheLeader(AIController controller)
    {
        super(controller);

        m_EngageTargetBehaviour = new AIBehaviour_EngageTarget(controller);
        m_Goal = new Vector3();
    }

    @Override
    public Vector3 GetNavigationCoordinates()
    {
        // If vehicle has no leader, fall back to engage target behaviour;
        if (m_Controller.GetLeader() != null)
        {
            m_Goal.SetVector(0);
            Vehicle target = m_Controller.GetLeader();
            m_Goal.SetVector(target.GetPosition());

            return m_Goal;
        }

        return m_EngageTargetBehaviour.GetNavigationCoordinates();
    }
}
