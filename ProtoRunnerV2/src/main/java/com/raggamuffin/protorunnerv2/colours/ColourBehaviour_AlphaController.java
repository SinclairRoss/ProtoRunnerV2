package com.raggamuffin.protorunnerv2.colours;

// Author: Sinclair Ross
// Date:   10/08/2016
// Note:   Used as a way to control the transparency of an object while working within the
//         bounds of the colour management system.

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class ColourBehaviour_AlphaController extends ColourBehaviour
{

    public ColourBehaviour_AlphaController(GameObject Anchor, ActivationMode Mode)
    {
        super(Anchor, Mode);
    }

    @Override
    protected void UpdateBehaviour(double DeltaTime)
    {
        // Do nothing.
    }

    public void SetAlpha(double alpha)
    {
        m_DeltaColour.W = -(1.0 - MathsHelper.Clamp(alpha, 0.0, 1.0));
    }
}
