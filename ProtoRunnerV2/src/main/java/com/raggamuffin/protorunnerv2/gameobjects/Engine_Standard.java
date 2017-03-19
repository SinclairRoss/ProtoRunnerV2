package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.TrailEmitter;

public class Engine_Standard extends Engine
{
    TrailEmitter m_TrailEmitter;
    public Engine_Standard(Vehicle anchor, GameLogic game)
    {
        super(game, anchor);

        m_TrailEmitter = new TrailEmitter(game, m_Anchor);
    }

    @Override
    public void Update(double deltaTime)
    {
        super.Update(deltaTime);

        m_TrailEmitter.Update();
    }
}