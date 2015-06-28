package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.utils.Timer;

public class FireControl_Charge extends FireControl
{
	private enum FireState
	{
		Idle,
		Charging,
	}
	
	private FireState m_FireState;
	
	private Timer m_FireTimer;
	private Timer m_ChargeTimer;
	
	public FireControl_Charge(double fireRate, double ChargeRate)
	{
		super();
		
		m_FireState = FireState.Idle;
		
		m_FireTimer 	= new Timer(fireRate);
		m_ChargeTimer 	= new Timer(ChargeRate);
	}
	
	@Override
	public void Update(double deltaTime) 
	{
		switch(m_FireState)
		{
			case Idle:
				m_FireTimer.Update(deltaTime);
				break;
				
			case Charging:
				m_ChargeTimer.Update(deltaTime);
				break;
		}
	}

	@Override
	public boolean CanFire() 
	{
		return m_FireTimer.TimedOut();
	}

	@Override
	public boolean ShouldFire() 
	{
		return(m_ChargeTimer.TimedOut());
	}

	@Override
	public void NotifyOfFire() 
	{
		m_ChargeTimer.ResetTimer();
		m_FireState = FireState.Idle;
	}
	
	@Override
	public void PullTrigger()
	{
		super.PullTrigger();
		
		if(CanFire())
		{
			m_FireState = FireState.Charging;
			m_FireTimer.ResetTimer();
		}
	}
}