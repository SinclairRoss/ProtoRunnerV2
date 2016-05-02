package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.CollisionReport;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Projectile_EnergyBall extends Projectile
{
    public Projectile_EnergyBall(Vector3 position, Vector3 initialVelocity, Vector3 forward, Colour colour, double baseDamage, AffiliationKey affiliation, ProjectileType type)
    {
        super(position, initialVelocity, forward, colour, baseDamage, affiliation, ModelType.Bit);
    }

    @Override
    public CollisionReport CheckForCollision(GameObject object)
    {
        return null;
    }

    @Override
    public void CollisionResponse(CollisionReport report)
    {

    }

    @Override
    public void CleanUp()
    {

    }
    /*
    public Projectile_EnergyBall(Vector3 position, Vector3 forward, Colour colour, double baseDamage, AffiliationKey affiliation, ProjectileType type)
    {
        super(position, forward, colour, baseDamage, affiliation, type);
    }

    @Override
    public CollisionReport CollidesWith(GameObject object)
    {
        return null;
    }

    @Override
    public void CollisionResponse(CollisionReport report)
    {

    }

    @Override
    public void CleanUp()
    {

    }
    /*
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
    public CollisionReport CollidesWith(GameObject object)
    {
        return null;
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
    public void CollisionResponse(CollisionReport report)
    {

    }

    @Override
    public void CollisionResponse(GameObject other)
    {
        ForceInvalidation();
    }
    */
}
