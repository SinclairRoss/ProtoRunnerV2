package com.raggamuffin.protorunnerv2.ObjectEffect;

// Author: Sinclair Ross
// Date:   01/06/2017

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;

public class ObjectEffect_SpawnPillar extends ObjectEffect
{
    private final double MAX_HEIGHT = 150.0;
    private final double MAX_WIDTH = 2.0;

    private Timer m_LifeTimer;

    public ObjectEffect_SpawnPillar()
    {
        super(ModelType.SpawnPillar, ObjectEffectType.SpawnPillar);

        m_LifeTimer = new Timer(0.7);
    }

    @Override
    public void Initialise(Vehicle anchor)
    {
        super.Initialise(anchor);

        SetColour(GetAnchor().GetColour());
        SetPosition(GetAnchor().GetPosition());
        SetScale(0);

        m_LifeTimer.Start();
    }

    @Override
    public void Update(double deltaTime)
    {
        double progress = m_LifeTimer.GetProgress();

        SetPosition(GetAnchor().GetPosition());

        double scaleWidth = MathsHelper.Lerp(1.0 - progress, 0, MAX_WIDTH);
        double scaleHeight = MathsHelper.Lerp(progress, 0, MAX_HEIGHT);
        SetScale(scaleWidth, scaleHeight, scaleWidth);

        double alpha = MathsHelper.Normalise(1.0 - progress, 0.0, 0.4);
        SetAlpha(alpha);
    }

    @Override
    public boolean IsValid()
    {
        return m_LifeTimer.HasElapsed();
    }
}
