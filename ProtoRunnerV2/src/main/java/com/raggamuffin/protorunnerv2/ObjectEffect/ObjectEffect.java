package com.raggamuffin.protorunnerv2.ObjectEffect;

// Author: Sinclair Ross
// Date:   22/05/2017

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class ObjectEffect extends GameObject
{
    private GameObject m_Anchor;
    private ObjectEffectType m_EffectType;

    private Timer m_LifeTimer;

    public ObjectEffect(ModelType model, ObjectEffectType effectType, double duration)
    {
        super(model, 0);

        m_EffectType = effectType;
        m_LifeTimer = new Timer(duration);
    }

    public void Initialise(GameObject anchor)
    {
        m_Anchor = anchor;
        m_LifeTimer.Start();
    }

    @Override
    public void Update(double deltaTime) {}

    @Override
    public boolean IsValid()
    {
        return !m_LifeTimer.HasElapsed();
    }

    protected GameObject GetAnchor() { return m_Anchor; }
    protected double GetProgress() { return m_LifeTimer.GetProgress(); }

    public ObjectEffectType GetEffectType() { return m_EffectType; }
}
