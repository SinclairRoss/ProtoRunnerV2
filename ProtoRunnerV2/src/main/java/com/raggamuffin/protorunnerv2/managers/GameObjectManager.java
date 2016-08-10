package com.raggamuffin.protorunnerv2.managers;

// Author: Sinclair Ross
// Date:   25/06/2016

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;

import java.util.ArrayList;

public class GameObjectManager
{
    private ArrayList<GameObject> m_GameObjects;

    public GameObjectManager(GameLogic game)
    {
        m_GameObjects = new ArrayList<>();
    }

    public void Update(double deltaTime)
    {
        for(int i = 0; i < m_GameObjects.size(); i++)
        {
            GameObject obj = m_GameObjects.get(i);

            if (obj.IsValid())
            {
                obj.Update(deltaTime);
            }
            else
            {
                obj.CleanUp();
                m_GameObjects.remove(obj);
                i--;
            }
        }
    }

    public void AddObject(GameObject obj)
    {
        m_GameObjects.add(obj);
    }

    public void RemoveObject(GameObject obj)
    {
        m_GameObjects.remove(obj);
    }
}
