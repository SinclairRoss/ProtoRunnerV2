package com.raggamuffin.protorunnerv2.pubsub;

import java.util.Vector;

public class Topic 
{
	private Vector<Subscriber> m_Subscribers;
	
	public Topic()
	{
		m_Subscribers = new Vector<Subscriber>();
	}
	
	public void Update(final int args)
	{
		for(Subscriber Sub : m_Subscribers)
		{
			Sub.Update(args);
		}
	}
	
	public void Subscribe(Subscriber Sub)
	{
		m_Subscribers.add(Sub);
	}
	
	public void Unsubscribe(Subscriber Sub)
	{
		m_Subscribers.remove(Sub);
	}
}
