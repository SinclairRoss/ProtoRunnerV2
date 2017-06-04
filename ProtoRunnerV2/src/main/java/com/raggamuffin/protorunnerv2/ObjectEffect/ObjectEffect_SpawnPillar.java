package com.raggamuffin.protorunnerv2.ObjectEffect;

// Author: Sinclair Ross
// Date:   01/06/2017

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class ObjectEffect_SpawnPillar extends ObjectEffect
{
    private final double MAX_HEIGHT = 150.0;
    private final double MAX_WIDTH = 2.0;

    public ObjectEffect_SpawnPillar()
    {
        super(ModelType.SpawnPillar, ObjectEffectType.SpawnPillar, 0.7);
    }

    @Override
    public void Initialise(GameObject anchor)
    {
        super.Initialise(anchor);

        SetColour(GetAnchor().GetColour());
        SetPosition(GetAnchor().GetPosition());
        SetScale(0);
    }

    @Override
    public void Update(double deltaTime)
    {
        double progress = GetProgress();

        SetPosition(GetAnchor().GetPosition());

        double scaleWidth = MathsHelper.Lerp(1.0 - progress, 0, MAX_WIDTH);
        double scaleHeight = MathsHelper.Lerp(progress, 0, MAX_HEIGHT);
        SetScale(scaleWidth, scaleHeight, scaleWidth);

        double alpha = MathsHelper.Normalise(1.0 - progress, 0.0, 0.4);
        SetAlpha(alpha);
    }
}
