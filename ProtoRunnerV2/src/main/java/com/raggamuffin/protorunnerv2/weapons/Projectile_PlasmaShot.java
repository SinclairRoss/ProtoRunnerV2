package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.CollisionReport;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Projectile_PlasmaShot extends Projectile
{
    private Timer m_LifeSpan;

    public Projectile_PlasmaShot(Vector3 position, Vector3 initialVelocity, Vector3 forward, Colour colour, double baseDamage, AffiliationKey affiliation)
    {
        super(position, initialVelocity, forward, colour, baseDamage, affiliation, ModelType.PlasmaPulse);

        m_LifeSpan = new Timer(5);

        m_Scale.SetVector(0.25, 0.25, 3);

        double muzzleVelocity = 150.0;

        m_BoundingRadius = 0.1;

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
    public CollisionReport CheckForCollision(GameObject object)
    {
        return CollisionDetection.CheckCollisions(this, object);
    }

    @Override
    public void CollisionResponse(CollisionReport report)
    {
        ForceInvalidation();
    }

    @Override
    public void CleanUp()
    {

    }
}
