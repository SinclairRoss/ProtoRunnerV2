package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.CollisionReport;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Projectile_LaserVampire extends Projectile_Laser
{
    private Vehicle m_Anchor;
    private final double m_ChargeMultiplier;

    public Projectile_LaserVampire(GameLogic game, Vector3 position, Vector3 initialVelocity, Vector3 forward, Colour colour, double baseDamage, AffiliationKey affiliation, Vehicle anchor, Weapon firingWeapon)
    {
        super(game, position, initialVelocity, forward, colour, baseDamage, affiliation, firingWeapon);

        m_Anchor = anchor;
        m_ChargeMultiplier = 5.0;
    }

    @Override
    public void CollisionResponse(CollisionReport report)
    {
        m_Anchor.ChargeEnergy(GetDamageOutput() * m_ChargeMultiplier);
        super.CollisionResponse(report);
    }
}
