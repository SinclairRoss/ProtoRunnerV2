package com.raggamuffin.protorunnerv2.weapons;

import android.util.Log;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.renderer.ModelType;

public class LaserBurner extends Weapon
{
    public LaserBurner(Vehicle anchor, GameLogic game)
    {
        super(anchor, game);

        m_FireMode = new FireControl_None();
        m_ProjectileTemplate = new ProjectileTemplate(this, ModelType.ParticleLaser,
                m_MuzzleVelocity, 100, 0.5, 0.0,
                ProjectileBehaviourType.ParticleLaser, game);

        AddMuzzle(0,0,0);

        m_HasLasers = true;
    }

    @Override
    public void OpenFire()
    {
        super.OpenFire();

        Log.e("Open", "Fire");

        m_BulletManager.CreateBullet(m_ProjectileTemplate);
        m_PostFireAction.Update();
    }
}
