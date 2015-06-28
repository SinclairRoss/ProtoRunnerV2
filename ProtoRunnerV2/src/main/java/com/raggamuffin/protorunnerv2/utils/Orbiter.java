package com.raggamuffin.protorunnerv2.utils;

import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.renderer.ModelType;

public class Orbiter extends Vehicle
{
	private double m_Speed;
	private double m_Range;

	public Orbiter(BulletManager BManager, ParticleManager PManager,PubSubHub PubSub, GameAudioManager audio) 
	{
		super(PManager, PubSub, audio);
		
		m_Model = ModelType.Cube;
		
		this.SetAffiliation(AffiliationKey.RedTeam);
		
		m_Speed = 1.0;
		m_Range = 6;
		
		m_Position.SetVector(m_Range, 0, m_Range);
	}
	
	@Override
	public void Update(double deltaTime)
	{
		m_Position.RotateY(deltaTime * m_Speed);
	}
}
