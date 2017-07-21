package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   12/06/2017

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.utils.Action;

public class UIAnimation_Pulse extends UIAnimation
{
    private final double PULSE_RATE_GROW = 6.0;
    private final double PULSE_RATE_SHRINK = 2.0;
    private final double PULSE_MAGNITUDE = 0.015;
    private final double BASE_SCALE = 0.2;

    private double m_PulseCounter;
    private Action m_OnGrowthAction;
    private boolean m_GrowthActionPerformed;

    public UIAnimation_Pulse(UIElement anchor, Action onGrowthAction)
    {
        super(anchor);

        m_PulseCounter = 0;

        m_OnGrowthAction = onGrowthAction;
        m_GrowthActionPerformed = false;
    }

    @Override
    public void OnUpdate(double deltaTime)
    {
        double pulseRate = ((m_PulseCounter < Math.PI / 2) || (m_PulseCounter > Math.PI * 1.5)) ? PULSE_RATE_GROW : PULSE_RATE_SHRINK;
        m_PulseCounter += deltaTime * pulseRate;
        m_PulseCounter %= Math.PI * 2;

        double pulseAmount = Math.sin(m_PulseCounter);
        GetAnchor().SetScale(BASE_SCALE + (pulseAmount * PULSE_MAGNITUDE));

        if (m_PulseCounter > Math.PI * 1.75)
        {
            if(!m_GrowthActionPerformed)
            {
                m_OnGrowthAction.Fire();
                m_GrowthActionPerformed = true;
            }
        }
        else
        {
            m_GrowthActionPerformed = false;
        }
    }
}
