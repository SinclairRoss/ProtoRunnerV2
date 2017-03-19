package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer_Accumulation;

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
    private Timer_Accumulation m_AttackCooldown;
    private Timer_Accumulation m_HoldPositionTimer;
    private Timer_Accumulation m_StartSpinTimer;
    private Timer_Accumulation m_AttackDuration;
    private final double TURN_RATE = 0.2;

    public FireControl_LaserSpinner(AIController controller)
    {
        super(controller);

        m_AttackState = AttackState.Wandering;
        m_AttackCooldown = new Timer_Accumulation(5);
        m_HoldPositionTimer = new Timer_Accumulation(2);
        m_StartSpinTimer = new Timer_Accumulation(1);
        m_AttackDuration = new Timer_Accumulation(5);
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
                    double turnRate = MathsHelper.RandomBoolean() ? TURN_RATE : - TURN_RATE;
                    m_Anchor.SetTurnRate(turnRate);
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
                m_Anchor.GetPrimaryWeapon().DeactivateComponent();

                m_AttackState = AttackState.Wandering;

                break;
            }
        }
    }
}
