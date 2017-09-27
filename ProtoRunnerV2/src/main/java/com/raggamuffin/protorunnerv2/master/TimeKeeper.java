package com.raggamuffin.protorunnerv2.master;

// Author: Sinclair Ross
// Date:   20/08/2017

public class TimeKeeper
{
    private static TimeKeeper m_Instance;
    public static TimeKeeper Instance()
    {
        if(m_Instance == null) { m_Instance = new TimeKeeper(); }
        return m_Instance;
    }

    private double m_CurrentTime;
    private double m_TimeDilation;

    private TimeKeeper()
    {
        m_CurrentTime = 0.0;
        m_TimeDilation = 1.0;
    }

    public void Update(double deltaTime)
    {
        m_CurrentTime += deltaTime * m_TimeDilation;
    }

    public double CurrentTime() { return m_CurrentTime; }
    public void SetTimeDilation(double dilation) { m_TimeDilation = dilation; }
}
