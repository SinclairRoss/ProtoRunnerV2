package com.raggamuffin.protorunnerv2.managers;

// Author: Sinclair Ross
// Date:   23/09/2016

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;

import java.util.ArrayList;

public class GameObjectManager
{
    private ArrayList<GameObject> m_GameObjects;
    private GameLogic m_GameLogic;

    public GameObjectManager(GameLogic game)
    {
        m_GameObjects = new ArrayList<>();
        m_GameLogic = game;
    }

    public void Update(double deltaTime)
    {
        for(int i = 0; i < m_GameObjects.size(); ++i)
        {
            GameObject object = m_GameObjects.get(i);

            if(object.IsValid())
            {
                object.Update(deltaTime);
            }
            else
            {
                object.CleanUp();
                m_GameObjects.remove(object);
                m_GameLogic.RemoveObjectFromRenderer(object);
                --i;
            }
        }
    }

    public void AddObject(GameObject object)
    {
        m_GameObjects.add(object);
        m_GameLogic.AddObjectToRenderer(object);
    }

    public void RemoveObject(GameObject object)
    {
        m_GameObjects.remove(object);
        m_GameLogic.RemoveObjectFromRenderer(object);
    }

    public void Wipe()
    {
        for(int i = 0; i < m_GameObjects.size(); ++i)
        {
            GameObject object = m_GameObjects.get(i);

            object.CleanUp();
            m_GameObjects.remove(object);
            m_GameLogic.RemoveObjectFromRenderer(object);
            --i;
        }
    }
}
