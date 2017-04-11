package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   24/09/2016

import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Timer_Accumulation;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class SpawnEffect extends GameObject
{
    private final double MAX_HEIGHT = 150.0;
    private final double MAX_WIDTH = 2.0;

    private Timer m_LifeTimer;

    public SpawnEffect(Colour colour, Vector3 pos)
    {
        super(ModelType.SpawnPillar, 1.0);

        SetColour(colour);
        SetPositionByRef(pos);
        SetScale(0);

        m_LifeTimer = new Timer(0.7);
    }

    @Override
    public void Update(double deltaTime)
    {
        double progress = m_LifeTimer.GetProgress();

        double scaleWidth = MathsHelper.Lerp(1.0 - progress, 0, MAX_WIDTH);
        double scaleHeight = MathsHelper.Lerp(progress, 0, MAX_HEIGHT);
        SetScale(scaleWidth, scaleHeight, scaleWidth);

        double alpha = MathsHelper.Normalise(1.0 - progress, 0.0, 0.4);
        SetAlpha(alpha);
    }

    @Override
    public boolean IsValid()
    {
        return !m_LifeTimer.HasElapsed();
    }

    @Override
    public void CleanUp()
    {}
}
