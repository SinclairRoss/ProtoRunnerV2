package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;

public class Projectile_Flare extends Projectile
{
    private Timer m_LifeSpan;

    private final double m_FadeIn;
    private final double m_FadeOut;

    public Projectile_Flare(Weapon origin)
    {
        super(origin);

        m_LifeSpan = new Timer(3.0);
        m_FadeIn = 0.02;
        m_FadeOut = 0.9;

        double muzzleVelocity = 50.0;

        m_Forward.RotateX(Math.toRadians(45));

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
    public boolean CollidesWith(Vehicle other)
    {
        return false;
    }

    @Override
    public void CollisionResponse(Vehicle other)
    {
        ForceInvalidation();
    }
}
