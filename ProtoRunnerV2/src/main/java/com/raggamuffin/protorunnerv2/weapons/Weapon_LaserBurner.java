package com.raggamuffin.protorunnerv2.weapons;

import android.util.Log;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.renderer.ModelType;

public class Weapon_LaserBurner extends Weapon
{
    public Weapon_LaserBurner(Vehicle anchor, GameLogic game)
    {
        super(anchor, game);

        m_FireMode = new FireControl_None();

        AddMuzzle(0,0,0);

        m_ProjectileType = ProjectileType.Laser;
    }

    @Override
    public void OpenFire()
    {
        super.OpenFire();

        Log.e("Open", "Fire");

        m_BulletManager.CreateProjectile(this);
        m_PostFireAction.Update();
    }
}
