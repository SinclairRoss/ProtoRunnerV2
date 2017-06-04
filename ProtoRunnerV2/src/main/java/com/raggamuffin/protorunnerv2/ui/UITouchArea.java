package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   04/06/2017

public class UITouchArea
{
    private double m_Top;
    private double m_Bottom;
    private double m_Left;
    private double m_Right;

    public UITouchArea(double top, double bottom, double left, double right)
    {
        m_Top = top;
        m_Bottom = bottom;
        m_Left = left;
        m_Right = right;
    }

    public double Top() { return m_Top; }
    public double Bottom() { return m_Bottom; }
    public double Left() { return m_Left; }
    public double Right() { return m_Right; }
}
