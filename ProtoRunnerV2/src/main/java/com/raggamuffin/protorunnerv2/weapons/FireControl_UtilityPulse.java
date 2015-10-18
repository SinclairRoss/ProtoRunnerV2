package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.utils.Timer;

public class FireControl_UtilityPulse extends FireControl
{
    private enum FireState
    {
        Idle,
        Firing
    }

    private FireState m_FireState;

    private Weapon m_Anchor;
    private Timer m_FireTimer;
    private Timer m_BurstTimer;
    private int m_RoundsInBurst;

    private int m_BurstCount;

    private boolean m_TriggerLocked;

    public FireControl_UtilityPulse(Weapon anchor, double FireRate, double BurstRate, int RoundsInBurst)
    {
        super();

        m_Anchor = anchor;

        m_FireTimer  	= new Timer(FireRate);
        m_FireTimer.MaxOutTimer();

        m_BurstTimer 	= new Timer(BurstRate);
        m_RoundsInBurst = RoundsInBurst;

        m_FireState 	= FireState.Idle;
        m_BurstCount 	= 0;
    }

    @Override
    public void Update(double DeltaTime)
    {
        m_FireTimer.Update(DeltaTime);

        switch(m_FireState)
        {
            case Idle:
            {
                if(CanFire() && m_TriggerPulled)
                {
                    m_FireState = FireState.Firing;
                    m_FireTimer.ResetTimer();
                    m_TriggerLocked = true;
                }

                break;
            }
            case Firing:
            {
                m_BurstTimer.Update(DeltaTime);

                break;
            }
        }
    }

    public void PullTrigger()
    {
        if(CanFire())
            super.PullTrigger();
    }

    public void ReleaseTrigger()
    {
        super.ReleaseTrigger();
        m_TriggerLocked = false;
    }

    @Override
    public boolean ShouldFire()
    {
        return m_BurstTimer.TimedOut();
    }

    @Override
    public void NotifyOfFire()
    {
        m_BurstTimer.ResetTimer();

        m_BurstCount++;

        if(m_BurstCount >= m_RoundsInBurst)
        {
            m_BurstCount = 0;
            m_FireState  = FireState.Idle;
            m_Anchor.CeaseFire();
        }
    }

    @Override
    public boolean CanFire()
    {
        return m_FireTimer.TimedOut() && !m_TriggerLocked;
    }
}
