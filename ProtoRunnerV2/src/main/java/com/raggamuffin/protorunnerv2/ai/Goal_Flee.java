package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Goal_Flee extends Goal
{
    private Vehicle m_Anchor;
    private Vector3 m_TempVector;
    private Vector3 m_FleeVector;

    public Goal_Flee(AIController controller)
    {
        super(controller);

        m_Anchor = m_Controller.GetAnchor();
        m_TempVector = new Vector3();
        m_FleeVector = new Vector3();
    }

    @Override
    public double CalculateUtility()
    {
        return 1 - m_SituationalAnalysis.GetBravery();
    }

    @Override
    public Vector3 GetGoalCoords()
    {
        Vector3 enemyMass = m_SituationalAwareness.GetSurroundingAwarenessSensor().GetCenterOfMassEnemy();
        Vector3 pos = m_Anchor.GetPosition();

        m_TempVector.SetVectorDifference(enemyMass, pos);
        m_TempVector.Normalise();

        m_FleeVector.SetVector(pos);
        m_FleeVector.Add(m_TempVector);
        return m_FleeVector;
    }
}
