// Author: 	Sinclair Ross.
// Date:	23/10/2014.

package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.FloorGrid;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public class Weapon_PulseLaser extends Weapon
{
    public Weapon_PulseLaser(Vehicle anchor, GameLogic game)
    {
        super(anchor, game, AudioClips.Blaster_Friendly);

        m_Damage = 40;
        m_FiringSpeed = 140.0;   // 140
        m_Accuracy = 1.0;
        m_LifeSpan = 5.0;

        AddBarrel(1, 0, 0);
        AddBarrel(-1, 0, 0);

        m_FireMode = new FireControl_Pulse(0.4, 0.06, 4);
    }

    @Override
    public void WeaponEquipped()
    {
    }

    @Override
    public void WeaponUnequipped()
    {
    }
}
