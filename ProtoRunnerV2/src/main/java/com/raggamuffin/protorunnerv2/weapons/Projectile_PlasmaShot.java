package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.FloorGrid;
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

    private FloorGrid m_FloorGrid;

    public Projectile_PlasmaShot(GameLogic game, Vector3 position, Vector3 initialVelocity, Vector3 forward, Colour colour, double baseDamage, double firingSpeed, AffiliationKey affiliation)
    {
        super(game, position, initialVelocity, forward, colour, baseDamage, affiliation, ModelType.PlasmaPulse);

        m_LifeSpan = new Timer(4); // 5

        //TODO: Make length relative to speed. maybe.
        m_Scale.SetVector(0.25, 0.25, 3);

        m_BoundingRadius = 0.1;

        m_Velocity.I += m_Forward.I * firingSpeed;
        m_Velocity.J += m_Forward.J * firingSpeed;
        m_Velocity.K += m_Forward.K * firingSpeed;

        if(m_Velocity.GetLengthSqr() >= 0.1)
        {
            // Points the laser to point in the direction it is travelling and not the direction it was fired at.
            m_Forward.SetVector(m_Velocity);
            m_Forward.Normalise();
        }
        else
        {
            m_Forward.SetVector(forward);
        }

        UpdateVectorsWithForward(m_Forward);

        m_FloorGrid = new FloorGrid(m_Position, m_Colour, 10.0);
        m_Game.AddObjectToRenderer(m_FloorGrid);
    }

    @Override
    public void Update(double deltaTime)
    {
        super.Update(deltaTime);
    }

    @Override
    public boolean IsValid()
    {
        if(m_LifeSpan.HasElapsed())
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
        m_Game.RemoveObjectFromRenderer(m_FloorGrid);
    }
}
