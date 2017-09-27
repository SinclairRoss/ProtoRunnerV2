package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   16/01/2017

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class FloorGrid
{
    private final double DEPTH = -1.0;

    private final Vector3 m_AnchorPosition;
    private final Colour m_AnchorColour;

    private Vector3 m_Position;
    private double m_Attenuation;

    private Timer m_FadeAwayTimer;

    public FloorGrid(Vector3 anchorPosition, Colour anchorColour, double attenuation)
    {
        super();

        m_AnchorPosition = anchorPosition;
        m_AnchorColour = anchorColour;

        m_Position = new Vector3();
        UpdatePosition();

        m_Attenuation = attenuation;

        m_FadeAwayTimer = new Timer(5);
    }

    private void UpdatePosition()
    {
        m_Position.SetVector(m_AnchorPosition.X, DEPTH, m_AnchorPosition.Z);
    }

    public Vector3 GetPosition()
    {
        UpdatePosition();
        return m_Position;
    }

    public void NotifyOfAnchorInvalidation()
    {
        m_FadeAwayTimer.StartElapsed();
    }

    public boolean IsValid()
    {
        return !m_FadeAwayTimer.HasElapsed();
    }

    public Colour GetColour()
    {
        return m_AnchorColour;
    }

    public double GetAttenuation() { return m_Attenuation; }
    public void SetAttenuation(double attenuation) { m_Attenuation = attenuation; }
}
