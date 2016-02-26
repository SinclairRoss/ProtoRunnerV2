package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.renderer.ModelType;

import java.util.ArrayList;

public class Weapon_MissileLauncher extends Weapon
{
	public Weapon_MissileLauncher(Vehicle anchor, GameLogic game)
	{
		super(anchor, game);

        m_ProjectileType = ProjectileType.Missile;

		m_Damage = 100;
        m_Drain = 25;
		m_MuzzleVelocity = 0.0;
		m_Accuracy = 1.0;
		m_LifeSpan = 5.0;

		m_AudioClip = AudioClips.MissileSpawned;
		
		AddMuzzle( 2, 0, -1);
		AddMuzzle(-2, 0, -1);
		AddMuzzle( 3, 0, -2);
		AddMuzzle(-3, 0, -2);

		m_FireMode = new FireControl_MissileCharge(this, 0.4, GetNumMuzzles());
	}
}
