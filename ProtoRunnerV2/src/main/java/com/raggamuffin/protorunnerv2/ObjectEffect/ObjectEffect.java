package com.raggamuffin.protorunnerv2.ObjectEffect;

// Author: Sinclair Ross
// Date:   22/05/2017

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class ObjectEffect extends GameObject
{
    private Vehicle m_Anchor;
    private ObjectEffectType m_EffectType;

    public ObjectEffect(ModelType model, ObjectEffectType effectType)
    {
        super(model, 0);

        m_EffectType = effectType;
    }

    public void Initialise(Vehicle anchor)
    {
        m_Anchor = anchor;
    }

    @Override
    public void Update(double deltaTime) {}

    protected Vehicle GetAnchor() { return m_Anchor; }
    public ObjectEffectType GetEffectType() { return m_EffectType; }
}
