package com.raggamuffin.protorunnerv2.RenderObjects;

// Author: Sinclair Ross
// Date:   29/07/2017

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public class RenderObject_Vehicle extends RenderObject
{
    private double m_InnerIntensity;

    public void SetupForVehicle(Vehicle vehicle)
    {
        SetupForObject(vehicle.GetPosition(), vehicle.GetForward(), vehicle.GetRight(), vehicle.GetUp(), vehicle.GetScale(), vehicle.GetColour());
        m_InnerIntensity = vehicle.GetInnerColourIntensity();
    }

    public double GetInnerIntensity() { return m_InnerIntensity; }
}
