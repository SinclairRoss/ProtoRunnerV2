package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;

public class Projectile_PlasmaShot extends Projectile
{
    private Timer m_LifeSpan;

    private final double m_FadeIn;
    private final double m_FadeOut;

    public Projectile_PlasmaShot(Weapon origin)
    {
        super(origin);

        m_LifeSpan = new Timer(3.0);
        m_FadeIn = 0.02;
        m_FadeOut = 0.9;

        double muzzleVelocity = 50.0;

        m_Velocity.SetVector(m_Origin.GetVelocity());
        m_Velocity.I += m_Forward.I * muzzleVelocity;
        m_Velocity.J += m_Forward.J * muzzleVelocity;
        m_Velocity.K += m_Forward.K * muzzleVelocity;

        m_BaseColour.Alpha = 0.0;
        m_AltColour.Alpha  = 0.0;
    }

    @Override
    public void Update(double deltaTime)
    {
        m_LifeSpan.Update(deltaTime);

        double normLifeSpan = m_LifeSpan.GetInverseProgress();

        double alpha = normLifeSpan <= m_FadeIn ?
                MathsHelper.Normalise(normLifeSpan, 0, m_FadeIn) :
                1.0 - MathsHelper.Normalise(normLifeSpan, m_FadeOut, 1.0);

        m_BaseColour.Alpha = alpha;
        m_AltColour.Alpha = alpha;

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
        return CollisionDetection.CheckCollisions(this, other);
    }

    @Override
    public void CollisionResponse(Vehicle other)
    {
        ForceInvalidation();
    }
}
