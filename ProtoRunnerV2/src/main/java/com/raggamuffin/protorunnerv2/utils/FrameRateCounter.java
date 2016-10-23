package com.raggamuffin.protorunnerv2.utils;

// Author: Sinclair Ross
// Date:   26/09/2016

import android.util.Log;

public final class FrameRateCounter
{
    private Long m_FrameStart;
    private Long m_FrameEnd;

    public FrameRateCounter()
    {
        m_FrameStart = 0L;
        m_FrameEnd = 0L;
    }

    public void StartFrame()
    {
        m_FrameStart = System.currentTimeMillis();
    }

    public void EndFrame()
    {
        m_FrameEnd = System.currentTimeMillis();
    }

    public void LogFrameDuration(String tag)
    {
        LogFrameDuration(tag, 0L);
    }

    public void LogFrameDuration(String tag, Long threshold)
    {
        Long frameDuration = m_FrameEnd - m_FrameStart;

        if(frameDuration >= threshold)
        {
            Log.e(tag, "Frame duration: " + frameDuration + "ms");
        }
    }
}
