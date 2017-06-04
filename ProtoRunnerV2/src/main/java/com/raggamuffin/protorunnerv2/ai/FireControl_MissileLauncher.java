package com.raggamuffin.protorunnerv2.ai;

// Author: Sinclair Ross
// Date:   28/10/2016

import com.raggamuffin.protorunnerv2.utils.Timer_Accumulation;

public class FireControl_MissileLauncher extends FireControl
{
    private enum LaunchState
    {
        Idle,
        Charging,
        Fire
    }

    private LaunchState m_LaunchState;

    private Timer_Accumulation m_IdleTimer;
    private Timer_Accumulation m_ChargeTimer;

    public FireControl_MissileLauncher(AIController controller)
    {
        super(controller);

        m_LaunchState = LaunchState.Idle;

        m_IdleTimer = new Timer_Accumulation(5);
        m_ChargeTimer = new Timer_Accumulation(5);
    }

    @Override
    public void Update(double deltaTime)
    {
        switch (m_LaunchState)
        {
            case Idle:
            {
                if(m_SituationalAwareness.GetTargetSensor().GetTarget() != null)
                {
                    m_IdleTimer.Update(deltaTime);
                }

                if (m_IdleTimer.HasElapsed())
                {
                    m_LaunchState = LaunchState.Charging;
                    m_IdleTimer.ResetTimer();
                    m_Weapon.PullTrigger();
                }

                break;
            }
            case Charging:
            {
                m_ChargeTimer.Update(deltaTime);

                if(m_ChargeTimer.HasElapsed())
                {
                    m_LaunchState = LaunchState.Fire;
                    m_ChargeTimer.ResetTimer();
                }

                break;
            }
            case Fire:
            {
                m_Weapon.ReleaseTrigger();
                m_LaunchState = LaunchState.Idle;

                break;
            }
        }
    }
}
