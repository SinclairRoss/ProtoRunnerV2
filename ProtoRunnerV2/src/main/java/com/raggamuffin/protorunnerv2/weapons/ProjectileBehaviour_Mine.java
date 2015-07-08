package com.raggamuffin.protorunnerv2.weapons;

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
    public boolean UseSimpleCollisionDetection()
    {
        return false;
    }

    @Override
    public void CleanUp()
    {

    }
}
