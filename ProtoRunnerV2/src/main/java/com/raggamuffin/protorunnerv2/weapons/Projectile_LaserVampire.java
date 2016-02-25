package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;

public class Projectile_LaserVampire extends Projectile_Laser
{
    private final double m_ChargeMultiplier;

    public Projectile_LaserVampire(Weapon origin, GameLogic game)
    {
        super(origin, game);

        m_ChargeMultiplier = 5.0;
    }


    @Override
    public void CollisionResponse(GameObject other)
    {
        m_Origin.GetAnchor().ChargeEnergy(GetDamageOutput() * m_ChargeMultiplier);
        super.CollisionResponse(other);
    }
}
