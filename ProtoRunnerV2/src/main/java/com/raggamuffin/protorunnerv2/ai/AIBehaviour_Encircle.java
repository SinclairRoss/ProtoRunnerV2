package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class AIBehaviour_Encircle extends AIBehaviour
{
    private Vector3 m_Goal;
    private Vector3 m_Position;

    public AIBehaviour_Encircle(AIController controller)
    {
        super(controller);

        m_Goal = new Vector3();
        m_Position = m_Controller.GetAnchor().GetPosition();
    }

    @Override
    public Vector3 GetNavigationCoordinates()
    {
        m_Goal.SetVector(0,0,0);

        Vehicle target = m_Controller.GetSituationalAwareness().GetTargetSensor().GetTarget();

        if(target != null)
        {
            m_Goal.SetVectorDifference(target.GetForward(), m_Position);
            m_Goal.Normalise();
            m_Goal.Scale(20);
        }

        return m_Goal;
    }
}
