package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.renderer.ModelType;

public class LaserBurner extends Weapon
{
    public LaserBurner(Vehicle anchor, GameLogic game)
    {
        super(anchor, game);

        m_FireMode = new FireControl_Pulse(0.6, 0.06, 1);
        m_ProjectileTemplate = new ProjectileTemplate(this, ModelType.ParticleLaser, GetAffiliation(),
                m_MuzzleVelocity, m_Damage, 500, m_ProjectileFadeInTime, m_ProjectileFadeOutTime, 0.0,
                ProjectileBehaviourType.ParticleLaser, game);

        AddMuzzle(0,0,0);

        m_HasLasers = true;
    }
}
