package com.raggamuffin.protorunnerv2.ai;

public abstract class Action
{
    protected AIController m_Controller;

    public Action(AIController controller)
    {
        m_Controller = controller;
    }

    public abstract void Update(double deltaTime);
    public abstract double CalculateUtility();
}
