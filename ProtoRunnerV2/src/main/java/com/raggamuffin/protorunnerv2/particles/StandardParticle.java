package com.raggamuffin.protorunnerv2.particles;

import java.util.Vector;

public class StandardParticle extends Particle
{
	private Vector<Graviton> m_Gravitons;
	
	public StandardParticle(Vector<Graviton> gravitons) 
	{
		super();
		
		m_Gravitons = gravitons;
	}
	
	@Override
	public void Update(double deltaTime)
	{
		super.Update(deltaTime);
		
		for(Graviton grav : m_Gravitons)
		{
			grav.GetGravitonBehaviour().ApplyGravitonForce(this);
		}
	}
	
	@Override
	public boolean IsValid()
	{
		return !m_DissipationTimer.TimedOut();
	}

	@Override
	public ParticleType GetParticleType() 
	{
		return ParticleType.Standard;
	}
}
