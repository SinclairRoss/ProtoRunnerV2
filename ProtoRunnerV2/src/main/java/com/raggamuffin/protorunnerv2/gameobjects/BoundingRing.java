package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colour;

public class BoundingRing extends GameObject
{
    public BoundingRing(double radius, Colour colour)
    {
        super(null, null);

        m_Colour = colour;
        m_Model = ModelType.Ring;
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

    @Override
    public void CleanUp()
    {

    }
}
