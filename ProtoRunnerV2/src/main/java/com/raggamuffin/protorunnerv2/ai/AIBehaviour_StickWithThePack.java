package com.raggamuffin.protorunnerv2.ai;

// Author: Sinclair Ross
// Date:   06/08/2016

import com.raggamuffin.protorunnerv2.utils.Vector3;

public class AIBehaviour_StickWithThePack extends AIBehaviour
{
    private AIBehaviour m_BackupBehaviour;
    private Vector3 m_Goal;

    public AIBehaviour_StickWithThePack(AIController controller)
    {
        super(controller);

        m_Goal = new Vector3();
        m_BackupBehaviour = new AIBehaviour_Encircle(controller);
    }

    @Override
    public Vector3 GetNavigationCoordinates()
    {
        m_Goal.SetVector(0);

        Sensor_SurroundingAwareness sensor = m_Controller.GetSituationalAwareness().GetSurroundingAwarenessSensor();

        if(sensor.GetFriendliesInNeighbourhood().size() > 0)
        {
            m_Goal.SetVector(sensor.GetCenterOfMassFriend());
        }
        else
        {
            m_Goal.SetVector(m_BackupBehaviour.GetNavigationCoordinates());
        }

        return m_Goal;
    }
}
