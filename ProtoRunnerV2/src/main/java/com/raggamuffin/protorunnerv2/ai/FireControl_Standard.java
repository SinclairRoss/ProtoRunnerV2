package com.raggamuffin.protorunnerv2.ai;


import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class FireControl_Standard extends FireControl
{
    private Vector3 m_ToTarget;

    private double m_FireArc;
    private double m_Range;

    private Timer m_TelegraphTimer;
    private Timer m_CooldownTimer;

    public FireControl_Standard(AIController controller)
    {
        super(controller);

        m_ToTarget = new Vector3();

        m_Range = 100.0f;
        m_FireArc = Math.toRadians(1);
    }

    @Override
    public void Update(double deltaTime)
    {
        m_Weapon.CeaseFire();

        Vehicle target = m_SituationalAwareness.GetTargetSensor().GetTarget();

        if(IsTargetInSights(target))
        {
            m_Weapon.OpenFire();
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
