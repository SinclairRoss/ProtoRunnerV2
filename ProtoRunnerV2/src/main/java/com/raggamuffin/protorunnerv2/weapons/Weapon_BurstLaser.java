package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public class Weapon_BurstLaser extends Weapon
{
	public Weapon_BurstLaser(Vehicle anchor, GameLogic game)
	{
		super(anchor, game, AudioClips.Blaster_Friendly);

		m_Damage = 50.0;
		m_FiringSpeed = 70.0;
		m_Accuracy = 0.85;
		m_LifeSpan = 2.0;

		m_FireMode = new FireControl_Burst(0.8, 12);

		AddBarrel(1, 0, 0);
		AddBarrel(-1, 0, 0);
	}
}