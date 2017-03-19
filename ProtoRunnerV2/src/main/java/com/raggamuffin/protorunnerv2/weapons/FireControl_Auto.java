package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.utils.Timer;

public class FireControl_Auto extends FireControl
{
	private Timer m_FireTimer;
	
	public FireControl_Auto(double fireRate)
	{
		super();
		
		m_FireTimer = new Timer(fireRate);
		m_FireTimer.Start();
	}

	@Override
	public void Update(double deltaTime)
	{}

	@Override
	public boolean CanFire() 
	{
		return m_FireTimer.HasElapsed();
	}
	
	@Override
	public boolean ShouldFire()
	{
		return m_TriggerPulled && CanFire();
	}
	
	@Override
	public void NotifyOfFire()
	{
		m_FireTimer.Start();
	}	
}
