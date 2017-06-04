package com.raggamuffin.protorunnerv2.master;

// Author: Sinclair Ross
// Date:   30/05/2017

import com.raggamuffin.protorunnerv2.utils.UIHelpers;
import com.raggamuffin.protorunnerv2.utils.Vector2;

public class TouchPointer
{
    private int m_ID;
    private Vector2 m_InitialPosition;
    private Vector2 m_CurrentPosition;

    public TouchPointer()
    {
        m_ID = -1;
        m_InitialPosition = new Vector2();
        m_CurrentPosition = new Vector2();
    }

    public void Initialise(int id, float x, float y)
    {
        m_ID = id;

        UIHelpers.TouchToScreenCoords(x, y, m_InitialPosition);
        m_CurrentPosition.SetVector(m_InitialPosition);
    }

    public void CleanUp()
    {
        m_ID = -1;
    }

    public void UpdatePosition(float x, float y)
    {
        UIHelpers.TouchToScreenCoords(x, y, m_CurrentPosition);
    }

    public int GetId() { return m_ID; }
    public Vector2 GetInitialPosition() { return m_InitialPosition; }
    public Vector2 GetCurrentPosition() { return m_CurrentPosition; }

    public boolean IsActive() { return m_ID >= 0; }
}
