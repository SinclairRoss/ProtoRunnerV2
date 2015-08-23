// Author: 	Sinclair Ross.
// Date:	23/10/2014.

package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.vehicles.Vehicle;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class PulseLaser extends Weapon
{
	public PulseLaser(Vehicle anchor, GameLogic game)
	{
		super(anchor, game);

		m_Damage = 40;
        m_Drain = 15;
		m_MuzzleVelocity = 45.0;
		m_Accuracy = 1.0;
		m_LifeSpan = 5.0;

		m_AudioClip = AudioClips.PulseLaserPunk;

		m_FireMode = new FireControl_Pulse(0.6, 0.06, 4);
		m_ProjectileTemplate = new ProjectileTemplate(this, m_Anchor.GetVehicleInfo(), ModelType.PulseLaser, GetAffiliation(),
													  m_MuzzleVelocity, m_Damage, m_LifeSpan, m_ProjectileFadeInTime, m_ProjectileFadeOutTime, 0.0,
													  ProjectileBehaviourType.Standard, game);

		m_HasLasers = true;

		AddMuzzle( 1, 0, 0);
		AddMuzzle(-1, 0, 0);
	}
}
