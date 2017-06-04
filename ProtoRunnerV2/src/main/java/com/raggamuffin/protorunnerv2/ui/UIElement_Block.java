package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.managers.UIManager;

public class UIElement_Block extends UIElement
{

    public UIElement_Block(double[] colour, UIManager uiManager, Alignment alignment)
    {
        super((alignment == Alignment.Center) ? UIElementType.Block_Centered :
                ((alignment == Alignment.Right) ? UIElementType.Block_Right : UIElementType.Block_Left) , uiManager);

        SetColour(colour);
    }

    @Override
    public void Update(double deltaTime)
    {}

    public void SetScale_UseRatio(double x, double y) { super.SetScale(x * SCREEN_RATIO, y); }
}