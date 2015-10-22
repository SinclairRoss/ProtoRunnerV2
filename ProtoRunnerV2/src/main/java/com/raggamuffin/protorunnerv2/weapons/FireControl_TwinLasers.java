package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.utils.Timer;

public class FireControl_TwinLasers extends FireControl
{
    private enum FireState
    {
        Idle,
        Bursting,
        PostFire
    }

    private FireState m_FireState;

    private Weapon m_Anchor;
    private Timer m_FireTimer;
    private Timer m_BurstTimer;

    private int m_RoundsInBurst;
    private int m_RoundCount;

    private int m_NumBursts;
    private int m_BurstCount;

    public FireControl_TwinLasers(Weapon anchor, double fireRate, double burstRate)
    {
        super();

        m_Anchor        = anchor;
        m_FireTimer  	= new Timer(fireRate);
        m_BurstTimer 	= new Timer(burstRate);
        m_BurstTimer.MaxOutTimer();

        m_RoundsInBurst = anchor.GetNumMuzzles();
        m_RoundCount    = 0;

        m_NumBursts     = 3;
        m_BurstCount    = 0;

        m_FireState 	= FireState.Idle;
    }

    @Override
    public void Update(double DeltaTime)
    {
        m_FireTimer.Update(DeltaTime);

        switch(m_FireState)
        {
            case Idle:
            {
                if(CanFire())
                {
                    m_FireState = FireState.Bursting;
                    m_FireTimer.ResetTimer();
                }

                break;
            }
            case Bursting:
            {
                m_BurstTimer.Update(DeltaTime);

                break;
            }
            case PostFire:
                break;
        }
    }

    public void ReleaseTrigger()
    {
        super.ReleaseTrigger();

        m_BurstTimer.MaxOutTimer();
        m_FireState = FireState.Idle;
        m_BurstCount = 0;
        m_RoundCount = 0;
    }

    @Override
    public boolean ShouldFire()
    {
        return m_BurstTimer.TimedOut() && m_FireState == FireState.Bursting;
    }

    @Override
    public void NotifyOfFire()
    {
        m_Anchor.NextMuzzle();

        m_RoundCount++;

        if(m_RoundCount >= m_RoundsInBurst)
        {
            m_RoundCount = 0;

            m_BurstCount ++;
            m_BurstTimer.ResetTimer();
            m_FireState  = FireState.Bursting;

            if(m_BurstCount >= m_NumBursts)
            {
                m_FireState = FireState.PostFire;
                m_BurstCount = 0;
            }
        }
    }

    @Override
    public boolean CanFire()
    {
        return m_FireTimer.TimedOut() && m_TriggerPulled;
    }
}
