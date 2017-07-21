package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   07/06/2017

public class UIElement_Chevron extends UIElement
{
    private final static double LINE_WIDTH = 50;

    public UIElement_Chevron()
    {
        super(UIElementType.Chevron);
    }

    public double GetLineWidth() { return LINE_WIDTH; }
}
