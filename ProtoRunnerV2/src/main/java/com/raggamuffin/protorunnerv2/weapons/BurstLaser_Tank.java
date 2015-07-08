package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class BurstLaser_Tank extends Weapon
{
	public BurstLaser_Tank(Vehicle anchor, GameLogic game)
	{
		super(anchor, game);

		m_Damage = 1000.0;
		m_MuzzleVelocity = 3.5;
		m_Accuracy = 0.6;
		m_LifeSpan = 2.0;
		
		m_FireMode = new FireControl_Burst(3, 30);
		m_ProjectileTemplate = new ProjectileTemplate(this, m_Anchor.GetVehicleInfo(), ModelType.PulseLaser, GetAffiliation(),
													  m_MuzzleVelocity, m_Damage, m_LifeSpan, m_ProjectileFadeInTime, 0.0,
													  ProjectileBehaviourType.Standard, game);

		m_HasLasers = true;
		
		AddMuzzle( 0, 0, 2.5);
	
		SetColour(Colours.DarkGoldenRod);
	}
}
