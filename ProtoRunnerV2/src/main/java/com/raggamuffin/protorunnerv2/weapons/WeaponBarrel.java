package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.utils.Quaternion;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class WeaponBarrel
{
    private Vector3 m_Position;
    private Quaternion m_Rotation;

    public WeaponBarrel(double x, double y, double z, double rotation)
    {
        m_Position = new Vector3(x, y, z);
        m_Rotation = new Quaternion(Vector3.UP, rotation);
    }

    public Vector3 GetPosition()
    {
        return m_Position;
    }

    public Quaternion GetRotation()
    {
        return m_Rotation;
    }
}
