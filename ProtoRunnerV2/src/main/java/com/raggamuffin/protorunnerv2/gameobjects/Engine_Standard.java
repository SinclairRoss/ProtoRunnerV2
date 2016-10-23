package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.TrailEmitter;

public class Engine_Standard extends Engine
{
    public Engine_Standard(GameObject anchor, GameLogic game)
    {
        super(game, anchor);

        m_Anchor.AddObjectToGameObjectManager(new TrailEmitter(game, m_Anchor));
    }
}