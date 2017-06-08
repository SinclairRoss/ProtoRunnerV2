package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   07/06/2017

import com.raggamuffin.protorunnerv2.managers.UIManager;

public class UIElement_Chevron extends UIElement
{
    private final static double LINE_WIDTH = 50;

    public UIElement_Chevron(UIManager uiManager)
    {
        super(UIElementType.Chevron, uiManager);
    }

    @Override
    public void Update(double deltaTime)
    {

    }

    public double GetLineWidth() { return LINE_WIDTH; }
}
