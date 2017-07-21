package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.master.GameActivity;
import com.raggamuffin.protorunnerv2.utils.Colour;

public class UIElement_Block extends UIElement
{

    public UIElement_Block(Colour colour, Alignment alignment)
    {
        super((alignment == Alignment.Center) ? UIElementType.Block_Centered :
                ((alignment == Alignment.Right) ? UIElementType.Block_Right : UIElementType.Block_Left));

        SetColour(colour);
    }

    public void SetScale_UseRatio(double x, double y) { super.SetScale(x * GameActivity.SCREEN_RATIO, y); }
}