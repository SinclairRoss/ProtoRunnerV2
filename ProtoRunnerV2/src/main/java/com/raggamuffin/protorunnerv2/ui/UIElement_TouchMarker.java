package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   02/06/2017

import com.raggamuffin.protorunnerv2.master.TouchPointer;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Timer;

public class UIElement_TouchMarker extends UIElement_Triangle
{
    private static final double FADE_DURATION = 0.25;
    private static final double ROTATION_SPEED = 2.0;

    private enum State
    {
        Normal,
        FadeAway,
        Off
    }

    private State m_State;

    private TouchPointer m_TouchPointer;

    private Timer m_FadeTimer;

    public UIElement_TouchMarker()
    {
        super(30.0);

        SetScale(0.2);
        SetColour(Colours.RunnerBlue);

        m_FadeTimer = new Timer(FADE_DURATION);
    }

    public void Initialise(TouchPointer pointer)
    {
        m_TouchPointer = pointer;
        SetPosition(m_TouchPointer.GetCurrentPosition());
        SetColour(Colours.RunnerBlue);
        SetAlpha(0);

        m_State = State.Normal;

        m_FadeTimer.Start();
    }

    public void Update(double deltaTime)
    {
        switch (m_State)
        {
            case Normal:
            {
                SetPosition(m_TouchPointer.GetCurrentPosition());
                SetAlpha(m_FadeTimer.GetProgress());

                if (!m_TouchPointer.IsActive())
                {
                    m_FadeTimer.Start();
                    m_State = State.FadeAway;
                }

                break;
            }
            case FadeAway:
            {
                SetAlpha(m_FadeTimer.GetInverseProgress());
                break;
            }
        }

       // Rotate(deltaTime * ROTATION_SPEED);
    }

    public void CleanUp()
    {
        m_TouchPointer = null;
    }

    public int GetTouchPointerID() { return m_TouchPointer.GetId(); }
    public boolean IsActive() { return m_State != State.FadeAway || !m_FadeTimer.HasElapsed(); }
}