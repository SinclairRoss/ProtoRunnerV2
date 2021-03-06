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
    private Projectile m_ActiveProjectile;

    public Weapon_BitLaser(Vehicle anchor, GameLogic game)
    {
        super(anchor, game, ProjectileType.Laser, AudioClips.Laser_Enemy);

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
        m_TriggerPulled = !m_DurationTimer.HasElapsed();
    }

    @Override
    public void PullTrigger()
    {
        super.PullTrigger();

        m_ActiveProjectile = m_Game.GetBulletManager().CreateProjectile(this);
        NextBarrel();

        m_DurationTimer.Start();
        m_AudioEmitter.Start();

        if(!m_ActiveProjectile.IsValid())
        {
            m_ActiveProjectile = null;
        }
    }

    @Override
    public void ReleaseTrigger()
    {}

    @Override
    public boolean IsFiring()
    {
        return m_ActiveProjectile != null && m_ActiveProjectile.IsValid();
    }
}
