package com.raggamuffin.protorunnerv2.managers;

// Author: Sinclair Ross
// Date:   23/09/2016

import com.raggamuffin.protorunnerv2.gameobjects.FloorGrid;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;

import java.util.ArrayList;

public class GameObjectManager
{
    private ArrayList<FloorGrid> m_FloorGrids;
    private ArrayList<GameObject> m_GameObjects;

    public GameObjectManager()
    {
        m_FloorGrids = new ArrayList<>();
        m_GameObjects = new ArrayList<>();
    }

    public void Update(double deltaTime)
    {
        for(int i = 0; i < m_GameObjects.size();)
        {
            GameObject object = m_GameObjects.get(i);

            if(object.IsValid())
            {
                object.Update(deltaTime);
                ++i;
            }
            else
            {
                object.CleanUp();
                m_GameObjects.remove(i);
            }
        }

        for(int i = 0; i < m_FloorGrids.size(); )
        {
            FloorGrid floorGrid = m_FloorGrids.get(i);

            if(floorGrid.IsValid())
            {
                ++i;
            }
            else
            {
                m_FloorGrids.remove(i);
            }
        }
    }

    public void AddObject(GameObject object) { m_GameObjects.add(object); }
    public void AddFloorGrid(FloorGrid grid) { m_FloorGrids.add(grid); }

    public ArrayList<GameObject> GetGameObjects() { return m_GameObjects; }
    public ArrayList<FloorGrid> GetFloorGrids() { return m_FloorGrids; }

    public void Wipe()
    {
        for(int i = 0; i < m_GameObjects.size(); ++i)
        {
            GameObject object = m_GameObjects.get(i);

            object.CleanUp();
            m_GameObjects.remove(object);
            --i;
        }
    }
}
