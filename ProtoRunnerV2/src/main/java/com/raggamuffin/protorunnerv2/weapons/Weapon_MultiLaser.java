package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Weapon_MultiLaser extends Weapon
{
    private final int m_NumBeams;

    public Weapon_MultiLaser(Vehicle anchor, GameLogic game, int numBeams)
    {
        super(anchor, game);

        m_NumBeams = numBeams;
        double deltaRotation = (Math.PI * 2) / m_NumBeams;

        m_Damage = 100;

        m_FireMode = new FireControl_None();

        for(int i = 0; i < m_NumBeams; i++)
        {
            AddBarrel(0, 0, 0, deltaRotation * i);
        }

        m_ProjectileType = ProjectileType.Laser;
    }

    @Override
    public void OpenFire()
    {
        super.OpenFire();

        for(int i = 0; i < m_NumBeams; i ++)
        {
            m_BulletManager.CreateProjectile(this);
            NextBarrel();
        }
    }

    @Override
    protected void CalculateProjectileHeading(Vector3 out)
    {
        WeaponBarrel barrel = GetActiveWeaponBarrel();
        out.SetVector(m_Anchor.GetForward());
        out.RotateY(GetMuzzleIndex() * barrel.GetRotation());
    }
}
