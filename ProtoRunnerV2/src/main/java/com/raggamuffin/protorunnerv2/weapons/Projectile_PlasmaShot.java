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

    private GameLogic m_Game;

    private boolean m_HasColided;

    public Projectile_PlasmaShot(GameLogic game, Vector3 position, Vector3 initialVelocity, Vector3 forward, Colour colour, double baseDamage, double firingSpeed, AffiliationKey affiliation)
    {
        super(position, initialVelocity, forward, colour, baseDamage, affiliation, ModelType.PlasmaPulse);

        m_Game = game;

        m_LifeSpan = new Timer(4);
        m_LifeSpan.Start();

        SetScale(0.25, 0.25, 3);

        SetVelocity(GetForward(), firingSpeed);

        if(GetVelocity().GetLengthSqr() >= 0.1)
        {
            // Points the laser to point in the direction it is travelling and not the direction it was fired at.
            SetForward(GetVelocity());
            GetForward().Normalise();
        }
        else
        {
            SetForward(forward);
        }

        m_FloorGrid = new FloorGrid(GetPosition(), GetColour(), 10.0);
        m_Game.AddObjectToRenderer(m_FloorGrid);
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
    public void CollisionResponse(CollisionReport report)
    {
        m_HasColided = true;
    }

    @Override
    public void CleanUp()
    {
        m_Game.RemoveObjectFromRenderer(m_FloorGrid);
    }
}
