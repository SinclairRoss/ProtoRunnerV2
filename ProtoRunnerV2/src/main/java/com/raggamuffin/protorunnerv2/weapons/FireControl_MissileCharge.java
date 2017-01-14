package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.utils.Timer_Accumulation;

public class FireControl_MissileCharge extends FireControl
{
	private Timer_Accumulation m_SpawnTimer;
	
	private Weapon m_Anchor;
	private int m_MaxChargeCount;
	private int m_ChargeCount;
	
	public FireControl_MissileCharge(Weapon anchor, double fireRate, int maxCount)
	{
		super();

		m_SpawnTimer = new Timer_Accumulation(fireRate);
		
		m_Anchor = anchor;
		m_MaxChargeCount = maxCount;
		m_ChargeCount = 0;
	}
	
	@Override
	public void Update(double deltaTime) 
	{
		m_SpawnTimer.Update(deltaTime);
	}

	@Override
	public boolean CanFire() 
	{
		return m_SpawnTimer.TimedOut();
	}

	@Override
	public boolean ShouldFire() 
	{
		return  m_TriggerPulled  && 
				CanFire() 		 && 
				m_ChargeCount < m_MaxChargeCount;
	}

	@Override
	public void NotifyOfFire() 
	{
		m_SpawnTimer.ResetTimer();
		m_ChargeCount++;
	}
	
	@Override
	public void PullTrigger()
	{
		super.PullTrigger();
		
		m_SpawnTimer.ResetTimer();	
	}
	
	@Override
	public void ReleaseTrigger()
	{
		super.ReleaseTrigger();
		
		m_ChargeCount = 0;
		m_Anchor.ResetMuzzleIndex();
	}
}
