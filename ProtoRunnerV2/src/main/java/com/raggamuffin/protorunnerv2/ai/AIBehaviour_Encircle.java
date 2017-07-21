package com.raggamuffin.protorunnerv2.ai;

import android.util.Log;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class AIBehaviour_Encircle extends AIBehaviour
{
    private static final double ARBITRARY_ROTATION = Math.toRadians(15);
    private static final double IDEAL_RANGE = 80;
    private static final double MINIMUM_RANGE = 10;
    private static final double PANIC_BARRIER = 40;
    private static final double FALL_FROM_TARGET_RATE = 5.0;

    private static final double SWAP_DIRECTION_TIME_UPPER_MAX = 20;
    private static final double SWAP_DIRACTION_TIME_UPPER_MIN = 10;

    private static final double SWAP_DIRECTION_TIME_LOWER_MAX = 3;
    private static final double SWAP_DIRACTION_TIME_LOWER_MIN = 1;

    private Timer m_Timer_SwapDirection;

    private Vector3 m_Goal;
    private double m_PreviousDistance;

    private double m_ArbitraryRotation;

    public AIBehaviour_Encircle(AIController controller)
    {
        super(controller);

        m_Goal = new Vector3();
        m_ArbitraryRotation = ARBITRARY_ROTATION;

        m_Timer_SwapDirection = new Timer(SWAP_DIRACTION_TIME_UPPER_MIN);
        m_Timer_SwapDirection.Start();
    }

    @Override
    public Vector3 GetNavigationCoordinates()
    {
        m_Goal.SetVector(0);

        Vehicle target = m_Controller.GetSituationalAwareness().GetTargetSensor().GetTarget();

        if(target != null)
        {
            Vector3 pos = m_Controller.GetAnchor().GetPosition();

            m_Goal.SetVectorAsDifference(target.GetPosition(), pos);

            double distance = m_Goal.GetLength();
            double deltaScale = (IDEAL_RANGE > distance) ? FALL_FROM_TARGET_RATE : -FALL_FROM_TARGET_RATE;


            if(m_PreviousDistance > PANIC_BARRIER && distance < PANIC_BARRIER)
            {
                m_Timer_SwapDirection.ElapseTimer();
            }

            m_Goal.Scale(1 / distance);
            m_Goal.Scale(distance + deltaScale);
            m_Goal.RotateY(m_ArbitraryRotation);
            m_Goal.Add(target.GetPosition());

            m_PreviousDistance = distance;

            if(m_Timer_SwapDirection.HasElapsed())
            {
                double val = MathsHelper.Normalise(distance, MINIMUM_RANGE, IDEAL_RANGE);
                double max = ((SWAP_DIRECTION_TIME_UPPER_MAX - SWAP_DIRECTION_TIME_LOWER_MAX) * val) + SWAP_DIRECTION_TIME_LOWER_MAX;
                double min = ((SWAP_DIRACTION_TIME_UPPER_MIN - SWAP_DIRACTION_TIME_LOWER_MIN) * val) + SWAP_DIRACTION_TIME_LOWER_MIN;

                double newTimerDuration = MathsHelper.RandomDouble(min, max);
                m_Timer_SwapDirection.SetDuration(newTimerDuration);
                m_Timer_SwapDirection.Start();

                m_ArbitraryRotation *= -1;
            }
        }

        return m_Goal;
    }
}
