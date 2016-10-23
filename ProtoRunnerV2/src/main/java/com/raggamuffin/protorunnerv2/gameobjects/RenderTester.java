package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   27/09/2016

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.renderer.ModelType;

public class RenderTester extends GameObject
{
    public RenderTester(GameLogic game, ModelType modelType)
    {
        super(game, modelType);
    }

    @Override
    public boolean IsValid()
    {
        return !m_ForciblyInvalidated;
    }

    @Override
    public void CleanUp()
    {}
}
