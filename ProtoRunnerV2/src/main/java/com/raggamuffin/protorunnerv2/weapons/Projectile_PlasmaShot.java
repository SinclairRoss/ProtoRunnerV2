package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_HyperLight;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Point;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Spiral;
import com.raggamuffin.protorunnerv2.particles.TrailEmitter;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;

public class Projectile_PlasmaShot extends Projectile
{
    private Timer m_LifeSpan;

    public Projectile_PlasmaShot(Weapon origin, GameLogic game)
    {
        super(origin);

        m_Model = ModelType.Nothing;

        m_LifeSpan = new Timer(1.5);

        double muzzleVelocity = 120.0;

        m_Velocity.SetVector(m_Origin.GetVelocity());
        m_Velocity.I += m_Forward.I * muzzleVelocity;
        m_Velocity.J += m_Forward.J * muzzleVelocity;
        m_Velocity.K += m_Forward.K * muzzleVelocity;

        TrailEmitter emitter = new TrailEmitter(this, game, 0.0);
        emitter.LowResolutionMode();
        AddChild(emitter);
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
