package com.raggamuffin.protorunnerv2.weapons;

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
	public void CleanUp() 
	{

	}

    @Override
    public boolean UseSimpleCollisionDetection()
    {
        return false;
    }
}
