package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public class Projectile_Missile extends Projectile
{
    public Projectile_Missile(Weapon origin)
    {
        super(origin);
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
}
