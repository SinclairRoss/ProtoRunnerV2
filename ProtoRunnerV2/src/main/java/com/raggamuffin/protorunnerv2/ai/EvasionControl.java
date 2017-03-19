package com.raggamuffin.protorunnerv2.ai;

import android.util.Log;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Timer_Accumulation;

public class EvasionControl
{
    private Sensor_IncomingDanger m_DangerSensor;
    private Vehicle m_Anchor;
    private Timer m_DodgeCooldownTimer;

    private Boolean m_Enabled;

    public EvasionControl(AIController controller)
    {
        m_DangerSensor = controller.GetSituationalAwareness().GetDangerSensor();
        m_Anchor = controller.GetAnchor();
        m_DodgeCooldownTimer = new Timer(3.0);
        m_DodgeCooldownTimer.ElapseTimer();

        m_Enabled = true;
    }

    public void Update()
    {
        if(m_Enabled)
        {
            switch (m_DangerSensor.GetDangerState())
            {
                case NoDanger:
                {
                    break;
                }
                case DangerLeft:
                {
                    if (m_DodgeCooldownTimer.HasElapsed())
                    {
                        m_Anchor.DodgeRight();
                        m_DodgeCooldownTimer.Start();
                    }

                    break;
                }
                case DangerRight:
                {
                    if (m_DodgeCooldownTimer.HasElapsed())
                    {
                        m_Anchor.DodgeLeft();
                        m_DodgeCooldownTimer.Start();
                    }

                    break;
                }
            }
        }
    }

    public void Disable() { m_Enabled = false; }
    public void Enable() { m_Enabled = true; }
}
