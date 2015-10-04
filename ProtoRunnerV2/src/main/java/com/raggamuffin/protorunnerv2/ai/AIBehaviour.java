package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class AIBehaviour
{
    protected AIController m_Controller;
    protected NavigationControl m_NavControl;
    protected SituationalAwareness m_SituationalAwareness;

    public AIBehaviour(AIController controller)
    {
        m_Controller = controller;
        m_NavControl = m_Controller.GetNavigationControl();
        m_SituationalAwareness  = m_Controller.GetSituationalAwareness();
    }

    public abstract Vector3 GetNavigationCoordinates();
}
