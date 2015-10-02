package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class BurstLaser extends Weapon
{
	public BurstLaser(Vehicle anchor, GameLogic game)
	{
		super(anchor, game);

		m_Damage = 50.0;
        m_Drain = 6;
		m_MuzzleVelocity = 70.0;
		m_Accuracy = 0.85;
		m_LifeSpan = 2.0;

		m_FireMode = new FireControl_Burst(0.8, 12);
		m_ProjectileTemplate = new ProjectileTemplate(this, ModelType.PulseLaser, GetAffiliation(),
				m_MuzzleVelocity, m_Damage, m_LifeSpan, 0.0,
				ProjectileBehaviourType.Standard, game);

		m_HasLasers = true;

		AddMuzzle( 1, 0, 0);
		AddMuzzle(-1, 0, 0);
	}
}