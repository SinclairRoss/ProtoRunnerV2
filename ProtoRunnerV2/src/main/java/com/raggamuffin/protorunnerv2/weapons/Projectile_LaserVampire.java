package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.CollisionReport;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Projectile_LaserVampire extends Projectile_Laser_Depricated
{
    public Projectile_LaserVampire(Vector3 position, Vector3 forward, Colour colour, double baseDamage, AffiliationKey affiliation, ProjectileType type)
    {
        super(position, forward, colour, baseDamage, affiliation, type);
    }

    @Override
    public CollisionReport CheckForCollision(GameObject object)
    {
        return null;
    }
    /*
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
    }*/
}
