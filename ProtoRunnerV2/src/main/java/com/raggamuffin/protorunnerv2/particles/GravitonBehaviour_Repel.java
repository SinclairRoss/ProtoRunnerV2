package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class GravitonBehaviour_Repel extends GravitonBehaviour
{
	public GravitonBehaviour_Repel(Graviton anchor) 
	{
		super(anchor);
	}

	@Override
	public void ApplyGravitonForce(Particle particle) 
	{
		//m_ToParticle.SetVectorDifference(particle.GetPosition(), m_Anchor.GetPosition());
		
		double length = m_ToParticle.GetLength();
		double inverse;
		
		if(length < 1.0)
			inverse = 1.0;
		else
			inverse = MathsHelper.FastInverseSqrt((float)length);
		
		m_ToParticle.Normalise();
		
		particle.ApplyForce(m_ToParticle, inverse * -m_Anchor.GetPull());
	}
}
