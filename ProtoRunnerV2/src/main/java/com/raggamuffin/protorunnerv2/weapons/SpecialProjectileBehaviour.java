package com.raggamuffin.protorunnerv2.weapons;

public abstract class SpecialProjectileBehaviour 
{
	Projectile m_Anchor;
	
	public SpecialProjectileBehaviour(Projectile anchor)
	{
		m_Anchor = anchor;
	}
	
	public abstract void Activate();
	public abstract void Deactivate();
}
