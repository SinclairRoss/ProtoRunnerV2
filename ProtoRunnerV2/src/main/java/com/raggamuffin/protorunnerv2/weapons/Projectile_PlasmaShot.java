package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.FloorGrid;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.StatusEffect;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
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

    private GameLogic m_Game;

    private boolean m_HasColided;

    public Projectile_PlasmaShot(GameLogic game, Vector3 position, Vector3 initialVelocity, Vector3 firingDirection, Colour colour, double baseDamage, double firingSpeed, AffiliationKey affiliation)
    {
        super(position, initialVelocity, firingDirection, colour, baseDamage, affiliation, ModelType.PlasmaPulse);

        m_Game = game;

        m_LifeSpan = new Timer(4);
        m_LifeSpan.Start();

        SetScale(0.25, 0.25, 3);

        Vector3 vel = GetVelocity();
        vel.Add(firingDirection.X * firingSpeed, firingDirection.Y * firingSpeed, firingDirection.Z * firingSpeed);

        if(GetVelocity().GetLengthSqr() >= 0.1)
        {
            Vector3 fwd = GetForward();
            fwd.SetVector(GetVelocity());
            fwd.Normalise();
            SetForward(fwd);
        }
        else
        {
            SetForward(firingDirection);
        }

        m_FloorGrid = new FloorGrid(GetPosition(), GetColour(), 10.0);
        game.GetGameObjectManager().AddFloorGrid(m_FloorGrid);

        m_HasColided = false;
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
        {
            return false;
        }

        if(m_HasColided)
        {
            return false;
        }

        return true;
    }

    public CollisionReport CheckForCollision(GameObject object)
    {
        return CollisionDetection.CheckCollisions(this, object);
    }

    @Override
    public void CollisionResponse(CollisionReport report, Vehicle other)
    {
        if(other.HasStatusEffect(StatusEffect.Shielded))
        {
            Vector3 velocity = GetVelocity();

            Vector3 normal = new Vector3();
            normal.SetVectorAsDifference(other.GetPosition(), GetPreviousPosition());
            normal.Normalise();

            double dot = Vector3.DotProduct(velocity, normal);
            normal.Scale(dot * 2);

            velocity.Subtract(normal);

            Vector3 fwd = GetForward();
            fwd.SetVector(velocity);
            fwd.Normalise();
            SetForward(fwd);

            double posOffset = 15.0;
            GetPosition().Add(fwd.X * posOffset, fwd.Y * posOffset, fwd.Z * posOffset);
        }
        else
        {
            m_HasColided = true;
        }
    }

    @Override
    public void CleanUp()
    {
        m_FloorGrid.NotifyOfAnchorInvalidation();
    }
}
