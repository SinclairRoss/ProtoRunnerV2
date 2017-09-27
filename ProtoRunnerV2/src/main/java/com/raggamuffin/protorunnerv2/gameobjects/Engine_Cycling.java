package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;

import java.util.ArrayList;

public class Engine_Cycling extends Engine
{
    private GameLogic m_Game;
    private ArrayList<CyclingEngineAttachment> m_EngineAttachments;

    public Engine_Cycling(Vehicle anchor, GameLogic game)
    {
        super(game, anchor);

        m_Game = game;

        int numEngines = 3;
        double theta = (Math.PI * 2) / numEngines;

        m_EngineAttachments = new ArrayList<>(numEngines);

        for(int i = 0; i < numEngines; ++i)
        {
            CyclingEngineAttachment attachment = new CyclingEngineAttachment(anchor, game, 2, theta * i);
            m_EngineAttachments.add(attachment);

            m_Game.AddMiscObject(attachment);
        }
    }

    @Override
    public void Update(double deltaTime)
    {
        int numEngines = m_EngineAttachments.size();
        for(int i = 0; i < numEngines; ++i)
        {
            CyclingEngineAttachment attachment = m_EngineAttachments.get(i);
            attachment.Update(deltaTime);
        }

        super.Update(deltaTime);
    }

    @Override
    public void CleanUp()
    {
        int numEngines = m_EngineAttachments.size();
        for(int i = 0; i < numEngines; ++i)
        {
            CyclingEngineAttachment attachment = m_EngineAttachments.get(i);
            m_Game.RemoveMiscObject(attachment);
        }

        m_EngineAttachments.clear();
    }
}
