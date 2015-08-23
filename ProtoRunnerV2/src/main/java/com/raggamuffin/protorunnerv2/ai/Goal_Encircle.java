package com.raggamuffin.protorunnerv2.ai;

import android.util.Log;

import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Goal_Encircle extends Goal
{
    private double m_Rotation;

    private Vector3 m_Goal;
    private Vector3 m_Position;
    private Vector3 m_CenterOfMass;

    public Goal_Encircle(AIController controller)
    {
        super(controller);

        m_Rotation = Math.toRadians(5);

        m_Goal = new Vector3();
        m_Position = m_Controller.GetAnchor().GetPosition();
        m_CenterOfMass = m_SituationalAwareness.GetSurroundingAwarenessSensor().GetCenterOfMassEnemy();
    }

    @Override
    public double CalculateUtility()
    {
        return 0;
    }

    @Override
    public Vector3 GetGoalCoords()
    {
        // Change direction randomly.
        if(MathsHelper.RandomDouble(0,1) < 0.01)
            m_Rotation *= -1;

        m_Goal.SetVectorDifference(m_CenterOfMass, m_Position);
        m_Goal.RotateY(m_Rotation);
        m_Goal.Normalise();
        m_Goal.Scale(30);

        return m_Goal;
    }
}
