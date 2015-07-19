package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class Goal
{
    protected AIController m_Controller;
    protected NavigationControl m_NavControl;
    protected SituationalAnalysis m_SituationalAnalysis;
    protected SituationalAwareness m_SituationalAwareness;

    public Goal(AIController controller)
    {
        m_Controller = controller;
        m_NavControl = m_Controller.GetNavigationControl();
        m_SituationalAnalysis   = m_Controller.GetSituationalAnalysis();
        m_SituationalAwareness  = m_Controller.GetSituationalAwareness();
    }

    public abstract double CalculateUtility();
    public abstract Vector3 GetGoalCoords();
}
