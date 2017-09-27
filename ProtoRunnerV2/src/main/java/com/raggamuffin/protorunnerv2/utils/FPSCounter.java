package com.raggamuffin.protorunnerv2.utils;

// Author: Sinclair Ross
// Date:   23/08/2017

import android.util.Log;

public class FPSCounter
{
    private final String m_Name;
    private long m_StartTime;
    private int m_NumFrames;

    public FPSCounter(String name)
    {
        m_Name = name;
        m_StartTime = System.currentTimeMillis();
    }

    public void Bump()
    {
        ++m_NumFrames;
    }

    public void Update()
    {
        long currentTime =  System.currentTimeMillis();
        long delta = currentTime - m_StartTime;

        if(delta >= 1000L)
        {
            Log.e("FPS" + m_Name, "Frames: " + m_NumFrames);

            m_NumFrames = 0;
            m_StartTime = currentTime;
        }
    }
}
