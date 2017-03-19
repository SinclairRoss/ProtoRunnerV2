package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public class Weapon_None extends Weapon
{
    public Weapon_None(Vehicle anchor, GameLogic game)
    {
        super(anchor, game, AudioClips.Silence);

        m_FireMode = new FireControl_Auto(1.0);
        AddBarrel(0, 0, 0);
    }

    @Override
    public void Update(double deltaTime)
    {}
}
