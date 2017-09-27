package com.raggamuffin.protorunnerv2.utils;

// Author: Sinclair Ross
// Date:   12/01/2017

import com.raggamuffin.protorunnerv2.master.TimeKeeper;

public class Timer
{
    private double m_Duration;
    private double m_EndTime;
    private double m_StartTime;

    private boolean m_Active;

    public Timer(double durationSeconds)
    {
        SetDuration(durationSeconds);
        m_EndTime = 0;

        m_Active = false;
    }

    public void Start(double durationSeconds)
    {
        SetDuration(durationSeconds);
        Start();
    }

    public void Start()
    {
        m_StartTime = TimeKeeper.Instance().CurrentTime();
        m_EndTime = m_StartTime + m_Duration;

        m_Active = true;
    }

    public void StartElapsed()
    {
        Start();
        ElapseTimer();
    }

    public void Stop()
    {
        m_Active = false;
    }

    public void SetDuration(double durationSeconds)
    {
        m_Duration = durationSeconds;
    }

    public void ElapseTimer()
    {
        m_EndTime = 0;
        m_Active = true;
    }

    public boolean HasElapsed()
    {
        boolean hasElapsed = m_Active && (TimeKeeper.Instance().CurrentTime() >= m_EndTime);
        return hasElapsed;
    }

    public boolean IsActive() { return m_Active; }

    public double GetProgress()
    {
        double progress = m_Active ? MathsHelper.Normalise(TimeKeeper.Instance().CurrentTime(), m_StartTime, m_EndTime) : 0.0;
        return progress;
    }

    public double GetInverseProgress()
    {
        double progress = 1.0 - GetProgress();
        return progress;
    }
}