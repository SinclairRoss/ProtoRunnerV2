package com.raggamuffin.protorunnerv2.ObjectEffect;

// Author: Sinclair Ross
// Date:   22/05/2017

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.renderer.ModelType;

public abstract class ObjectEffect extends GameObject
{
    private Vehicle m_Anchor;

    public ObjectEffect(ModelType model)
    {
        super(model, 0);
    }

    public void Initialise(Vehicle anchor)
    {
        m_Anchor = anchor;
    }

    @Override
    public void Update(double deltaTime) {}

    protected Vehicle GetAnchor() { return m_Anchor; }
}
