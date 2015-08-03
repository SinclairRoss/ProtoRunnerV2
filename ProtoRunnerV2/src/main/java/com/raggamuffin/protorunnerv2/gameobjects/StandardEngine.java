package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.particles.TrailEmitter;
import com.raggamuffin.protorunnerv2.utils.Colour;

public class StandardEngine extends Engine
{
    private TrailEmitter m_TrailEmitter;

    public StandardEngine(GameObject anchor, ParticleManager pManager, EngineUseBehaviour behaviour)
    {
        super(anchor, behaviour);

        m_TrailEmitter = new TrailEmitter(m_Anchor, pManager);
        m_Anchor.AddChild(m_TrailEmitter);
    }

    @Override
    public void UpdateParticleColours(Colour start, Colour end)
    {
        m_TrailEmitter.SetStartColour(start);
        m_TrailEmitter.SetFinalColour(end);
    }
}
