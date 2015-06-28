package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class GravitonBehaviour 
{
	protected Graviton m_Anchor;
	protected Vector3 m_ToParticle;
	
	public GravitonBehaviour(Graviton anchor)
	{
		m_Anchor = anchor;
		m_ToParticle = new Vector3();
	}
	
	public abstract void ApplyGravitonForce(Particle particle);
}
