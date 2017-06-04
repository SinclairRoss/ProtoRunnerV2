package com.raggamuffin.protorunnerv2.utils;

// Author: Sinclair Ross
// Date:   03/06/2017

import com.raggamuffin.protorunnerv2.master.GameActivity;

public final class UIHelpers
{
    private UIHelpers()
    {}

    public static void TouchToScreenCoords(double x, double y, Vector2 out)
    {
        x = MathsHelper.Normalise(x, 0, GameActivity.SCREEN_SIZE.x);
        x = MathsHelper.Lerp(x, -GameActivity.SCREEN_RATIO, GameActivity.SCREEN_RATIO);

        y = MathsHelper.Normalise(y, 0, GameActivity.SCREEN_SIZE.y);
        y = MathsHelper.Lerp(y, 1, -1);

        out.SetVector(x, y);
    }
}
