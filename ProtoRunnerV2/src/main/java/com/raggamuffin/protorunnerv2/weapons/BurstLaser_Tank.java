package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class BurstLaser_Tank extends Weapon
{
	public BurstLaser_Tank(Vehicle anchor, VehicleManager vManager, BulletManager bManager, ParticleManager pManager, GameAudioManager audio, PubSubHub pubSub)
	{
		super(anchor, bManager, pManager, audio);

		m_Damage = 1000.0;
		m_MuzzleVelocity = 3.5;
		m_Accuracy = 0.6;
		m_LifeSpan = 2.0;
		
		m_FireMode = new FireControl_Burst(3, 30);
		m_ProjectileTemplate = new ProjectileTemplate(this, m_Anchor.GetVehicleInfo(), ModelType.PulseLaser, GetAffiliation(),
													  m_MuzzleVelocity, m_Damage, m_LifeSpan, m_ProjectileFadeInTime,
													  ProjectileBehaviourType.Standard, m_ParticleManager, m_BulletManager, audio, vManager, pubSub);

		m_HasLasers = true;
		
		AddMuzzle( 0, 0, 2.5);
	
		SetColour(Colours.DarkGoldenRod);
	}
}
