package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.vehicles.Vehicle;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.weapons.Weapon;

public class FireControl 
{
	private AIController m_Controller;
	private SituationalAwareness m_SituationalAwareness;
	private Weapon m_Weapon;
	
	private double m_Range;
	
	public FireControl(AIController Controller)
	{
		m_Controller = Controller;
		m_SituationalAwareness = m_Controller.GetSituationalAwareness();
		m_Weapon = m_Controller.GetAnchor().GetPrimaryWeapon();
		
		m_Range= 1000.0f;
	}
	
	public void Update()
	{
		Vehicle Target = m_SituationalAwareness.GetTargetSensor().GetTarget();
		
		// Don't fire if there is no target.
		if(Target == null)
		{
			m_Weapon.CeaseFire();
			return;
		}

		// If the target is not in sight: Cease fire.
		if(CollisionDetection.RayTrace(m_Weapon.GetPosition(), m_Weapon.GetForward(), m_Range, Target))
			m_Weapon.OpenFire();
		else
			m_Weapon.CeaseFire();
	}
}
