package com.raggamuffin.protorunnerv2.managers;

// Author: Sinclair Ross
// Date:   15/03/2017

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.Trail;

import java.util.ArrayList;

public class TrailManager
{
    private ArrayList<Trail> m_Trails;

    public TrailManager()
    {
        m_Trails = new ArrayList<>();
    }

    public void Update()
    {
        int numTrails = m_Trails.size();
        for(int i = 0; i < numTrails; i++)
        {
            Trail trail = m_Trails.get(i);
            trail.Update();

            if (!trail.IsValid())
            {
                m_Trails.remove(i);

                --numTrails;
                i--;
            }
        }
    }

    public void AddObject(Trail trail) { m_Trails.add(trail); }
    public ArrayList<Trail> GetTrails() { return m_Trails; }
}
