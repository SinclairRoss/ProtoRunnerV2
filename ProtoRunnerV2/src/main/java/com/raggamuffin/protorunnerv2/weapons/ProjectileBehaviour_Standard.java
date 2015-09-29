package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;

public class ProjectileBehaviour_Standard extends ProjectileBehaviour
{
	public ProjectileBehaviour_Standard(Projectile anchor) 
	{
		super(anchor);
	}

	@Override
	public void Update(double deltaTime) 
	{
		// Do nothing.
	}

    @Override
    public boolean CollidesWith(Vehicle other)
    {
        return CollisionDetection.CheckCollisions(m_Anchor, other);
    }

    @Override
    public void CollisionResponce(Vehicle other)
    {
        m_Anchor.ForceInvalidation();
    }

    @Override
    public double CalculateDamageOutput(double deltaTime)
    {
        return 0;
    }

    @Override
	public void CleanUp() 
	{

	}
}
