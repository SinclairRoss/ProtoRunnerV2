package com.raggamuffin.protorunnerv2.gamelogic;

import com.raggamuffin.protorunnerv2.gameobjects.Runner;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;

public class HealthRegenHandler
{
    private Runner m_Anchor;
    private boolean m_PanicLocked;

    public HealthRegenHandler(GameLogic game, Runner anchor)
    {
        m_Anchor = anchor;

        PubSubHub pubSubHub = game.GetPubSubHub();

        pubSubHub.SubscribeToTopic(PublishedTopics.PanicSwitchFired, new PanicSwitchFiredSubscriber());
        pubSubHub.SubscribeToTopic(PublishedTopics.PanicSwitchDepleted, new PanicSwitchDepletedSubscriber());
        pubSubHub.SubscribeToTopic(PublishedTopics.EnemyDestroyed, new EnemyDestroyedSubscriber());

        m_PanicLocked = false;
    }

    private class EnemyDestroyedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            if(m_PanicLocked)
                return;

            m_Anchor.ChargeEnergy(200);
        }
    }

    private class PanicSwitchFiredSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_PanicLocked = true;
        }
    }

    private class PanicSwitchDepletedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_PanicLocked = false;
        }
    }
}
