package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.renderer.ModelType;

public class TargetBot extends Vehicle
{
    public TargetBot(GameLogic game)
    {
        super(game);

        m_Model = ModelType.Dummy;
    }
}
