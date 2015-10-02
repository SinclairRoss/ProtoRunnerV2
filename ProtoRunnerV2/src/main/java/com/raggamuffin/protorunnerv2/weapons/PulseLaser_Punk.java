package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.renderer.ModelType;

public class PulseLaser_Punk extends Weapon
{
	public PulseLaser_Punk(Vehicle anchor, GameLogic game)
	{
		super(anchor, game);
		
		m_Damage = 100;
		m_MuzzleVelocity = 25.0;
		m_Accuracy = 1.0;
		m_LifeSpan = 2.0;
		
		m_FireMode = new FireControl_Pulse(0.8, 0.06, 2);
		m_ProjectileTemplate = new ProjectileTemplate(this, ModelType.PulseLaser, GetAffiliation(),
														m_MuzzleVelocity, m_Damage, m_LifeSpan, 0.0,
														ProjectileBehaviourType.Standard, game);

		m_AudioClip = AudioClips.PulseLaser;
		
		m_HasLasers = false;
		
		AddMuzzle( 1, 0, 0);
		AddMuzzle(-1, 0, 0);
	}
}
