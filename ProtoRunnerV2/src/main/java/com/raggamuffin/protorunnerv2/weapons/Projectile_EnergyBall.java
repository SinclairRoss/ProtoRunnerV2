package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.Timer;

public class Projectile_EnergyBall extends Projectile
{
    private Timer m_LifeSpan;

    public Projectile_EnergyBall(Weapon origin)
    {
        super(origin);

        m_Model = ModelType.PlasmaShot;

        m_LifeSpan = new Timer(1.5);

        double muzzleVelocity = 40.0;
        m_Scale.SetVector(60);

        m_Velocity.SetVector(m_Origin.GetVelocity());
        m_Velocity.I += m_Forward.I * muzzleVelocity;
        m_Velocity.J += m_Forward.J * muzzleVelocity;
        m_Velocity.K += m_Forward.K * muzzleVelocity;
    }
    @Override
    public void Update(double deltaTime)
    {
        m_LifeSpan.Update(deltaTime);

        super.Update(deltaTime);
    }

    @Override
    public boolean IsValid()
    {
        if(m_LifeSpan.TimedOut())
            return false;

        return super.IsValid();
    }

    @Override
    public void CleanUp()
    {

    }

    @Override
    public boolean CollidesWith(GameObject other)
    {
        return CollisionDetection.CheckCollisions(this, other);
    }

    @Override
    public void CollisionResponse(GameObject other)
    {
        ForceInvalidation();
    }
}
