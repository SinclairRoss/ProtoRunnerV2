package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colour;

public class BoundingRing extends GameObject
{
    public BoundingRing(GameLogic game, double radius, Colour colour)
    {
        super(game, ModelType.Ring);

        m_Colour = colour;
        m_Scale.SetVector(radius);
    }

    @Override
    public void Update(double DeltaTime)
    {
        // Do nothing.
    }

    @Override
    public boolean IsValid()
    {
        return true;
    }
}
