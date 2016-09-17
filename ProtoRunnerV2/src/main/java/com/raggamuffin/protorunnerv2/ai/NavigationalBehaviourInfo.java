package com.raggamuffin.protorunnerv2.ai;

// Author: Sinclair Ross
// Date:   15/08/2016

public class NavigationalBehaviourInfo
{
    private final double m_GoalWeight;
    private final double m_SeperationWeight;
    private final double m_AlignmentWeight;
    private final double m_CohesionWeight;

    public NavigationalBehaviourInfo(double goalWeight, double seperationWeight, double alignmentWeight, double cohesionWeight)
    {
        m_GoalWeight = goalWeight;
        m_SeperationWeight = seperationWeight;
        m_AlignmentWeight = alignmentWeight;
        m_CohesionWeight = cohesionWeight;
    }

    public double GetGoalWeight()
    {
        return m_GoalWeight;
    }

    public double GetSeperationWeight()
    {
        return m_SeperationWeight;
    }

    public double GetAlignmentWeight()
    {
        return m_AlignmentWeight;
    }

    public double GetCohesionWeight()
    {
        return m_CohesionWeight;
    }
}
