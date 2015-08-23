package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.vehicles.Vehicle;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class Weapon_None extends Weapon
{
    public Weapon_None(Vehicle anchor, GameLogic game)
    {
        super(anchor, game);

        m_FireMode = new FireControl_Auto(1.0);
        AddMuzzle( 0, 0, 0);
    }

    @Override
    public void Update(double deltaTime)
    {
        // Do nothing.
    }
}
