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

	public Projectile(Weapon origin)
	{
		super(null, null);

        m_Origin = origin;

		m_Model 			= ModelType.PlasmaShot;
		m_BoundingRadius 	= 0.0;
		m_Mass 				= 1000;

		m_BaseDamage = 0.0f;

        m_Position.SetVector(m_Origin.GetFirePosition());
        CalculateForward(m_Origin.GetForward(), m_Origin.GetAccuracy());
        m_Yaw = m_Origin.GetOrientation();

        m_BaseColour.SetColour(m_Origin.GetAnchor().GetBaseColour());
        m_AltColour.SetColour(m_Origin.GetAnchor().GetAltColour());

        SetAffiliation(m_Origin.GetAffiliation());

		SetDragCoefficient(0.0);
	}

	public void Update(double deltaTime)
	{
		super.Update(deltaTime);
	}

	private void CalculateForward(Vector3 weaponForward, double accuracy)
	{
		m_Forward.SetVector(weaponForward);
		
		double Min = -(Math.PI * 0.5) * (1.0 - accuracy);	// 90 Degrees multiplied by value between 0 and 1.
		double Max =  (Math.PI * 0.5) * (1.0 - accuracy);
	 
		double Theta;
		
		Theta = MathsHelper.RandomDouble(Min, Max);
		m_Forward.RotateY(Theta);
		
		Theta = MathsHelper.RandomDouble(Min, Max);
		m_Forward.RotateX(Theta * 0.25);
	}

	@Override
	public boolean IsValid() 
	{
		if(IsForciblyInvalidated())
			return false;
		
		return true;
	}

    public abstract boolean CollidesWith(Vehicle other);

	public abstract void CollisionResponse(Vehicle other);

	public Weapon GetOrigin()
	{
		return m_Origin;
	}

    public double GetDamageOutput(double deltaTime)
    {
        return 0.0;
    }
}