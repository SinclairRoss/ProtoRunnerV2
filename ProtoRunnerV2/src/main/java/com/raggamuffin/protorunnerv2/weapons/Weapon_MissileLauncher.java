package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public class Weapon_MissileLauncher extends Weapon
{
	public Weapon_MissileLauncher(Vehicle anchor, GameLogic game)
	{
		super(anchor, game, AudioClips.Missile_Spawned);

        m_ProjectileType = ProjectileType.Missile;

		m_Damage = 100;
		m_FiringSpeed = 0.0;
		m_Accuracy = 1.0;
		m_LifeSpan = 5.0;
		
		AddBarrel(2, 0, -1);
		AddBarrel(-2, 0, -1);
		AddBarrel(3, 0, -2);
		AddBarrel(-3, 0, -2);

		m_FireMode = new FireControl_MissileCharge(this, 0.4, GetNumMuzzles());
	}
}
