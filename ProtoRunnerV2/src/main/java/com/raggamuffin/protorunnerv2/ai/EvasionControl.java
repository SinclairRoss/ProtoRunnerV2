package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.Timer_Accumulation;

public class EvasionControl
{
    private Sensor_IncomingDanger m_DangerSensor;
    private Vehicle m_Anchor;
    private Timer_Accumulation m_DodgeCooldownTimer;

    private Boolean m_Enabled;

    public EvasionControl(AIController controller)
    {
        m_DangerSensor = controller.GetSituationalAwareness().GetDangerSensor();
        m_Anchor = controller.GetAnchor();
        m_DodgeCooldownTimer = new Timer_Accumulation(3.0);

        m_Enabled = true;
    }

    public void Update(double deltaTime)
    {
        m_DodgeCooldownTimer.Update(deltaTime);

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

    public void Disable()
    {
        m_Enabled = false;
    }

    public void Enable()
    {
        m_Enabled = true;
    }
}
