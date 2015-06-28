package com.raggamuffin.protorunnerv2.weapons;

public abstract class ProjectileBehaviour 
{
	protected Projectile m_Anchor;
	
	public ProjectileBehaviour(Projectile anchor)
	{
		m_Anchor = anchor;
	}
	
	public abstract void Update(double deltaTime);

	public void CollisionResponce()
	{
		m_Anchor.ForceInvalidation();
	}

    public abstract boolean UseSimpleCollisionDetection();

	public abstract void CleanUp();
}
