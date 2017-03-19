package com.raggamuffin.protorunnerv2.weapons;

// Author: Sinclair Ross
// Date:   16/03/2017

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.Timer;

public class Weapon_BitLaser extends Weapon
{
    private Timer m_DurationTimer;

    public Weapon_BitLaser(Vehicle anchor, GameLogic game)
    {
        super(anchor, game, AudioClips.Laser_Enemy);

        m_ProjectileType = ProjectileType.Laser;

        m_Damage = 100;
        m_FiringSpeed = 140.0;
        m_Accuracy = 1.0;
        m_LifeSpan = 2.0;

        m_FireMode = new FireControl_None();

        AddBarrel(0, 0, 5);

        m_WeaponComponent = InitialiseWeaponComponent(EWeaponComponents.LaserPointer);

        m_DurationTimer = new Timer(0.3);
    }

    @Override
    public void Update(double deltaTime)
    {
        m_WeaponComponent.Update(deltaTime);

        m_TriggerPulled = !m_DurationTimer.HasElapsed();
    }

    @Override
    public void OpenFire()
    {
        super.OpenFire();

        m_BulletManager.CreateProjectile(this);
        NextBarrel();

        m_DurationTimer.Start();
    }

    @Override
    public void CeaseFire()
    {}
}
