package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.utils.Timer;

public class FireControl_BothBarrels extends FireControl
{
    private Weapon m_Anchor;
    private Timer m_FireTimer;
    private final int m_NumMuzzles;
    private int m_MuzzleIndex;
    private boolean m_TriggerLocked;

    public FireControl_BothBarrels(Weapon anchor, double fireRate)
    {
        m_Anchor = anchor;
        m_FireTimer = new Timer(fireRate);

        m_NumMuzzles = m_Anchor.GetNumMuzzles();
        m_MuzzleIndex = 0;

        m_TriggerLocked = false;
    }

    @Override
    public void Update(double deltaTime)
    {
        m_FireTimer.Update(deltaTime);
    }

    @Override
    public boolean CanFire()
    {
        return false;
    }

    @Override
    public boolean ShouldFire()
    {
        return m_FireTimer.TimedOut() && m_TriggerPulled && m_MuzzleIndex < m_NumMuzzles && !m_TriggerLocked;
    }

    @Override
    public void NotifyOfFire()
    {
        m_Anchor.NextMuzzle();
        m_MuzzleIndex++;

        if(m_MuzzleIndex >= m_NumMuzzles)
        {
            m_MuzzleIndex = 0;
            m_FireTimer.ResetTimer();
            m_TriggerLocked = true;
        }
    }

    @Override
    public void PullTrigger()
    {
        super.PullTrigger();
    }

    public void ReleaseTrigger()
    {
        super.ReleaseTrigger();
        m_TriggerLocked = false;
    }
}
