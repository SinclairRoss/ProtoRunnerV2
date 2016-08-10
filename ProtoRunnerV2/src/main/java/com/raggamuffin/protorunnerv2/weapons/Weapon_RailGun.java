package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public class Weapon_RailGun extends Weapon
{
	//BurstEmitter m_BurstEmitter;
	
	public Weapon_RailGun(Vehicle anchor, GameLogic game)
	{
		super(anchor, game, AudioClips.Laser_Friendly);
		
		//m_BurstEmitter = new BurstEmitter(m_Anchor, m_ParticleManager);
		
		m_Damage = 100;
		m_FiringSpeed = 90.0;
		m_Accuracy = 1.0;
		m_LifeSpan = 0.5;
		
		m_FireMode = new FireControl_Charge(1.0, 1.0);

		AddBarrel(0, 0, 1);
	}
	
	@Override
	public void OpenFire()
	{	
		if(m_FireMode.CanFire())
		{
		//	m_BurstEmitter.Burst();
		}
		
		super.OpenFire();	
	}
}