package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.TrailEmitter;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class CyclingEngineAttachment extends GameObject
{
    private final double PositionOffset = -6.0;

    GameObject m_Anchor;
    private double m_OrbitRange;
    private double m_Offset;

    private double m_OrbitCounter;
    private double m_OrbitRate;
    private TrailEmitter m_TrailEmitter;

    public CyclingEngineAttachment(GameObject anchor, GameLogic game, double orbitRange, double offset)
    {
        super(ModelType.EngineDrone, 1.0);

        SetColourByRef(anchor.GetColour());

        m_Anchor = anchor;
        SetOrientationVectorsByRef(m_Anchor);
        m_OrbitRange = orbitRange;
        m_Offset = offset;

        m_OrbitCounter = 0.0;
        m_OrbitRate = Math.toRadians(270.0);

        m_TrailEmitter = new TrailEmitter(game, this);
    }

    @Override
    public void Update(double deltaTime)
    {
        m_OrbitCounter += deltaTime * m_OrbitRate;
        m_OrbitCounter %= Math.PI * 2;

        SetPosition(m_Anchor.GetRight());
        GetPosition().Scale(m_OrbitRange);
        GetPosition().RotateAboutAxis(GetForward(), m_OrbitCounter + m_Offset);

        Vector3 fwd = GetForward();
        Translate(fwd.X * PositionOffset, fwd.Y * PositionOffset, fwd.Z * PositionOffset);
        Translate(m_Anchor.GetPosition());

        m_TrailEmitter.Update();
    }

    @Override
    public double GetInnerColourIntensity() { return m_Anchor.GetInnerColourIntensity(); }

    @Override
    public boolean IsValid()
    {
        return m_Anchor.IsValid();
    }

    @Override
    public void CleanUp()
    {}
}
