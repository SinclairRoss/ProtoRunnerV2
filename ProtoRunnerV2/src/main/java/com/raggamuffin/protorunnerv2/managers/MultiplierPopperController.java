package com.raggamuffin.protorunnerv2.managers;

// Author: Sinclair Ross
// Date:   17/01/2017

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_MultiplierPopper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.ArrayList;

public class MultiplierPopperController
{
    private GameLogic m_Game;
    private ArrayList<ParticleEmitter_MultiplierPopper> m_Poppers;

    private Boolean m_On;

    public MultiplierPopperController(GameLogic game)
    {
        m_Game = game;

        m_Poppers = new ArrayList<>();

        m_On = true;
    }

    public void Update()
    {
        for(ParticleEmitter_MultiplierPopper popper : m_Poppers)
        {
            if(popper.IsActive())
            {
                popper.Update();
            }
        }
    }

    public void Pop(Vector3 position, Vector3 velocity)
    {
        if(m_On)
        {
            ParticleEmitter_MultiplierPopper activePopper = null;

            for (ParticleEmitter_MultiplierPopper popper : m_Poppers)
            {
                if (!popper.IsActive())
                {
                    activePopper = popper;
                    break;
                }
            }

            if (activePopper == null)
            {
                activePopper = new ParticleEmitter_MultiplierPopper(m_Game, 10);
                m_Poppers.add(activePopper);
            }

            activePopper.Start(position, velocity);
        }
    }

    public void On()
    {
        m_On = true;
    }

    public void Off()
    {
        m_On = false;
    }
}