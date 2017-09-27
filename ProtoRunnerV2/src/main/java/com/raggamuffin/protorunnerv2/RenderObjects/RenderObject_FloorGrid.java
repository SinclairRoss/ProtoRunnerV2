package com.raggamuffin.protorunnerv2.RenderObjects;

// Author: Sinclair Ross
// Date:   15/08/2017

import com.raggamuffin.protorunnerv2.gameobjects.FloorGrid;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class RenderObject_FloorGrid extends RenderObject
{
    private double m_Attenuation;

    public void SetupForFloorGrid(FloorGrid grid)
    {
        SetupForObject(grid.GetPosition(), Vector3.FORWARD, Vector3.RIGHT, Vector3.UP, Vector3.ONE, grid.GetColour());
        m_Attenuation = grid.GetAttenuation();
    }

    public double GetAttenuation() { return m_Attenuation; }
}
