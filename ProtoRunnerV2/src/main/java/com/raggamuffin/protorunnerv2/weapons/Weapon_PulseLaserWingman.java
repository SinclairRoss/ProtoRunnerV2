// Author: 	Sinclair Ross.
// Date:	23/10/2014.

package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public class Weapon_PulseLaserWingman extends Weapon
{
    public Weapon_PulseLaserWingman(Vehicle anchor, GameLogic game)
    {
        super(anchor, game, ProjectileType.PlasmaShot, AudioClips.Blaster_Friendly);

        m_Damage = 1;
        m_FiringSpeed = 200.0;   // 140
        m_Accuracy = 1.0;
        m_LifeSpan = 5.0;

        m_FireMode = new FireControl_LimitedPulse(0.06, 0.5, 2);

        AddBarrel(1, 0, 0);
        AddBarrel(-1, 0, 0);
    }
}
