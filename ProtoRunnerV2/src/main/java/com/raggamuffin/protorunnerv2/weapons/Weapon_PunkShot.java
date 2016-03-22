package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public class Weapon_PunkShot extends Weapon
{
	public Weapon_PunkShot(Vehicle anchor, GameLogic game)
	{
		super(anchor, game);

        m_ProjectileType = ProjectileType.EnergyBall;
		
		m_Damage = 100;
		m_MuzzleVelocity = 25.0;
		m_Accuracy = 1.0;
		m_LifeSpan = 2.0;
		
		m_FireMode = new FireControl_Pulse(0.8, 0.06, 2);

		m_AudioClip = AudioClips.PulseLaser;

		AddBarrel(1, 0, 0);
		AddBarrel(-1, 0, 0);
	}
}
