package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class PulseLaser_Punk extends Weapon
{
	public PulseLaser_Punk(Vehicle anchor, GameLogic game)
	{
		super(anchor, game);
		
		m_Damage = 200;
		m_MuzzleVelocity = 0.6;
		m_Accuracy = 1.0;
		m_LifeSpan = 2.0;
		
		m_FireMode = new FireControl_Pulse(1.0, 0.06, 1);
		m_ProjectileTemplate = new ProjectileTemplate(this, m_Anchor.GetVehicleInfo(), ModelType.PulseLaser, GetAffiliation(),
														m_MuzzleVelocity, m_Damage, m_LifeSpan, m_ProjectileFadeInTime, 0.0,
														ProjectileBehaviourType.Standard, game);

		m_AudioClip = AudioClips.PulseLaser;
		
		m_HasLasers = false;
		
		AddMuzzle( 1, 0, 0);
		AddMuzzle(-1, 0, 0);
		
		SetColour(Colours.Crimson);
	}
}
