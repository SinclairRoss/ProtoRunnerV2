package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class DirectionalEmitter extends ParticleEmitter
{
	public DirectionalEmitter(GameObject anchor, ParticleManager pManager) 
	{
		super(anchor, pManager, new EmissionBehaviour_Timed(0.1), 5.0);
		
		m_Range = Math.PI * 0.05;
	}
	
	@Override
	protected Vector3 CalculateParticleForward() 
	{
		m_ParticleForward.SetVector(m_Forward);
		m_ParticleForward.RotateY(MathsHelper.RandomDouble(-m_Range, m_Range));
		
		return m_ParticleForward;
	}
}
