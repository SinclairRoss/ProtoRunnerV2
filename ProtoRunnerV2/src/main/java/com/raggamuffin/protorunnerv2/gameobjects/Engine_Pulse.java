package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   25/06/2016

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class Engine_Pulse extends Engine
{
    private double m_Output;
    private double m_PulseFrequency;

    public Engine_Pulse(GameLogic game, GameObject anchor)
    {
        super(game, anchor);

        m_Output = 0.0;
        m_PulseFrequency = 1.75;
    }

    @Override
    public void Update(double deltaTime)
    {
      //  double pulseModifier = m_PulseFrequency;

      //  if(m_Output > Math.PI)
       // {
       //     pulseModifier *= 0.4;
       // }

       // m_Output += pulseModifier * deltaTime;
        //m_Output %= Math.PI * 2;

        //SetEngineOutput(Math.sin(m_Output));
        SetEngineOutput(1.0);

        super.Update(deltaTime);
    }
}
