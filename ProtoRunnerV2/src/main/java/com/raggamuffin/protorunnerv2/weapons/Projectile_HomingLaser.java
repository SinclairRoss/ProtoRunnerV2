package com.raggamuffin.protorunnerv2.weapons;

// Author: Sinclair Ross
// Date:   26/05/2017

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.CollisionReport;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Projectile_HomingLaser extends Projectile
{
    private enum LaserState
    {
        Locked,
        Released
    }

    private Timer m_LifeSpan;

    private LaserState m_LaserState;
    private double m_Growth;

    private boolean m_ForcedInvalidation;

    public Projectile_HomingLaser(Vector3 position, Vector3 initialVelocity, Vector3 firingDirection, Colour colour, double baseDamage, AffiliationKey affiliation)
    {
        super(position, initialVelocity, firingDirection, colour, baseDamage, affiliation, ModelType.WeaponDrone);

        m_LifeSpan = new Timer(5);

        m_LaserState = LaserState.Locked;
        m_Growth = 0.0;

        m_ForcedInvalidation = false;
    }

    @Override
    public void Update(double deltaTime)
    {
        switch (m_LaserState)
        {
            case Locked: { break; }
            case Released:
            {
                SetVelocity(GetForward(), 20);
                break;
            }
        }

        super.Update(deltaTime);
    }

    @Override
    public CollisionReport CheckForCollision(GameObject object)
    {
        return CollisionDetection.CheckCollisions(this, object);
    }

    @Override
    public void CollisionResponse(CollisionReport report)
    {

    }

    public void SetGrowth(double growth)
    {
        m_Growth = MathsHelper.Clamp(growth, 0, 1);
        SetScale(m_Growth);
    }

    public void Release()
    {
        m_LaserState = LaserState.Released;
        m_LifeSpan.Start();
    }

    public void Destroy()
    {
        m_ForcedInvalidation = true;
    }

    @Override
    public boolean IsValid()
    {
        if(m_ForcedInvalidation)
        {
            return false;
        }

        if(m_LifeSpan.HasElapsed())
        {
            return false;
        }

        return true;
    }
}
