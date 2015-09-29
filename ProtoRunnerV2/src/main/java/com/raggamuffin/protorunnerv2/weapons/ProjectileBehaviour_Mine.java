package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public class ProjectileBehaviour_Mine extends ProjectileBehaviour
{
    public ProjectileBehaviour_Mine(Projectile anchor)
    {
        super(anchor);
    }

    @Override
    public void Update(double deltaTime)
    {
        m_Anchor.GetVelocity().SetVector(0.0);
    }

    @Override
    public boolean CollidesWith(Vehicle other)
    {
        return false;
    }

    @Override
    public void CollisionResponce(Vehicle other)
    {

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
