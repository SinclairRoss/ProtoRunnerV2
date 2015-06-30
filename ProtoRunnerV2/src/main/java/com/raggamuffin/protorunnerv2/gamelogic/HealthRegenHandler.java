package com.raggamuffin.protorunnerv2.gamelogic;

import com.raggamuffin.protorunnerv2.gameobjects.Runner;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;

public class HealthRegenHandler
{
    private GameLogic m_Game;
    private Runner m_Player;
    private boolean m_Locked;
    private boolean m_PanicLocked;

    public HealthRegenHandler(GameLogic game)
    {
        m_Game = game;

        PubSubHub pubSubHub = m_Game.GetPubSubHub();

        pubSubHub.SubscribeToTopic(PublishedTopics.PanicSwitchFired, new PanicSwitchFiredSubscriber());
        pubSubHub.SubscribeToTopic(PublishedTopics.PanicSwitchDepleted, new PanicSwitchDepletedSubscriber());
        pubSubHub.SubscribeToTopic(PublishedTopics.PlayerSpawned, new PlayerSpawnedSubscriber());
        pubSubHub.SubscribeToTopic(PublishedTopics.PlayerDestroyed, new PlayerDestroyedSubscriber());
        pubSubHub.SubscribeToTopic(PublishedTopics.EnemyDestroyed, new EnemyDestroyedSubscriber());

        m_Locked = true;
        m_PanicLocked = false;
    }

    private class PlayerSpawnedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_Player = m_Game.GetVehicleManager().GetPlayer();
            m_Locked = false;
        }
    }

    private class PlayerDestroyedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_Player = null;
            m_Locked = true;
        }
    }

    private class EnemyDestroyedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            if(m_Locked)
                return;

            if(m_PanicLocked)
                return;

            m_Player.ChargeEnergy(200);
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
