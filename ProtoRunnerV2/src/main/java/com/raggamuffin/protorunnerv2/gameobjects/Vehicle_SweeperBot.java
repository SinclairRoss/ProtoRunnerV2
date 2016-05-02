package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class Vehicle_SweeperBot extends Vehicle
{
    private double m_ElapsedTime;
    private final double SWEEP_SPEED = 0.0;
    private final double SWEEP_LENGTH = 10.0;
    private final double DEPTH = 4.0;

    public Vehicle_SweeperBot(GameLogic game)
    {
        super(game);

        m_Model = ModelType.Ring;
        SetBaseColour(Colours.HannahBlue);
        SetAffiliation(AffiliationKey.RedTeam);

        m_ElapsedTime = 0.0;

        m_OnDeathPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.EnemyDestroyed);

        m_Engine = new Engine_Standard(this, game);
        SetEngineOutput(0.0);
    }

    @Override
    public void Update(double deltaTime)
    {
        m_ElapsedTime += deltaTime * SWEEP_SPEED;
        m_ElapsedTime %= Math.PI * 2;
        m_Position.SetVector(Math.sin(m_ElapsedTime) * SWEEP_LENGTH, 0, DEPTH);

        super.Update(deltaTime);
    }

    @Override
    public void CleanUp()
    {

    }
}
