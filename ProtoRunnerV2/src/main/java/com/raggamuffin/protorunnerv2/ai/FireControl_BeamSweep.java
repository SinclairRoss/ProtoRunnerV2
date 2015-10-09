package com.raggamuffin.protorunnerv2.ai;


import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class FireControl_BeamSweep extends FireControl
{
    private enum SweepState
    {
        Idle,
        Sweeping,
        Shutdown,
        Cooldown
    }

    private SweepState m_State;

    private Timer m_SweepTimer;
    private Timer m_CooldownTimer;

    private Vector3 m_StartPoint;
    private Vector3 m_EndPoint;
    private Vector3 m_Aim;
    private double m_Amount;
    private final double m_InitialOffset;
    private final double m_Speed;

    private Vector3 m_ToTarget;

    public FireControl_BeamSweep(AIController controller)
    {
        super(controller);

        m_State = SweepState.Idle;

        double activeTime = 3.0;

        m_SweepTimer = new Timer(activeTime);
        m_CooldownTimer = new Timer(5.0);

        m_StartPoint = new Vector3();
        m_EndPoint = new Vector3();
        m_Aim = new Vector3();
        m_Amount = 0.0;
        m_InitialOffset = Math.toRadians(30);
        m_Speed = 1.0 / activeTime;

        m_ToTarget = new Vector3();

        m_Weapon.SetTargetVector(m_ToTarget);
    }

    @Override
    public void Update(double deltaTime)
    {
        switch(m_State)
        {
            case Idle:
            {
                Vehicle targetVehicle =  m_SituationalAwareness.GetTargetSensor().GetTarget();

                if(targetVehicle == null)
                    break;

                m_State = SweepState.Sweeping;

                Vector3 targetPos = targetVehicle.GetPosition();


                m_StartPoint.SetVector(targetVehicle.GetForward());
                m_StartPoint.Scale(20.0);
                m_StartPoint.Add(targetPos);

                m_EndPoint.SetVectorDifference(m_StartPoint, targetPos);
                m_EndPoint.Scale(2);
                m_EndPoint.Add(targetPos);

                m_Amount = 0;

                m_SweepTimer.ResetTimer();
                m_Weapon.OpenFire();
            }
            case Sweeping:
            {
                m_SweepTimer.Update(deltaTime);

                m_Amount = MathsHelper.Clamp(m_Amount + (deltaTime * m_Speed), 0, 1);

                m_Aim.Lerp(m_StartPoint, m_EndPoint, m_Amount);
                m_ToTarget.SetVectorDifference(m_Anchor.GetPosition(), m_Aim);
                m_ToTarget.Normalise();

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
                    m_State = SweepState.Idle;

                break;
            }
        }

      //  Log.e("state", "State: " + m_State);
    }
}
