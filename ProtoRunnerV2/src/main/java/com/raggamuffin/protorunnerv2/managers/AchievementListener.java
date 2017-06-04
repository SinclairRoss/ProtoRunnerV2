package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;

public class AchievementListener
{
    private GameLogic m_Game;
    private GooglePlayService m_GPService;

    public AchievementListener(GameLogic game, GooglePlayService service)
    {
        m_Game = game;
        m_GPService = service;

        PubSubHub pubSub = m_Game.GetPubSubHub();

        pubSub.SubscribeToTopic(PublishedTopics.Fire, new TestSubscriber());
    }

    private class TestSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            String id = m_Game.GetContext().getString(R.string.achievement_test_id);
            m_GPService.UpdateAchievement(id, 1);
        }
    }
}