package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   13/06/2017

public class UIElement_Radial extends UIElement
{
    private static double LINE_WIDTH = 30;

    private double m_Progress;

    public UIElement_Radial()
    {
        super(UIElementType.Radial);

        m_Progress = 0.0;
    }

    public double GetLineWidth() { return LINE_WIDTH; }

    public double GetProgress() { return m_Progress; }
    public void SetProgress(double progress) { m_Progress = progress; }
}
