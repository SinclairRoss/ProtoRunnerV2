package com.raggamuffin.protorunnerv2.utils;

// Author: Sinclair Ross
// Date:   17/01/2017

public class StopWatch
{
    private long m_StartTime;
    private long m_EndTime;

    public StopWatch()
    {
        m_StartTime = 0;
        m_EndTime = 0;
    }

    public void Start()
    {
        m_StartTime = System.currentTimeMillis();
        m_EndTime = m_StartTime;
    }

    public long Stop()
    {
        m_EndTime = System.currentTimeMillis();
        return m_EndTime - m_StartTime;
    }
}
