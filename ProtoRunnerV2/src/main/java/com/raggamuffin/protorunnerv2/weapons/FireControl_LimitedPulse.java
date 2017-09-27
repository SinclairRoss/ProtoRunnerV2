package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.utils.Timer;

public class FireControl_LimitedPulse extends FireControl
{
	private Timer m_BurstTimer;
	private Timer m_FireRateTimer;

	private int m_RoundsInBurst;

	private int m_BurstCount;

	public FireControl_LimitedPulse(double BurstRate, double fireRate, int RoundsInBurst)
	{
		super();

		m_BurstTimer = new Timer(BurstRate);
		m_FireRateTimer = new Timer(fireRate);
		m_FireRateTimer.StartElapsed();

		m_RoundsInBurst = RoundsInBurst;
		m_BurstCount = 0;
	}

	@Override
	public void PullTrigger()
    {
		if(m_FireRateTimer.HasElapsed())
		{
			m_BurstTimer.StartElapsed();
			m_FireRateTimer.Start();
		}
	}

    @Override
	public void ReleaseTrigger()
	{}

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
