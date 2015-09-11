package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.utils.Colour;

import java.util.ArrayList;

public class Engine_Cycling extends Engine
{
    private ArrayList<CyclingEngineAttachment> m_Attachments;

    public Engine_Cycling(GameObject anchor, GameLogic game, EngineUseBehaviour behaviour)
    {
        super(anchor, behaviour);

        m_Attachments = new ArrayList<>();

        int numEngines = 3;
        double theta = (Math.PI * 2) / numEngines;

        for(int i = 0; i < numEngines; i++)
        {
            CyclingEngineAttachment attachment = new CyclingEngineAttachment(anchor, game, 2, theta * i);
            m_Attachments.add(attachment);
            m_Anchor.AddChild(attachment);
        }
    }

    @Override
    public void UpdateParticleColours(Colour start, Colour end)
    {
        for(CyclingEngineAttachment attachment : m_Attachments)
            attachment.SetTrailColour(start, end);
    }
}
