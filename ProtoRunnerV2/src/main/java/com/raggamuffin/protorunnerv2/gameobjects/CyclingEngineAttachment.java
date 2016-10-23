package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.TrailEmitter;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class CyclingEngineAttachment extends GameObject
{
    GameObject m_Anchor;
    private double m_OrbitRange;
    private double m_Offset;

    private double m_OrbitCounter;
    private double m_OrbitRate;
    private TrailEmitter m_TrailEmitter;

    private Vector3 m_TempVector;

    public CyclingEngineAttachment(GameObject anchor, GameLogic game, double orbitRange, double offset)
    {
        super(game, ModelType.EngineDrone);

        m_BaseColour = anchor.GetBaseColour();
        m_AltColour = anchor.GetAltColour();
        m_Colour = anchor.GetColour();

        m_Anchor = anchor;
        m_Forward = m_Anchor.GetForward();
        m_OrbitRange  = orbitRange;
        m_Offset = offset;

        m_OrbitCounter = 0.0;
        m_OrbitRate = Math.toRadians(270.0);

        m_TrailEmitter = new TrailEmitter(game, this);
        AddObjectToGameObjectManager(m_TrailEmitter);

        m_TempVector = new Vector3();
    }

    @Override
    public void Update(double deltaTime)
    {
        m_Position.SetVector(m_Anchor.GetPosition());

        m_OrbitCounter += (deltaTime * m_OrbitRate);

        m_OrbitCounter %= Math.PI * 2;

        m_TempVector.SetVector(m_OrbitRange, 0.0, 0.0);
        m_TempVector.RotateZ(m_OrbitCounter + m_Offset);
        m_TempVector.RotateY(m_Anchor.GetYaw());
        m_Position.Add(m_TempVector);

        m_TempVector.SetVector(m_Anchor.GetForward());
        m_TempVector.Scale(-5.0);
        m_Position.Add(m_TempVector);

        m_TrailEmitter.Update(deltaTime);

        UpdateVectorsWithForward(m_Anchor.GetForward());

        m_TrailEmitter.SetPosition(m_Position);
        m_TrailEmitter.Update(deltaTime);
    }

    @Override
    public double CalculateStress()
    {
        return m_Anchor.CalculateStress();
    }

    @Override
    public boolean IsValid()
    {
        return m_Anchor.IsValid();
    }

    @Override
    public void CleanUp()
    {}
}
