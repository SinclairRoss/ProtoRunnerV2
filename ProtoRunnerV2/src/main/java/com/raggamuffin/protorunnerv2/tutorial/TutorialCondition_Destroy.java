package com.raggamuffin.protorunnerv2.tutorial;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Runner;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class TutorialCondition_Destroy extends TutorialCondition
{
    private final int m_NumDummies;
    private int m_DummiesDestroyed;
    private final double m_MinSpawnDistance;
    private final double m_MaxSpawnDistance;

    public TutorialCondition_Destroy(GameLogic game, String message, int amount)
    {
        super(game, message, OptionalElement.ProgressBar);

        m_NumDummies = amount;
        m_DummiesDestroyed = 0;
        m_MinSpawnDistance = 5.0;
        m_MaxSpawnDistance = 20.0;

        m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.DummyDestroyed, new DummyDestroyedSubscriber());
    }

    @Override
    public void Update(double deltaTime)
    {

    }

    @Override
    public boolean ConditionComplete()
    {
        return m_DummiesDestroyed >= m_NumDummies;
    }

    @Override
    public double GetProgress()
    {
        return MathsHelper.Normalise(m_DummiesDestroyed, 0, m_NumDummies);
    }

    @Override
    public void Initialise()
    {
        super.Initialise();

        Runner player = m_Game.GetVehicleManager().GetPlayer();

        if(player == null)
            return;

        Vector3 playerPos = player.GetPosition();

        double deltaTheta = (Math.PI * 2) / m_NumDummies;
        double theta = 0.0;

        for(int i = 0; i < m_NumDummies; i++)
        {
            double distance = 60;

            double x = (Math.cos(theta) * distance) + playerPos.I;
            double z = (Math.sin(theta) * distance) + playerPos.K;
            m_Game.GetVehicleManager().SpawnDummy(x, z);

            theta += deltaTheta;
        }

        m_DummiesDestroyed = 0;
    }

    private class DummyDestroyedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_DummiesDestroyed++;
        }
    }
}
