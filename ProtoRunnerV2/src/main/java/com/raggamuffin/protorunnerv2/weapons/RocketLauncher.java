package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.vehicles.Vehicle;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class RocketLauncher extends Weapon
{
	public RocketLauncher(Vehicle anchor, GameLogic game)
	{
		super(anchor, game);

		m_Damage = 100;
        m_Drain = 25;
		m_MuzzleVelocity = 0.0;
		m_Accuracy = 1.0;
		m_LifeSpan = 5.0;
		m_ProjectileFadeInTime = 0.0;
        m_ProjectileFadeOutTime = 0.0;
		
		m_AudioClip = AudioClips.MissileSpawned;
		
		AddMuzzle( 2, 0, -1);
		AddMuzzle(-2, 0, -1);
		AddMuzzle( 3, 0, -2);
		AddMuzzle(-3, 0, -2);
		
		m_FireMode = new FireControl_MissileCharge(this, 0.25, GetNumMuzzles());
		m_ProjectileTemplate = new ProjectileTemplate(this, m_Anchor.GetVehicleInfo(), ModelType.Missile, GetAffiliation(),
													  m_MuzzleVelocity, m_Damage, m_LifeSpan, m_ProjectileFadeInTime, m_ProjectileFadeOutTime, 0.0,
													  ProjectileBehaviourType.Missile, game, SpecialProjectileBehaviourType.Explode);
		
		m_HasLasers = false;
	}
}
