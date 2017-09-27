package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.CollisionReport;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Projectile_TelegraphedPlasmaShot extends Projectile
{
    private Timer m_LifeSpan;

    private boolean m_HasColided;

    public Projectile_TelegraphedPlasmaShot(GameLogic game, Vector3 position, Vector3 initialVelocity, Vector3 forward, Colour colour, double baseDamage, double firingSpeed, AffiliationKey affiliation)
    {
        super(position, initialVelocity, forward, colour, baseDamage, affiliation, ModelType.PlasmaPulse);

        m_LifeSpan = new Timer(4);
        m_LifeSpan.Start();

        SetScale(0.25, 0.25, 3);

        SetVelocity(GetForward(), firingSpeed);

        Vector3 fwd = GetVelocity().GetLengthSqr() >= 0.1 ? GetVelocity() : forward;
        SetForward(fwd);

        game.GetGameObjectManager().AddObject(new ProjectileLaserPointer(game, this));

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
        m_HasColided = true;
    }

    @Override
    public void CleanUp()
    { }
}
