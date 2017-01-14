package com.raggamuffin.protorunnerv2.utils;

// Author: Sinclair Ross
// Date:   12/01/2017

public class Timer
{
    private double m_Duration;
    private double m_EndTime;
    private double m_StartTime;

    public Timer(double durationSeconds)
    {
        m_Duration = durationSeconds * 1000;
        m_EndTime = 0;
    }

    public void Start()
    {
        m_StartTime = GetTime();
        m_EndTime = m_StartTime + m_Duration;
    }

    public void ElapseTimer()
    {
        m_EndTime = 0;
    }

    public Boolean HasTimerElapsed()
    {
        return GetTime() >= m_EndTime;
    }

    public double GetProgress()
    {
        return MathsHelper.Normalise(GetTime(), m_StartTime, m_EndTime);
    }

    public double GetInverseProgress()
    {
        return 1.0 - GetProgress();
    }

    private double GetTime()
    {
        return System.currentTimeMillis();
    }
}