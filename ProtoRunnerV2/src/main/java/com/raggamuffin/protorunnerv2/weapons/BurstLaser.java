package com.raggamuffin.protorunnerv2.weapons;

import android.content.Context;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class BurstLaser extends Weapon
{
	public BurstLaser(Vehicle anchor, VehicleManager vManager, BulletManager bManager, ParticleManager pManager, GameAudioManager audio, PubSubHub pubSub, Context context)
	{
		super(anchor, bManager, pManager, audio);

		m_Damage = 50.0;
        m_Drain = 20;
		m_MuzzleVelocity = 3.5;
		m_Accuracy = 0.85;
		m_LifeSpan = 2.0;

		m_FireMode = new FireControl_Burst(0.8, 12);
		m_ProjectileTemplate = new ProjectileTemplate(this, m_Anchor.GetVehicleInfo(), ModelType.PulseLaser, GetAffiliation(),
				m_MuzzleVelocity, m_Damage, m_LifeSpan, m_ProjectileFadeInTime,
				ProjectileBehaviourType.Standard, m_ParticleManager, m_BulletManager, audio, vManager, pubSub);

		m_HasLasers = true;

		AddMuzzle( 1, 0, 0);
		AddMuzzle(-1, 0, 0);

		SetColour(Colours.EmeraldGreen);

        m_Name = context.getString(R.string.weapon_burst);
	}
}