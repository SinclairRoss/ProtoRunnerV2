package com.raggamuffin.protorunnerv2.utils;

public class CollisionReport
{
    private double m_RayEntry;
    private double m_RayExit;

    public CollisionReport(double rayEntry, double rayExit)
    {
        m_RayEntry = rayEntry;
        m_RayExit = rayExit;
    }

    public double GetRayEntry() { return m_RayEntry; }
    public double GetRayExit() { return m_RayExit; }
}
