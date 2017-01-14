package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.utils.Timer_Accumulation;

public class FireControl_Auto extends FireControl
{
	private Timer_Accumulation m_FireTimer;
	
	public FireControl_Auto(double fireRate)
	{
		super();
		
		m_FireTimer = new Timer_Accumulation(fireRate);
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
		return m_TriggerPulled && CanFire();
	}
	
	@Override
	public void NotifyOfFire()
	{
		m_FireTimer.ResetTimer();
	}	
}
