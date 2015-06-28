package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.utils.Timer;

public abstract class ProgressBarAnimation
{
    protected enum AnimationState
    {
        Initialising,
        Running,
        Closing,
        Idle
    }

    protected AnimationState m_State;

    protected UIProgressBar m_Bar;
    private Timer m_DelayTimer;

    public ProgressBarAnimation(UIProgressBar bar)
    {
        m_Bar = bar;
        m_State = AnimationState.Idle;
        m_DelayTimer = new Timer(0.0);
    }

    public void Update(double deltaTime)
    {
        switch(m_State)
        {
            case Initialising:
            {
                m_DelayTimer.Update(deltaTime);

                if (m_DelayTimer.TimedOut())
                    m_State = AnimationState.Running;

                break;
            }
            case Running:
            {
                Run(deltaTime);

                break;
            }
            case Closing:
            {
                Close();
                m_State = AnimationState.Idle;

                break;
            }
            case Idle:
            {
                break;
            }
        }
    }

    protected abstract void Initialise();
    protected abstract void Run(double deltaTime);
    protected abstract void Close();

    public void TriggerBehaviour()
    {
        TriggerBehaviour(0.0);
    }

    public void TriggerBehaviour(double delay)
    {
        Initialise();
        m_DelayTimer.SetLimit(delay);
        m_State = AnimationState.Initialising;
    }

    protected void AnimationComplete()
    {
        m_State = AnimationState.Closing;
    }
}
