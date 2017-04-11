package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   09/04/2017

import com.raggamuffin.protorunnerv2.renderer.ModelType;

public class Marker_BoundingSphere extends GameObject
{
    GameObject m_Anchor;

    public Marker_BoundingSphere(GameObject anchor)
    {
        super(ModelType.Ring, 0);

        m_Anchor = anchor;

        SetOrientationVectorsByRef(m_Anchor);
    }

    @Override
    public void Update(double deltaTime)
    {
        SetPosition(m_Anchor.GetPosition());
        SetScale(m_Anchor.GetBoundingRadius());
    }

    @Override
    public boolean IsValid()
    {
        return m_Anchor.IsValid();
    }

    @Override
    public void CleanUp()
    {}
}
