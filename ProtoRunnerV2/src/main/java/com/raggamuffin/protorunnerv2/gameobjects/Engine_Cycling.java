package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;

public class Engine_Cycling extends Engine
{
    public Engine_Cycling(GameObject anchor, GameLogic game)
    {
        super(game, anchor);

        int numEngines = 3;
        double theta = (Math.PI * 2) / numEngines;

        for(int i = 0; i < numEngines; ++i)
        {
            CyclingEngineAttachment attachment = new CyclingEngineAttachment(anchor, game, 2, theta * i);
            m_Anchor.AddObjectToGameObjectManager(attachment);
        }
    }
}
