package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ProjectileBehaviour_Laser extends ProjectileBehaviour
{
    private Vector3 m_DockedPosition;
    private Vector3 m_Offset;
    private Weapon m_FiringWeapon;

    public ProjectileBehaviour_Laser(Projectile anchor, Vector3 muzzlePos)
    {
        super(anchor);

        m_DockedPosition = new Vector3();
        m_Offset = new Vector3(muzzlePos);
        m_FiringWeapon = m_Anchor.GetFiringWeapon();

        m_Anchor.SetScale(200.0);
    }

    @Override
    public void Update(double deltaTime)
    {
        LockProjectile();
    }

    private void LockProjectile()
    {
        m_DockedPosition.SetVector(m_Offset);
        m_DockedPosition.RotateY(m_FiringWeapon.GetOrientation());
        m_DockedPosition.Add(m_FiringWeapon.GetPosition());

        m_Anchor.SetYaw(m_FiringWeapon.GetOrientation());
        m_Anchor.SetPosition(m_DockedPosition);
    }

    @Override
    public boolean UseSimpleCollisionDetection()
    {
        return false;
    }

    @Override
    public void CleanUp()
    {
        m_Anchor.SetScale(1.0);
    }
}
