package com.raggamuffin.protorunnerv2.weapons;

import java.util.ArrayList;

import com.raggamuffin.protorunnerv2.colours.ColourBehaviour;
import com.raggamuffin.protorunnerv2.colours.ColourBehaviour_FadeTo;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Projectile extends GameObject
{
	private final double DEFAULT_MASS = 1000;
	
	private double m_BaseDamage;
	
	private double m_MaxLifeSpan;
	private double m_LifeSpan;
	private ProjectileBehaviour m_Behaviour;
	private ArrayList<SpecialProjectileBehaviour> m_SpecialBehaviours;
	private Weapon m_Origin;
	
	private ColourBehaviour_FadeTo m_SpawnBehaviour;

	public Projectile()
	{
		super(null, null);

		m_Model 			= ModelType.PulseLaser;
		m_BoundingRadius 	= 0.0;
		m_Mass 				= DEFAULT_MASS;
		
		m_BaseDamage = 0.0f;
		m_LifeSpan   = 0.0f;
		m_Behaviour  = null;
		
		m_BaseColour.SetColour(Colours.Black);
		SetDragCoefficient(0.0);
		
		m_SpawnBehaviour = new ColourBehaviour_FadeTo(this, ColourBehaviour.ActivationMode.Triggered, 0.3);
		AddColourBehaviour(m_SpawnBehaviour);
	}
	
	public void Update(double deltaTime)
	{
		m_LifeSpan -= deltaTime;
		
		m_SpawnBehaviour.Update(deltaTime);
		m_Behaviour.Update(deltaTime);
		
		super.Update(deltaTime);
	}

	public void ResetLifeSpan()
	{
		m_LifeSpan = m_MaxLifeSpan;
	}
	
	public void Activate(ProjectileTemplate template)
	{
		RemoveAllChildren();
		ResetForcedInvalidation();
		
		m_Origin = template.GetOrigin();

		m_BaseColour.SetColour(Colours.Black);
		m_SpawnBehaviour.SetNextColour(m_Origin.GetBulletColour());
		m_SpawnBehaviour.SetTimeFrame(template.GetFadeInTime());
		m_SpawnBehaviour.TriggerBehaviour();	
		
		m_Model = template.GetModel();
		SetAffiliation(template.GetAffiliation());

		m_BaseDamage = template.GetBaseDamage();
		
		m_MaxLifeSpan = template.GetLifeSpan();
		m_LifeSpan = m_MaxLifeSpan;

		m_Behaviour	= template.GetBehaviour(this);
		m_SpecialBehaviours = template.GetSpecialBehaviour(this);
		ActivateSpecialBehaviours();

		m_Position.SetVector(m_Origin.GetFirePosition());
		m_Velocity.SetVector(m_Origin.GetVelocity());
		CalculateForward(m_Origin.GetForward(), m_Origin.GetAccuracy());
		m_Orientation = m_Origin.GetOrientation();

		double muzzleVelocity = template.GetMuzzleVelocity();
		m_Velocity.I += m_Forward.I * muzzleVelocity;
		m_Velocity.J += m_Forward.J * muzzleVelocity;
		m_Velocity.K += m_Forward.K * muzzleVelocity;
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
		{
			DeactivateSpecialBehaviours();
			m_Behaviour.CleanUp();
			m_BaseColour.SetColour(Colours.Black);
			return false;
		}
		
		if(m_LifeSpan <= 0.0)
		{
			DeactivateSpecialBehaviours();
			m_Behaviour.CleanUp();
			m_BaseColour.SetColour(Colours.Black);
			return false;
		}
		
		return true;
	}
	
	private void ActivateSpecialBehaviours()
	{
		for(SpecialProjectileBehaviour behaviour : m_SpecialBehaviours)
		{
			behaviour.Activate();
		}
	}
	
	private void DeactivateSpecialBehaviours()
	{
		for(SpecialProjectileBehaviour behaviour : m_SpecialBehaviours)
		{
			behaviour.Deactivate();
		}
	}
	
	public void CollisionResponse(GameObject Collider)
	{
		m_Behaviour.CollisionResponce();
	}
	
	public double GetBaseDamage()
	{
		return m_BaseDamage;
	}
	
	public Weapon GetFiringWeapon()
	{
		return m_Origin;
	}
	
	public void ResetMass()
	{
		m_Mass = DEFAULT_MASS;
	}

    public ProjectileBehaviour GetProjectileBehaviour()
    {
        return m_Behaviour;
    }
}