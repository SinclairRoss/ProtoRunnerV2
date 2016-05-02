package com.raggamuffin.protorunnerv2.utils;

public class CollisionReport
{
    private boolean m_CollisionDetected;

    private Vector3 m_EntryPoint;
    private Vector3 m_ExitPoint;

    private double m_RayEntry;
    private double m_RayExit;

    public CollisionReport(boolean collisionDetected, double rayEntry, double rayExit, Vector3 entryPoint, Vector3 exitPoint)
    {
        m_CollisionDetected = collisionDetected;

        m_EntryPoint = entryPoint;
        m_ExitPoint = exitPoint;

        m_RayEntry = rayEntry;
        m_RayExit = rayExit;
    }

    public boolean CollisionDetected()
    {
        return m_CollisionDetected;
    }

    public Vector3 GetEntryPoint()
    {
        return m_EntryPoint;
    }

    public Vector3 GetExitPoint()
    {
        return m_ExitPoint;
    }

    public double GetRayEntry()
    {
        return m_RayEntry;
    }

    public double GetRayExit()
    {
        return m_RayExit;
    }
}
