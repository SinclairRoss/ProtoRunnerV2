package com.raggamuffin.protorunnerv2.RenderObjects;

// Author: Sinclair Ross
// Date:   20/08/2017

import com.raggamuffin.protorunnerv2.ObjectEffect.ObjectEffect_HealthBar;

public class RenderObject_HealthBar extends RenderObject
{
    private double m_Progress;
    private double m_LineWidth;

    public RenderObject_HealthBar()
    {
        super();

        m_Progress = 0.0;
        m_LineWidth = 0.0;
    }

    public void SetupForHealthBar(ObjectEffect_HealthBar obj)
    {
        SetupForObject(obj);

        m_Progress = obj.Progress();
        m_LineWidth = obj.GetLineWidth();
    }

    public double GetProgress() { return m_Progress; }
    public double GetLineWidth() { return m_LineWidth; }
}
