package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public abstract class ProjectileBehaviour
{
	protected Projectile m_Anchor;
	
	public ProjectileBehaviour(Projectile anchor)
	{
		m_Anchor = anchor;
	}
	
	public abstract void Update(double deltaTime);
    public abstract boolean CollidesWith(Vehicle other);
    public abstract void CollisionResponce(Vehicle other);
    public abstract double CalculateDamageOutput(double baseDamage, double deltaTime);
	public abstract void CleanUp();
}
