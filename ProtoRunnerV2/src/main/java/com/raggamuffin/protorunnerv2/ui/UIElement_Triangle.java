package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   04/06/2017

public class UIElement_Triangle extends UIElement
{
    private double m_LineWidth;

    public UIElement_Triangle(double lineWidth)
    {
        super(UIElementType.Triangle);

        m_LineWidth = lineWidth;
    }

    public double GetLineWidth() { return m_LineWidth; }
}
