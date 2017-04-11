package com.raggamuffin.protorunnerv2.utils;

// Author: Sinclair Ross
// Date:   12/01/2017

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

    public void Start()
    {
        m_StartTime = System.currentTimeMillis();
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
        m_Duration = durationSeconds * 1000;
    }

    public void ElapseTimer()
    {
        m_EndTime = 0;
        m_Active = true;
    }

    public Boolean HasElapsed()
    {
        return System.currentTimeMillis() >= m_EndTime && m_Active;
    }

    public double GetProgress()
    {
        return MathsHelper.Normalise(System.currentTimeMillis(), m_StartTime, m_EndTime);
    }

    public double GetInverseProgress()
    {
        return 1.0 - GetProgress();
    }

    public double GetRunTimeMillis()
    {
        double runtime = System.currentTimeMillis() - m_StartTime;
        return runtime;
    }
}