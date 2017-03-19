package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Point;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.CollisionReport;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Timer_Accumulation;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Projectile_Flare extends Projectile
{
    private Timer_Accumulation m_LifeSpan;
    private Vector3 m_Destination;

    private ParticleEmitter_Point m_PointEmitter;

    public Projectile_Flare(GameLogic game, Vector3 position, Vector3 initialVelocity, Vector3 forward, Colour colour, double baseDamage, AffiliationKey affiliation)
    {
        super(position, initialVelocity, forward, colour, baseDamage, affiliation, ModelType.Nothing);

        m_LifeSpan = new Timer_Accumulation(5.0);

       // ApplyForce(m_Up, 200000);

        m_Destination = new Vector3(GetForward());
        m_Destination.Y = 0.0;
        m_Destination.Normalise();
        m_Destination.Scale(50.0);


        m_PointEmitter = new ParticleEmitter_Point(game, new Colour(Colours.IndianRed), new Colour(Colours.Blue), 5, 1.0);
    }

    @Override
    public void Update(double deltaTime)
    {
        m_LifeSpan.Update(deltaTime);
        m_PointEmitter.SetPosition(GetPosition());
        m_PointEmitter.Update(deltaTime);

        GetForward().Add(m_Destination);
        GetForward().Normalise();

        ApplyForce(GetForward(), 12000);

        super.Update(deltaTime);
    }

    @Override
    public boolean IsValid()
    {
        if(m_LifeSpan.TimedOut())
        {
            return false;
        }

        return true;
    }

    @Override
    public CollisionReport CheckForCollision(GameObject object)
    {
        return null;
    }

    @Override
    public void CollisionResponse(CollisionReport report)
    {}

    @Override
    public void CleanUp()
    {}
}
