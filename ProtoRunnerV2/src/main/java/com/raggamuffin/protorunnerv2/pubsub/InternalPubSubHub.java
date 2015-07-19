package com.raggamuffin.protorunnerv2.pubsub;

import java.util.ArrayList;

public class InternalPubSubHub
{
    private ArrayList<Topic> m_Topics;

    public InternalPubSubHub()
    {
        int numTopics = InternalTopics.values().length;
        m_Topics = new ArrayList<Topic>(numTopics);

        for(int i = 0; i < numTopics; i++)
            m_Topics.add(new Topic());
    }

    public Publisher CreatePublisher(InternalTopics Topic)
    {
        return new Publisher(GetTopic(Topic));
    }

    public void SubscribeToTopic(InternalTopics Topic, Subscriber Sub)
    {
        GetTopic(Topic).Subscribe(Sub);
    }

    public void UnsubscribeFromTopic(InternalTopics Topic, Subscriber Sub)
    {
        GetTopic(Topic).Unsubscribe(Sub);
    }

    private Topic GetTopic(InternalTopics topic)
    {
        return m_Topics.get(topic.ordinal());
    }
}