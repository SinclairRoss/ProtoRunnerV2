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
        super(anchor, game);

        m_Damage = 40;
        m_MuzzleVelocity = 70.0;
        m_Accuracy = 1.0;
        m_LifeSpan = 5.0;

        m_AudioClip = AudioClips.PulseLaserPunk;

        m_FireMode = new FireControl_Pulse(0.6, 0.06, 4);

        AddBarrel(1, 0, 0);
        AddBarrel(-1, 0, 0);
    }
}
