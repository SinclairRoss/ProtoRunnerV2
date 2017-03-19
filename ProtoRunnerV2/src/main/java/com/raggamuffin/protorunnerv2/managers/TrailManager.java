package com.raggamuffin.protorunnerv2.managers;

// Author: Sinclair Ross
// Date:   15/03/2017

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.Trail;

import java.util.ArrayList;

public class TrailManager
{
    private ArrayList<Trail> m_Trails;
    private GameLogic m_Game;

    public TrailManager(GameLogic game)
    {
        m_Trails = new ArrayList<>();
        m_Game = game;
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
                m_Game.RemoveObjectFromRenderer(trail);

                --numTrails;
                i--;
            }
        }
    }

    public void AddObject(Trail trail)
    {
        m_Trails.add(trail);
        m_Game.AddObjectToRenderer(trail);
    }
}
