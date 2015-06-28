package com.raggamuffin.protorunnerv2.pubsub;

public class Publisher 
{	
	private Topic m_Topic;
	
	public Publisher(Topic PubTopic)
	{
		m_Topic = PubTopic;
	}
	
	public void Publish(int args)
	{
		m_Topic.Update(args);
	}
	
	public void Publish()
	{
		m_Topic.Update(-1);
	}
}
