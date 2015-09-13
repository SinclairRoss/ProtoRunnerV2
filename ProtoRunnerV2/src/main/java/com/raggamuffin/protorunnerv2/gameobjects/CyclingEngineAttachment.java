package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.TrailEmitter;
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
    private TrailEmitter m_TrailEmitter;

    private Vector3 m_TempVector;

    public CyclingEngineAttachment(GameObject anchor, GameLogic game, double orbitRange, double offset)
    {
        super(null, null);

        m_Model = ModelType.EngineDrone;

        m_BaseColour = anchor.GetBaseColour();
        m_AltColour = anchor.GetAltColour();
        m_Colour = anchor.GetColour();

        m_Anchor = anchor;
        m_OrbitRange  = orbitRange;
        m_Offset = offset;

        m_OrbitCounter = 0.0;
        m_OrbitRate = Math.toRadians(270.0);

        m_TrailEmitter = new TrailEmitter(this, game);
        AddChild(m_TrailEmitter);

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

        m_TrailEmitter.Update(deltaTime);

        m_Yaw = m_Anchor.GetYaw();

        m_TrailEmitter.SetPosition(m_Position);
        m_TrailEmitter.Update(deltaTime);


      //  super.Update(deltaTime);
    }

    @Override
    public boolean IsValid()
    {
        return false;
    }

    public void SetTrailColour(Colour start, Colour end)
    {
      //  m_TrailEmitter.SetStartColour(start);
     //   m_TrailEmitter.SetFinalColour(end);
    }
}
