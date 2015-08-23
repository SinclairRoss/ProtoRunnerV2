package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class BurstEmitter extends ParticleEmitter
{
	private int m_BurstCount;
	
	public BurstEmitter(GameObject anchor, ParticleManager pManager)
	{
		super(anchor, pManager, new EmissionBehaviour_None(), 2.0);
		
		m_EmissionForce = 9000;
		m_BurstCount = 20;
	}
	
	public void Burst()
	{
		Emit(m_BurstCount);
	}
	
	@Override
	protected Vector3 CalculateParticleForward() 
	{
		double I = MathsHelper.RandomDouble(-1.0, 1.0);
		double J = MathsHelper.RandomDouble( 0.0, 1.0);
		double K = MathsHelper.RandomDouble(-1.0, 1.0);
		
		m_ParticleForward.SetVector(I, J, K);
		m_ParticleForward.Normalise();
		
		return m_ParticleForward;
	}
}