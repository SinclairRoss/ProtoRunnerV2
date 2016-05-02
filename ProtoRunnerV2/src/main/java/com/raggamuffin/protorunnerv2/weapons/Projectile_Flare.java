package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Point;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.CollisionReport;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Projectile_Flare extends Projectile
{
    public Projectile_Flare(Vector3 position, Vector3 initialVelocity, Vector3 forward, Colour colour, double baseDamage, AffiliationKey affiliation, ProjectileType type)
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
    private Timer m_LifeSpan;
    private Vector3 m_Destination;

    private ParticleEmitter_Point m_PointEmitter;

    public Projectile_Flare(Weapon origin, GameLogic game)
    {
        super(origin);

        m_Model = ModelType.Nothing;

        m_LifeSpan = new Timer(3.0);
        m_DragCoefficient	 = 0.85;
        m_Forward.SetVector(m_Origin.GetForward());
        m_Up.SetVector(m_Origin.GetUp());

        ApplyForce(m_Up, 40000);

        m_Destination = new Vector3(m_Forward);
        m_Destination.J = 0.0;
        m_Destination.Normalise();
        m_Destination.Scale(50.0);

        m_PointEmitter = new ParticleEmitter_Point(game, new Colour(), m_Origin.GetBaseColour(), 200, 1.0);
    }

    @Override
    public void Update(double deltaTime)
    {
        m_LifeSpan.Update(deltaTime);
        m_PointEmitter.SetPosition(m_Position);
        m_PointEmitter.Update(deltaTime);

        m_Forward.Add(m_Destination);
        m_Forward.Normalise();

        ApplyForce(m_Forward, 10000);

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
        return false;
    }

    @Override
    public void CollisionResponse(GameObject other)
    {
        ForceInvalidation();
    }*/
}
