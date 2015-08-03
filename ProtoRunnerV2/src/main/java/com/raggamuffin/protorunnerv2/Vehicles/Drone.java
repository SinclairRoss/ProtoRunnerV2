package com.raggamuffin.protorunnerv2.Vehicles;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;

public class Drone extends GameObject
{
    private Carrier m_Anchor;

    public Drone(GameLogic game, Carrier anchor)
    {
        super(null, null);

    }

    @Override
    public boolean IsValid()
    {
        return m_Anchor.IsValid();
    }
}
