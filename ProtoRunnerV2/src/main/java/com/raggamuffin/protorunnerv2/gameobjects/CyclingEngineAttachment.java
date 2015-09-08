package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.particles.TrailEmitterDepricated;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class CyclingEngineAttachment extends GameObject
{
    GameObject m_Anchor;
    private double m_OrbitRange;
    private double m_Offset;

    private double m_OrbitCounter;
    private double m_OrbitRate;
    private TrailEmitterDepricated m_TrailEmitterDepricated;

    private Vector3 m_TempVector;

    public CyclingEngineAttachment(GameObject anchor, ParticleManager pManager, double orbitRange, double offset)
    {
        super(null, null);

        m_Model = ModelType.EngineDrone;

        m_BaseColour = anchor.GetBaseColour();
        m_Anchor = anchor;
        m_OrbitRange  = orbitRange;
        m_Offset = offset;

        m_OrbitCounter = 0.0;
        m_OrbitRate = Math.toRadians(270.0);

        m_TrailEmitterDepricated = new TrailEmitterDepricated(this, pManager);
        AddChild(m_TrailEmitterDepricated);

        m_TempVector = new Vector3();
    }

    @Override
    public void Update(double deltaTime)
    {
        m_OrbitCounter += (deltaTime * m_OrbitRate);

        if(m_OrbitCounter > Math.PI * 2)
            m_OrbitCounter -= Math.PI * 2;

        m_TempVector.SetVector(m_OrbitRange, 0.0, 0.0);
        m_TempVector.RotateZ(m_OrbitCounter + m_Offset);
        m_TempVector.RotateY(m_Anchor.GetYaw());
        m_Position.Add(m_TempVector);

        m_TempVector.SetVector(m_Anchor.GetForward());
        m_TempVector.Scale(-5.0);
        m_Position.Add(m_TempVector);

        m_Anchor.GetPosition();

        m_TrailEmitterDepricated.Update(deltaTime);

        m_Yaw = m_Anchor.GetYaw();

        super.Update(deltaTime);
    }

    @Override
    public boolean IsValid()
    {
        return false;
    }

    public void SetTrailColour(Colour start, Colour end)
    {
        m_TrailEmitterDepricated.SetStartColour(start);
        m_TrailEmitterDepricated.SetFinalColour(end);
    }
}
