package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Weapon_MultiLaser extends Weapon
{
    private final int m_NumBeams;

    public Weapon_MultiLaser(Vehicle anchor, GameLogic game, int numBeams)
    {
        super(anchor, game, ProjectileType.Laser, AudioClips.Laser_Enemy);

        m_NumBeams = numBeams;
        double deltaRotation = (Math.PI * 2) / m_NumBeams;

        m_Damage = 100;

        m_FireMode = new FireControl_None();

        for(int i = 0; i < m_NumBeams; i++)
        {
            AddBarrel(0, 0, 0, deltaRotation * i);
        }

        m_WeaponComponent = InitialiseWeaponComponent(EWeaponComponents.LaserPointer);
    }

    @Override
    public void Update(double deltaTime)
    {
        m_WeaponComponent.Update(deltaTime);
    }

    @Override
    public void PullTrigger()
    {
        super.PullTrigger();

        for(int i = 0; i < m_NumBeams; i ++)
        {
            m_Game.GetBulletManager().CreateProjectile(this);
            NextBarrel();
        }
    }

    public Vector3 CalculateProjectileHeading()
    {
        WeaponBarrel barrel = GetActiveWeaponBarrel();

        Vector3 out = new Vector3(m_Anchor.GetForward());
        out.Rotate(barrel.GetRotation());

        return out;
    }
}