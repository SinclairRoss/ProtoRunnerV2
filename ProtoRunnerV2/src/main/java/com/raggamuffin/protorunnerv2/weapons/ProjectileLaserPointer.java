package com.raggamuffin.protorunnerv2.weapons;

// Author: Sinclair Ross
// Date:   09/01/2017

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.renderer.ModelType;

public class ProjectileLaserPointer extends GameObject
{
    private Projectile m_Anchor;

    public ProjectileLaserPointer(GameLogic game, Projectile anchor)
    {
        super(ModelType.LaserPointer, 1.0);

        m_Anchor = anchor;

        SetPositionByRef(anchor.GetPosition());

        SetForward(m_Anchor.GetForward());

        SetScale(0.1, 0.1, 1000);
        SetAlpha(1.0);

        SetColour(anchor.GetColour());
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