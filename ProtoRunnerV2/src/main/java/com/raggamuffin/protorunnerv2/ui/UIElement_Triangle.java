package com.raggamuffin.protorunnerv2.ui;


// Author: Sinclair Ross
// Date:   04/06/2017


import com.raggamuffin.protorunnerv2.managers.UIManager;

public class UIElement_Triangle extends UIElement
{
    private double m_LineWidth;

    public UIElement_Triangle(double lineWidth, UIManager uiManager)
    {
        super(UIElementType.Triangle, uiManager);

        m_LineWidth = lineWidth;
    }

    @Override
    public void Update(double deltaTime)
    {}

    public double GetLineWidth() { return m_LineWidth; }
}
