package com.raggamuffin.protorunnerv2.pubsub;

import java.util.ArrayList;

public class PubSubHub
{
    private ArrayList<Topic> m_Topics;

	public PubSubHub()
	{
        int numTopics = PublishedTopics.values().length;
        m_Topics = new ArrayList<>(numTopics);

        for(int i = 0; i < numTopics; i++)
		{
			m_Topics.add(new Topic());
		}
	}

	public Publisher CreatePublisher(PublishedTopics Topic)
	{
		return new Publisher(GetTopic(Topic));
	}

	public void SubscribeToTopic(PublishedTopics Topic, Subscriber Sub)
	{
		GetTopic(Topic).Subscribe(Sub);
	}

	public void UnsubscribeFromTopic(PublishedTopics Topic, Subscriber Sub)
	{
		GetTopic(Topic).Unsubscribe(Sub);
	}

	private Topic GetTopic(PublishedTopics topic)
	{
        return m_Topics.get(topic.ordinal());
	}
}