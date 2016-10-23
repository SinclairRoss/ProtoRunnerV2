package com.raggamuffin.protorunnerv2.ai;


import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class FireControl_Telegraphed extends FireControl
{
    private enum AttackState
    {
        Idle,
        Telegraph,
        Fire,
        PostFire,
        Cooldown
    }

    private AttackState m_AttackState;

    private Vector3 m_ToTarget;

    private double m_FireArc;
	private double m_Range;

    private Timer m_TelegraphTimer;
    private Timer m_CooldownTimer;
	
	public FireControl_Telegraphed(AIController controller)
	{
        super(controller);

        m_AttackState = AttackState.Idle;

        m_ToTarget = new Vector3();

        m_Range = 100.0f;
        m_FireArc = Math.toRadians(1);
        m_TelegraphTimer = new Timer(0.75);
        m_CooldownTimer = new Timer (1.5);
	}

    @Override
    public void Update(double deltaTime)
    {
        switch(m_AttackState)
        {
            case Idle:
            {
                Vehicle target = m_SituationalAwareness.GetTargetSensor().GetTarget();

                if(IsTargetInSights(target))
                {
                    m_Controller.GetNavigationControl().Deactivate();
                    m_Anchor.SetTurnRate(0);
                    m_Weapon.ActivateComponent();

                    m_AttackState = AttackState.Telegraph;
                }

                break;
            }
            case Telegraph:
            {
                m_TelegraphTimer.Update(deltaTime);

                if(m_TelegraphTimer.TimedOut())
                {
                    m_AttackState = AttackState.Fire;
                    m_TelegraphTimer.ResetTimer();
                }

                break;
            }
            case Fire:
            {
                m_Weapon.OpenFire();
                m_Weapon.DeactivateComponent();
                m_Controller.GetNavigationControl().Activate();
                m_AttackState = AttackState.PostFire;

                break;
            }
            case PostFire:
            {
                m_Weapon.CeaseFire();
                m_AttackState = AttackState.Cooldown;
            }
            case Cooldown:
            {
                m_CooldownTimer.Update(deltaTime);

                if(m_CooldownTimer.TimedOut())
                {
                    m_CooldownTimer.ResetTimer();
                    m_AttackState = AttackState.Idle;
                }

                break;
            }
        }
    }

    private boolean IsTargetInSights(Vehicle target)
    {
        boolean inSights = false;

        if(target != null)
        {
            // Is target within range.
            m_ToTarget.SetVectorDifference(m_Anchor.GetPosition(), target.GetPosition());

            if (m_ToTarget.GetLengthSqr() <= m_Range * m_Range)
            {
                // Is target within firing arc.
                m_ToTarget.Normalise();
                double radiansBetween = Vector3.RadiansBetween(m_ToTarget, m_Anchor.GetForward());

                if (radiansBetween <= m_FireArc)
                {
                    inSights = true;
                }
            }
        }

        return inSights;
    }
}
