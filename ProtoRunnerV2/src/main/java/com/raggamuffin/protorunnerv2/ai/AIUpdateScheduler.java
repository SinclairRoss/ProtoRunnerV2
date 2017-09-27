package com.raggamuffin.protorunnerv2.ai;


// Author: Sinclair Ross
// Date:   20/08/2017


import java.util.ArrayList;

public class AIUpdateScheduler
{
    private static AIUpdateScheduler m_Instance;
    public static AIUpdateScheduler Instance()
    {
        if(m_Instance == null) { m_Instance = new AIUpdateScheduler(); }
        return m_Instance;
    }

    private ArrayList<SituationalAwareness> m_AwarnessComponents;
    private int m_UpdateIndex;
    private boolean m_HasUpdated;

    private AIUpdateScheduler()
    {
        m_AwarnessComponents = new ArrayList<>();
        m_UpdateIndex = 0;
        m_HasUpdated = false;
    }

    public boolean CanUpdate(SituationalAwareness component)
    {
        boolean canUpdate = false;

        if(!m_HasUpdated)
        {
            if (component == m_AwarnessComponents.get(m_UpdateIndex))
            {
                canUpdate = true;
                m_HasUpdated = true;

                ++m_UpdateIndex;
                m_UpdateIndex %= m_AwarnessComponents.size();
            }
        }

        return canUpdate;
    }

    public void NotifyUpdateComplete()
    {
        m_HasUpdated = false;
    }

    public void RegisterListener(SituationalAwareness component)
    {
        m_AwarnessComponents.add(component);
    }

    public void DeregisterComponent(SituationalAwareness component)
    {
        m_AwarnessComponents.remove(component);

        if(m_UpdateIndex >= m_AwarnessComponents.size())
        {
            m_UpdateIndex = 0;
        }
    }
}
