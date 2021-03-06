package com.raggamuffin.protorunnerv2.ai;


import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
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
        m_TelegraphTimer = new Timer(1.25);
        m_CooldownTimer = new Timer(1.5);
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
                    m_Controller.GetEvasionControl().Disable();
                    m_Anchor.SetTurnRate(0);
                    m_Weapon.ActivateComponent();

                    m_TelegraphTimer.Start();
                    m_AttackState = AttackState.Telegraph;
                }

               break;
           }
           case Telegraph:
           {
               if(m_TelegraphTimer.HasElapsed())
               {
                   m_AttackState = AttackState.Fire;
               }

               break;
           }
           case Fire:
           {
                m_Weapon.PullTrigger();
                m_Weapon.DeactivateComponent();
                m_AttackState = AttackState.PostFire;

                break;
           }
           case PostFire:
           {
                if(!m_Weapon.IsFiring())
                {
                    m_Controller.GetNavigationControl().Activate();
                    m_Controller.GetEvasionControl().Enable();

                    m_CooldownTimer.Start();
                    m_AttackState = AttackState.Cooldown;
                }

                break;
           }
           case Cooldown:
           {
                if(m_CooldownTimer.HasElapsed())
                {
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
            m_ToTarget.SetVectorAsDifference(m_Anchor.GetPosition(), target.GetPosition());

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

        if(!GameLogic.TEST_MODE)
        {
          return inSights;
        }
        else
        {
            return true;
        }
    }
}
