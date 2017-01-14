package com.raggamuffin.protorunnerv2.weapons;

// Author: Sinclair Ross
// Date:   08/01/2017

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public class Weapon_ChargeLaser extends Weapon
{
    public Weapon_ChargeLaser(Vehicle anchor, GameLogic game, AudioClips fireClip)
    {
        super(anchor, game, fireClip);
    }
}
