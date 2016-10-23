package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colour;

public class BoundingRing extends GameObject
{
    public GameObject m_Anchor;

    public BoundingRing(GameLogic game, GameObject anchor, double radius, Colour colour)
    {
        super(game, ModelType.Ring);

        m_Anchor = anchor;
        m_Colour = colour;
        m_Scale.SetVector(radius);
    }

    @Override
    public void Update(double DeltaTime)
    {}

    @Override
    public boolean IsValid()
    {
        return m_Anchor.IsValid();
    }

    @Override
    public void CleanUp()
    {}
}
