package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public class Weapon_LaserVampire extends Weapon
{
    public Weapon_LaserVampire(Vehicle anchor, GameLogic game)
    {
        super(anchor, game);

        m_Damage = 100;

        m_FireMode = new FireControl_None();

        AddBarrel(0, 0, 0);

        m_ProjectileType = ProjectileType.LaserVampire;
    }

    @Override
    public void OpenFire()
    {
        super.OpenFire();

        m_BulletManager.CreateProjectile(this);
    }
}
