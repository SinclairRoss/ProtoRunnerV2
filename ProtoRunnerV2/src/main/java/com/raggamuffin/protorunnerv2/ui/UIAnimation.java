package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   12/06/2017

public abstract class UIAnimation
{
    private UIElement m_Anchor;
    private boolean m_IsActive;

    public UIAnimation(UIElement anchor)
    {
        m_Anchor = anchor;
        m_IsActive = false;
    }

    public void Update(double deltaTime)
    {
        if(m_IsActive)
        {
            OnUpdate(deltaTime);
        }
    }

    public abstract void OnUpdate(double deltaTime);

    protected UIElement GetAnchor() {return m_Anchor; }

    public void Start() { m_IsActive = true; }
    public void Stop() { m_IsActive = false; }
}
