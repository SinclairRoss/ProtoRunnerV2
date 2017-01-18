package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   12/01/2017

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.utils.Timer;

public class Shield_Timed extends Shield
{
    private Timer m_DurationTimer;

    public Shield_Timed(GameLogic game, Vehicle anchor)
    {
        super(game, anchor);

        m_DurationTimer = new Timer(3);

        AttachToObject(anchor);
    }

    @Override
    public void AttachToObject(Vehicle object)
    {
        super.AttachToObject(object);
        m_DurationTimer.Start();
    }

    @Override
    public void Update(double deltaTime)
    {
        super.Update(deltaTime);

        if(m_DurationTimer.HasElapsed())
        {
            DetachFromObject();
        }
    }

    @Override
    public boolean IsValid()
    {
        boolean isValid = m_NormalisedScale > 0 || !m_DurationTimer.HasElapsed();
        return isValid;
    }
}