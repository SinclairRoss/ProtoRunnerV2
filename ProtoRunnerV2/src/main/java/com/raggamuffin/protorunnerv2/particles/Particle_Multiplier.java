package com.raggamuffin.protorunnerv2.particles;

// Author: Sinclair Ross
// Date:   15/01/2017

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.FloorGrid;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Spring3;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Particle_Multiplier extends Particle
{
    private final double RESTING_HEIGHT = 1.0;
    private final double BOBBBING_SPEED = 3.0;
    private final double BOBBING_HEIGHT = 1;

    private GameLogic m_Game;

    private Vector3 m_RestingPositon;
    private Spring3 m_Spring;
    private double m_BobbingCounter;

    private FloorGrid m_FloorGrid;

    public Particle_Multiplier(GameLogic game)
    {
        super(5, 0.0, 0.96);

        m_Game = game;

        m_RestingPositon = new Vector3();
        m_Spring = new Spring3(0.6, 1);

        m_Colour.SetColour(Colours.PastelGreen);

        m_FloorGrid = new FloorGrid(m_Position, m_Colour, 50.0);
    }

    @Override
    protected void AdditionalBehaviour(double deltaTime)
    {
        m_RestingPositon.SetVector(m_Position);
        m_RestingPositon.Y = CalculateRestingHeight(deltaTime);
        Vector3 force = m_Spring.CalculateSpringForce(m_Position, m_RestingPositon);

        ApplyForce(force);
    }

    private double CalculateRestingHeight(double deltaTime)
    {
        m_BobbingCounter += deltaTime * BOBBBING_SPEED;
        m_BobbingCounter %= Math.PI * 2;

        return (Math.sin(m_BobbingCounter) * BOBBING_HEIGHT) + RESTING_HEIGHT;
    }

    @Override
    public void Activate(Vector3 pos, Vector3 vel, Vector3 forward, double emissionForce, Colour hot, Colour cold, double lifeSpan)
    {
        super.Activate(pos, vel, forward, emissionForce, hot, cold, lifeSpan);

        m_Game.AddObjectToRenderer(m_FloorGrid);
    }

    @Override
    public void OnInvalidation()
    {
        m_Game.RemoveObjectFromRenderer(m_FloorGrid);
    }
}
