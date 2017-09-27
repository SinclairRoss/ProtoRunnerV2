package com.raggamuffin.protorunnerv2.managers;

// Author: Sinclair Ross
// Date:   25/06/2016

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Tentacle;

import java.util.ArrayList;

public class RopeManager
{
    private ArrayList<Tentacle> m_GameObjects;
    private GameLogic m_Game;

    public RopeManager(GameLogic game)
    {
        m_GameObjects = new ArrayList<>();
        m_Game = game;
    }

    public void Update(double deltaTime)
    {
        int numTentacles = m_GameObjects.size();
        for(int i = 0; i < numTentacles; i++)
        {
            Tentacle obj = m_GameObjects.get(i);

            if (obj.IsValid())
            {
                obj.Update(deltaTime);
            }
            else
            {
                obj.CleanUp();
                RemoveObject(obj);

                --numTentacles;
                i--;
            }
        }
    }

    public void AddObject(Tentacle obj)
    {
        m_GameObjects.add(obj);
    }

    public void RemoveObject(Tentacle obj)
    {
        m_GameObjects.remove(obj);
    }
}
