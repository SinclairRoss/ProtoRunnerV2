package com.raggamuffin.protorunnerv2.weapons;

// Author: Sinclair Ross
// Date:   08/01/2017

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Weapon_ChargeLaser extends Weapon
{
    private final double SPAWN_DISTANCE = 3.0;

    private Timer m_ChargeDelayTimer;
    private Timer m_ChargeTimer;

    private Projectile_HomingLaser m_Projectile;
    private Vector3 m_FirePosition;

    public Weapon_ChargeLaser(Vehicle anchor, GameLogic game)
    {
        super(anchor, game, ProjectileType.HomingLaser, AudioClips.UI_Positive);

        m_ChargeDelayTimer = new Timer(1.0);
        m_ChargeTimer = new Timer(1.0);

        m_Projectile = null;
        m_FirePosition = new Vector3();
    }

    @Override
    public void Update(double deltaTime)
    {
        if(m_ChargeDelayTimer.HasElapsed())
        {
            m_ChargeDelayTimer.Stop();
            m_ChargeTimer.Start();

            m_Projectile = (Projectile_HomingLaser) m_Game.GetBulletManager().CreateProjectile(this);
        }

        if(m_Projectile != null)
        {
            m_Projectile.SetGrowth(m_ChargeTimer.GetProgress());
            m_Projectile.SetPosition(GetFirePosition());
            m_Projectile.SetForward(m_Anchor.GetForward());
        }
    }

    @Override
    public void PullTrigger()
    {
        m_ChargeDelayTimer.Start();
    }

    @Override
    public void ReleaseTrigger()
    {
        if(m_ChargeTimer.HasElapsed())
        {
            m_ChargeTimer.Stop();

            if(m_Projectile != null)
            {
                m_Projectile.Release();
                m_Projectile = null;
            }
        }
        else
        {
            if(m_Projectile != null)
            {
                m_Projectile.Destroy();
                m_Projectile = null;
            }
        }
    }

    @Override
    public Vector3 GetFirePosition()
    {
        UpdateFirePosition();
        return m_FirePosition;
    }

    private void UpdateFirePosition()
    {
        m_FirePosition.SetVector(m_Anchor.GetForward());
        m_FirePosition.Scale(SPAWN_DISTANCE);
        m_FirePosition.Add(m_Anchor.GetPosition());
    }
}
