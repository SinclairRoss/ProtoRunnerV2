package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class AIBehaviour_Encircle extends AIBehaviour
{
    private Vector3 m_Goal;

    public AIBehaviour_Encircle(AIController controller)
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
            Vector3 pos = m_Controller.GetAnchor().GetPosition();

            m_Goal.SetVectorDifference(target.GetPosition(), pos);
            m_Goal.Normalise();
            m_Goal.Scale(20);
            m_Goal.Add(target.GetPosition());
        }

        return m_Goal;
    }
}
