package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.utils.Timer_Accumulation;

public class FireControl_Burst extends FireControl
{
	private Timer_Accumulation m_FireTimer;
	private int m_ShotsInBurst;
	private int m_ShotsFired;
	
	public FireControl_Burst(double fireRate, int shotsInBurst)
	{
		super();
	
		m_FireTimer 	= new Timer_Accumulation(fireRate);
		m_ShotsInBurst 	= shotsInBurst;
		m_ShotsFired 	= m_ShotsInBurst;
	}

	@Override
	public void Update(double deltaTime) 
	{
		m_FireTimer.Update(deltaTime);
	}

	@Override
	public boolean CanFire() 
	{
		return m_FireTimer.TimedOut();
	}

	@Override
	public boolean ShouldFire() 
	{		
		if(m_ShotsFired < m_ShotsInBurst)
		{
			m_ShotsFired++;
			return true;
		}
		
		return false;
	}

	@Override
	public void NotifyOfFire() 
	{
		m_FireTimer.ResetTimer();
	}
	
	@Override
	public void PullTrigger()
	{
		super.PullTrigger();
		
		if(CanFire())
		{
			m_ShotsFired = 0;
		}
	}
}