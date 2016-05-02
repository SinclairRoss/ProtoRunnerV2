// Author: 	Sinclair Ross.
// Date:	23/10/2014.

package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.Vector;

public class Weapon_PulseLaser extends Weapon
{
    public Weapon_PulseLaser(Vehicle anchor, GameLogic game)
    {
        super(anchor, game);

        m_Damage = 40;
        m_MuzzleVelocity = 70.0;
        m_Accuracy = 1.0;
        m_LifeSpan = 5.0;

        m_AudioClip = AudioClips.PulseLaserPunk;

        AddBarrel(1, 0, 0);
        AddBarrel(-1, 0, 0);
        //AddBarrel(0,0,0);

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
