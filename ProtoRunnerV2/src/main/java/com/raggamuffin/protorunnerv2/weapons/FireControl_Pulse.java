package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.utils.Timer;

public class FireControl_Pulse extends FireControl
{
	private Timer m_BurstTimer;
	private int m_RoundsInBurst;
	
	private int m_BurstCount;
	
	public FireControl_Pulse(double BurstRate, int RoundsInBurst)
	{
		super();

		m_BurstTimer = new Timer(BurstRate);
		m_RoundsInBurst = RoundsInBurst;
		m_BurstCount = 0;
	}

	@Override
	public void PullTrigger()
    {
		m_BurstTimer.Start();
    }

    @Override
	public void ReleaseTrigger()
	{
		m_BurstTimer.Stop();
		m_BurstCount = 0;
	}

    @Override
    public void Update(double deltaTime)
    {}

    @Override
	public boolean ShouldFire()
	{
		return m_BurstTimer.HasElapsed();
	}
	
	@Override
	public void NotifyOfFire()
	{
		if(m_BurstCount >= m_RoundsInBurst)
		{
            m_BurstTimer.Stop();
            m_BurstCount = 0;
		}
		else
        {
            m_BurstTimer.Start();
            ++m_BurstCount;
        }
	}
}
