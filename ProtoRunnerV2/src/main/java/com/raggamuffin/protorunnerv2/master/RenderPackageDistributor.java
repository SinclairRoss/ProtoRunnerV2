package com.raggamuffin.protorunnerv2.master;

// Author: Sinclair Ross
// Date:   05/08/2017

import android.util.Log;

import com.raggamuffin.protorunnerv2.RenderObjects.RenderPackage;

import java.util.ArrayList;

public class RenderPackageDistributor
{
    private final int NUM_PACKAGES = 2;

    private final Object m_ReadMutex;
    private final Object m_WriteMutex;

    private ArrayList<RenderPackage> m_ReadReady;
    private ArrayList<RenderPackage> m_WriteReady;

    private static String tag = "Distributer";

    public RenderPackageDistributor()
    {
        m_ReadMutex = new Object();
        m_WriteMutex = new Object();

        m_WriteReady = new ArrayList<>(NUM_PACKAGES);
        for(int i = 0; i < NUM_PACKAGES; ++i)
        {
            m_WriteReady.add(new RenderPackage());
        }

        m_ReadReady = new ArrayList<>(NUM_PACKAGES);
    }

    private void Output()
    {
        Log.e(tag, "Read Ready: " + m_ReadReady.size());
        Log.e(tag, "Write Ready: " + m_WriteReady.size());
    }

    public RenderPackage Checkout_ForWrite()
    {
        synchronized (m_WriteMutex)
        {
            RenderPackage rPackage = null;

            if(!m_WriteReady.isEmpty())
            {
                rPackage = m_WriteReady.remove(m_WriteReady.size() - 1);
                rPackage.Clear();
            }

            return rPackage;
        }
    }

    public void CheckIn_FromWrite(RenderPackage rPackage)
    {
        synchronized (m_ReadMutex)
        {
            m_ReadReady.add(rPackage);
        }
    }

    public RenderPackage Checkout_ForRead()
    {
        synchronized (m_ReadMutex)
        {
            RenderPackage rPackage = null;

            if(!m_ReadReady.isEmpty())
            {
                rPackage = m_ReadReady.remove(0);
            }

            return rPackage;
        }
    }

    public void CheckIn_FromRead(RenderPackage rPackage)
    {
        synchronized (m_WriteMutex)
        {
            m_WriteReady.add(rPackage);
        }
    }
}