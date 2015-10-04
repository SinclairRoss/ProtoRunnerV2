package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class FireControl_BeamSweep extends FireControl
{
    private enum SweepState
    {
        Initialising,
        Sweeping,
        Shutdown,
        Cooldown
    }

    private SweepState m_State;

    private Timer m_SweepTimer;
    private Timer m_CooldownTimer;

    private Vector3 m_AimTarget;
    private Vector3 m_Aim;
    private Vector3 m_AimVelocity;
    private Vector3 m_ToTarget;

    public FireControl_BeamSweep(AIController controller)
    {
        super(controller);

        m_State = SweepState.Initialising;

        m_SweepTimer = new Timer(8.0);
        m_CooldownTimer = new Timer(5.0);

        m_AimTarget = new Vector3();
        m_Aim = new Vector3();
        m_AimVelocity = new Vector3();
        m_ToTarget = new Vector3();

        m_Weapon.SetTargetVector(m_ToTarget);
    }

    @Override
    public void Update(double deltaTime)
    {
        Vehicle target = m_SituationalAwareness.GetTargetSensor().GetTarget();

        if(target == null)
            return;

        switch(m_State)
        {
            case Initialising:
            {
                m_Aim = target.GetPosition();
                m_Weapon.OpenFire();

                m_SweepTimer.ResetTimer();
                m_State = SweepState.Sweeping;
            }
            case Sweeping:
            {
                m_SweepTimer.Update(deltaTime);

                if (m_SweepTimer.TimedOut())
                    m_State = SweepState.Shutdown;

                break;
            }
            case Shutdown:
            {
                m_Weapon.CeaseFire();

                m_CooldownTimer.ResetTimer();
                m_State = SweepState.Cooldown;
            }
            case Cooldown:
            {
                m_CooldownTimer.Update(deltaTime);

                if (m_CooldownTimer.TimedOut())
                    m_State = SweepState.Initialising;

                break;
            }
        }

        m_ToTarget.SetVectorDifference(m_Anchor.GetPosition(), m_Aim);
    }
}
