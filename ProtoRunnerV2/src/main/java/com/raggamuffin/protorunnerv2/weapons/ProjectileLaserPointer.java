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
        super(game, ModelType.LaserPointer);

        m_Position = anchor.GetPosition();
        UpdateVectorsWithForward(anchor.GetForward());

        m_Scale.SetVector(0.1, 0.1, 1000);
        m_Colour.Alpha = 1.0;
        m_Anchor = anchor;

        SetBaseColour(game.GetColourManager().GetDangerColour());
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