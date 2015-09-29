package com.raggamuffin.protorunnerv2.ai;

import java.util.ArrayList;
import java.util.Vector;

import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.Projectile;

public class Sensor_IncomingDanger extends Sensor
{
	private ArrayList<Projectile> m_Bullets;
	private ArrayList<Projectile> m_IncomingProjectiles;
	private Vector3 m_ReltivePosition;
	private Vector3 m_RelativeVelocity;
	
	public Sensor_IncomingDanger(AIController controller, BulletManager bManager) 
	{
		super(controller.GetAnchor(), 10);
		
		m_Bullets = bManager.GetActiveBullets();
		m_IncomingProjectiles = new ArrayList<>();
		m_ReltivePosition = new Vector3();
		m_RelativeVelocity = new Vector3();
	}

	@Override
	public void Update() 
	{
		m_IncomingProjectiles.clear();
		
		for(Projectile proj : m_Bullets)
		{
			if(proj.GetAffiliation() == m_Anchor.GetAffiliation())
				continue;
			
			m_ReltivePosition.SetVectorDifference(m_Anchor.GetPosition(), proj.GetPosition());
			
			if(m_ReltivePosition.GetLengthSqr() > m_SensorRadius * m_SensorRadius)
				continue;
			
			m_ReltivePosition.Normalise();
			m_RelativeVelocity.SetVectorDifference(m_Anchor.GetVelocity(), proj.GetVelocity());
			m_RelativeVelocity.Normalise();
			
			double dotProduct = Vector3.DotProduct(m_ReltivePosition, m_RelativeVelocity);
		
			if(dotProduct > 0)
				m_IncomingProjectiles.add(proj);
		}
	}
	
	public ArrayList<Projectile> GetIncomingProjectiles()
	{
		return m_IncomingProjectiles;
	}
}
