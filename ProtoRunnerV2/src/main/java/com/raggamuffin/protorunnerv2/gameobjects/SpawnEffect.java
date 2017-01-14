package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   24/09/2016

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer_Accumulation;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class SpawnEffect extends GameObject
{
    private final double MAX_HEIGHT = 150.0;
    private final double MAX_WIDTH = 2.0;

    private Timer_Accumulation m_LifeTimer;

    public SpawnEffect(GameLogic game, Colour colour, Vector3 pos)
    {
        super(game, ModelType.SpawnPillar);

        m_Colour.SetColour(colour);
        m_Position = pos;

        m_LifeTimer = new Timer_Accumulation(0.7);

        m_Scale.SetVector(0);
    }

    @Override
    public void Update(double deltaTime)
    {
        m_LifeTimer.Update(deltaTime);
        double progress = m_LifeTimer.GetProgress();

        m_Scale.I = MathsHelper.Lerp(1.0 - progress, 0, MAX_WIDTH);
        m_Scale.J = MathsHelper.Lerp(progress, 0, MAX_HEIGHT);
        m_Scale.K = m_Scale.I;

        double val = MathsHelper.Normalise(1.0 - progress, 0.0, 0.4);
        m_Colour.Alpha = MathsHelper.Lerp(val, 0, 1);
    }

    @Override
    public boolean IsValid()
    {
        return !m_LifeTimer.TimedOut();
    }

    @Override
    public void CleanUp()
    {}
}
