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
    private Vector<LaserPointer> m_Lasers;

    public Weapon_PulseLaser(Vehicle anchor, GameLogic game)
    {
        super(anchor, game);

        m_Damage = 40;
        m_Drain = 15;
        m_MuzzleVelocity = 70.0;
        m_Accuracy = 1.0;
        m_LifeSpan = 5.0;

        m_AudioClip = AudioClips.PulseLaserPunk;

        m_Lasers = new Vector<>();

        AddMuzzle( 1, 0, 0);
        AddMuzzle(-1, 0, 0);

        for(Vector3 muzzle : m_MuzzleOffsets)
            m_Lasers.add(new LaserPointer(this, muzzle));

        m_FireMode = new FireControl_Pulse(0.4, 0.06, 4);
    }

    @Override
    public void WeaponEquipped()
    {
       // for(LaserPointer Pointer : m_Lasers)
       //     Pointer.On();
    }

    @Override
    public void WeaponUnequipped()
    {
        //for(LaserPointer Pointer : m_Lasers)
        //    Pointer.Off();
    }
}
