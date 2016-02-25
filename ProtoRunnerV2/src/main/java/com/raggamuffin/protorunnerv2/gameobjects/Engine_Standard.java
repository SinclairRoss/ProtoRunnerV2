package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.TrailEmitter;
import com.raggamuffin.protorunnerv2.utils.Colour;

public class Engine_Standard extends Engine
{
    public Engine_Standard(GameObject anchor, GameLogic game)
    {
        super(game, anchor);

        m_Anchor.AddChild(new TrailEmitter(m_Anchor, game));
    }
}
