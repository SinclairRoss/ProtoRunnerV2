package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.utils.Timer;

public class FireControl_LaserSpinner extends FireControl
{
    private enum AttackState
    {
        Wandering,
        ComingToAHalt,
        HoldingPosition,
        StartingAttack,
        StartingSpin,
        Attacking,
        CeasingAttack
    }

    private AttackState m_AttackState;
    private Timer m_AttackCooldown;
    private Timer m_HoldPositionTimer;
    private Timer m_StartSpinTimer;
    private Timer m_AttackDuration;

    public FireControl_LaserSpinner(AIController controller)
    {
        super(controller);

        m_AttackState = AttackState.Wandering;
        m_AttackCooldown = new Timer(5);
        m_HoldPositionTimer = new Timer(2);
        m_StartSpinTimer = new Timer(1);
        m_AttackDuration = new Timer(5);
    }

    @Override
    public void Update(double deltaTime)
    {
        switch(m_AttackState)
        {
            case Wandering:
            {
                m_AttackCooldown.Update(deltaTime);

                if(m_AttackCooldown.TimedOut())
                {
                    m_AttackState = AttackState.ComingToAHalt;
                    m_AttackCooldown.ResetTimer();
                }

                break;
            }
            case ComingToAHalt:
            {
                m_Controller.GetNavigationControl().Deactivate();
                m_Anchor.SetEngineOutput(0.0);
                m_Anchor.SetTurnRate(0.0);
                m_Anchor.GetPrimaryWeapon().ActivateComponent();

                m_AttackState = AttackState.HoldingPosition;
            }
            case HoldingPosition:
            {
                m_HoldPositionTimer.Update(deltaTime);

                if(m_HoldPositionTimer.TimedOut())
                {
                    m_AttackState = AttackState.StartingAttack;
                    m_HoldPositionTimer.ResetTimer();
                }

                break;
            }
            case StartingAttack:
            {
                m_Anchor.GetPrimaryWeapon().OpenFire();

                m_AttackState = AttackState.StartingSpin;
            }
            case StartingSpin:
            {
                m_StartSpinTimer.Update(deltaTime);

                if(m_StartSpinTimer.TimedOut())
                {
                    m_Anchor.DisableRoll();
                    m_Anchor.SetTurnRate(0.5);
                    m_AttackState = AttackState.Attacking;
                    m_StartSpinTimer.ResetTimer();
                }

                break;
            }
            case Attacking:
            {
                m_AttackDuration.Update(deltaTime);

                if(m_AttackDuration.TimedOut())
                {
                    m_AttackDuration.ResetTimer();
                    m_AttackState = AttackState.CeasingAttack;
                }

                break;
            }
            case CeasingAttack:
            {
                m_Anchor.GetPrimaryWeapon().CeaseFire();
                m_Controller.GetNavigationControl().Activate();
                m_Controller.GetAnchor().SetEngineOutput(1.0);
                m_Anchor.EnableRoll();
                m_Anchor.GetPrimaryWeapon().DeactivateComponent();

                m_AttackState = AttackState.Wandering;

                break;
            }
        }
    }
}
