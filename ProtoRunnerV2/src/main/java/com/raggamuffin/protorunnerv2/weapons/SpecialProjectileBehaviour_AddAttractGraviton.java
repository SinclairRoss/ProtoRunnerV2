package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.particles.Graviton;
import com.raggamuffin.protorunnerv2.particles.GravitonBehaviourType;

public class SpecialProjectileBehaviour_AddAttractGraviton extends SpecialProjectileBehaviour
{
	private ParticleManager m_ParticleManager;
	private Graviton m_Graviton;
	
	public SpecialProjectileBehaviour_AddAttractGraviton(Projectile anchor, ParticleManager PManager) 
	{
		super(anchor);
		m_ParticleManager = PManager;
	}

	@Override
	public void Activate() 
	{
		m_Graviton = m_ParticleManager.CreateGraviton(1200, GravitonBehaviourType.Attract);
		m_Anchor.AddChild(m_Graviton);
	}

	@Override
	public void Deactivate() 
	{
		m_ParticleManager.DestroyGraviton(m_Graviton);
	}
}
