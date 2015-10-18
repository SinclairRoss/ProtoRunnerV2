package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Projectile_Missile extends Projectile
{
    private enum MissileState
    {
        Docked,
    }

    MissileState m_State;

    private Vector3 m_Offset;

    public Projectile_Missile(Weapon origin, GameLogic game)
    {
        super(origin);

        m_Model = ModelType.Missile;
        m_State = MissileState.Docked;

        m_Offset = new Vector3();
        m_Offset.SetVectorDifference(m_Origin.GetPosition(), m_Position);

        m_Forward.SetVector(0,1,0);

        m_Velocity.SetVector(0);
    }

    @Override
    public void Update(double deltaTime)
    {
        switch(m_State)
        {
            case Docked:
                LockProjectile();
                break;
        }
    }

    @Override
    public boolean CollidesWith(Vehicle other)
    {
        return false;
    }

    @Override
    public void CollisionResponse(Vehicle other)
    {

    }

    private void LockProjectile()
    {
        m_Position.SetVector(m_Offset);
        m_Position.RotateY(m_Origin.GetOrientation());
        m_Position.Add(m_Origin.GetPosition());

        SetYaw(m_Origin.GetOrientation());
    }
}