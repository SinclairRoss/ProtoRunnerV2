package com.raggamuffin.protorunnerv2.ai;


import android.util.Log;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.Timer;

public class EvasionControl
{
    private Sensor_IncomingDanger m_DangerSensor;
    private Vehicle m_Anchor;
    private Timer m_DodgeCooldownTimer;

    public EvasionControl(AIController controller)
    {
        m_DangerSensor = controller.GetSituationalAwareness().GetDangerSensor();
        m_Anchor = controller.GetAnchor();
        m_DodgeCooldownTimer = new Timer(3.0);
    }

    public void Update(double deltaTime)
    {
        m_DodgeCooldownTimer.Update(deltaTime);

        switch (m_DangerSensor.GetDangerState())
        {
            case NoDanger:
                break;
            case DangerLeft:
            {
                if (m_DodgeCooldownTimer.TimedOut())
                {
                    m_Anchor.DodgeRight();
                    m_DodgeCooldownTimer.ResetTimer();

                }

                break;
            }
            case DangerRight:
            {
                if (m_DodgeCooldownTimer.TimedOut())
                {
                    m_Anchor.DodgeLeft();
                    m_DodgeCooldownTimer.ResetTimer();
                }

                break;
            }
        }

    }
}
