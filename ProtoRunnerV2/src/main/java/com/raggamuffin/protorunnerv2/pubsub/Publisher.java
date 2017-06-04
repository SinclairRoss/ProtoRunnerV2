package com.raggamuffin.protorunnerv2.pubsub;

public class Publisher 
{	
	private Topic m_Topic;
	
	public Publisher(Topic PubTopic)
	{
		m_Topic = PubTopic;
	}

	public void Publish() { Publish(-1); }
	public void Publish(Object args) { m_Topic.Update(args); }
}
