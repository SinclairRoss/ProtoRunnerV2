// Author: 	Sinclair Ross.
// Date:	23/10/2014.

package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public class Weapon_DeployFlares extends Weapon
{
    public Weapon_DeployFlares(Vehicle anchor, GameLogic game)
    {
        super(anchor, game);

        m_EquipmentType = EquipmentType.Utility;
        m_ProjectileType = ProjectileType.Flare;

        m_Damage = 40;
        m_MuzzleVelocity = 70.0;
        m_Accuracy = 1.0;
        m_LifeSpan = 5.0;

        m_AudioClip = AudioClips.MissileSpawned;

        m_FireMode = new FireControl_UtilityPulse(this, 5, 0.15, 4);

        AddBarrel(2, 0, 0);
        AddBarrel(-2, 0, 0);
        AddBarrel(6, 0, -2);
        AddBarrel(-6, 0, -2);
    }

    @Override
    public void OpenFire()
    {
        super.OpenFire();

        if(!m_FireMode.CanFire())
        {
            // m_AudioService.PlaySound(m_Anchor.GetPosition(), AudioClips.CannotFire); //TODO: CONVERT TO AUDIO EMITTER.
        }
    }
}
