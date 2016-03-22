package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class Projectile extends GameObject
{
	protected Weapon m_Origin;
    protected double m_BaseDamage;

    private ProjectileType m_Type;

	public Projectile(Weapon origin)
	{
		super(null, null);

        m_Origin = origin;
        m_Type = origin.GetProjectileType();

		m_Model 			= ModelType.PlasmaShot;
		m_BoundingRadius 	= 0.0;
		m_Mass 				= 1000;

        m_BaseDamage = m_Origin.GetBaseDamage();

        m_Origin.GetFirePosition(m_Position);
        m_Origin.CalculateProjectileHeading(m_Forward);
        m_Yaw = m_Origin.GetOrientation();

        m_BaseColour.SetColour(m_Origin.GetAnchor().GetBaseColour());
        m_AltColour.SetColour(m_Origin.GetAnchor().GetAltColour());

        SetAffiliation(m_Origin.GetAffiliation());

		SetDragCoefficient(1.0);
	}

	@Override
	public boolean IsValid() 
	{
		if(IsForciblyInvalidated())
			return false;
		
		return true;
	}

    public abstract boolean CollidesWith(GameObject other);

	public abstract void CollisionResponse(GameObject other);

	public Weapon GetOrigin()
	{
		return m_Origin;
	}

    public ProjectileType GetProjectileType()
    {
        return m_Type;
    }

    public double GetDamageOutput()
    {
        return m_BaseDamage;
    }
}