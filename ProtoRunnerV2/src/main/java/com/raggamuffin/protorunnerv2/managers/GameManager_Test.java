package com.raggamuffin.protorunnerv2.managers;

// Author: Sinclair Ross
// Date:   24/09/2016

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.RenderTester;
import com.raggamuffin.protorunnerv2.renderer.ModelType;

public class GameManager_Test extends GameManager
{
    private final double SPACING = 2.0;
    private final int LENGTH = 15;

    public GameManager_Test(GameLogic game)
    {
        super(game);
    }

    @Override
    public void Update(double deltaTime)
    {

    }

    @Override
    public void Initialise()
    {
        for(int x = 0; x < LENGTH; ++x)
        {
            for(int y = 0; y < LENGTH; ++y)
            {
                GameObject obj = new RenderTester(m_Game, ModelType.Runner);
                obj.SetPosition(x * SPACING, 0, y * SPACING);
                m_Game.GetGameObjectManager().AddObject(obj);
            }
        }
    }

    @Override
    public void CleanUp()
    {
        m_Game.GetGameObjectManager().Update(2);
    }
}
