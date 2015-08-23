package com.raggamuffin.protorunnerv2.particles;

import java.util.ArrayList;

public class StandardParticle extends Particle
{
	private ArrayList<Graviton> m_Gravitons;
	
	public StandardParticle(ArrayList<Graviton> gravitons)
	{
		super();

        m_Gravitons = gravitons;

        m_DragCoefficient = 0.007;
        m_FadeIn = 0.2;
        m_FadeOut = 0.7;
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
