package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;

public class SpecialProjectileBehaviour_Explode extends SpecialProjectileBehaviour
{
	private BulletManager m_BulletManager;
	
	public SpecialProjectileBehaviour_Explode(Projectile anchor, ParticleManager pManager, BulletManager bManager) 
	{
		super(anchor);
		
		m_BulletManager = bManager;
	}
	
	@Override
	public void Activate()
	{
		
	}
	
	@Override
	public void Deactivate()
	{
		m_BulletManager.CreateExplosion(m_Anchor.GetPosition(), m_Anchor.GetAffiliation(), m_Anchor.GetColour(), 10, 1.5);
	}
}