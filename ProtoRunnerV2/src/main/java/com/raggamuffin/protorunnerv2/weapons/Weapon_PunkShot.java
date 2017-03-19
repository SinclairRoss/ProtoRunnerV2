package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public class Weapon_PunkShot extends Weapon
{
	public Weapon_PunkShot(Vehicle anchor, GameLogic game)
	{
		super(anchor, game, AudioClips.Blaster_Enemy);

        m_ProjectileType = ProjectileType.PlasmaShot_Telegraphed;
		
		m_Damage = 1;
		m_FiringSpeed = 140.0;
		m_Accuracy = 1.0;
		m_LifeSpan = 2.0;
		
		m_FireMode = new FireControl_Pulse(1.0, 0.06, 1);

		AddBarrel(0, 0, 0);

		m_WeaponComponent = InitialiseWeaponComponent(EWeaponComponents.LaserPointer);
	}
}
