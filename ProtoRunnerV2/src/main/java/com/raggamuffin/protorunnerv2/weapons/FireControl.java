package com.raggamuffin.protorunnerv2.weapons;

public abstract class FireControl 
{
	protected boolean m_TriggerPulled;

	public FireControl()
	{
		m_TriggerPulled = false;
	}
	
	public abstract void Update(double deltaTime);
	public abstract boolean ShouldFire();
	public abstract void NotifyOfFire();
	
	public void PullTrigger()
	{
		m_TriggerPulled = true;
	}
	
	public void ReleaseTrigger()
	{
		m_TriggerPulled = false;
	}
}
