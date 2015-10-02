package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.renderer.ModelType;

public class RailGun_Tank extends Weapon
{
	//BurstEmitter m_BurstEmitter;
	
	public RailGun_Tank(Vehicle anchor, GameLogic game)
	{
		super(anchor, game);
		
		//m_BurstEmitter = new BurstEmitter(m_Anchor, m_ParticleManager);
		
		m_Damage = 100;
		m_MuzzleVelocity = 90.0;
		m_Accuracy = 1.0;
		m_LifeSpan = 0.5;
		
		m_FireMode = new FireControl_Charge(1.0, 1.0);
		m_ProjectileTemplate = new ProjectileTemplate(this, ModelType.RailSlug, GetAffiliation(),
							   m_MuzzleVelocity, m_Damage, m_LifeSpan, 2.0,
							   ProjectileBehaviourType.Standard, game, SpecialProjectileBehaviourType.AddAttractGraviton, SpecialProjectileBehaviourType.Explode);
		
		m_HasLasers = true;
		
		AddMuzzle(0, 0, 1);
		
		m_AudioClip = AudioClips.RailGun;
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