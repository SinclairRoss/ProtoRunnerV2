package com.raggamuffin.protorunnerv2.pubsub;

import java.util.Vector;

public class Topic 
{
	private Vector<Subscriber> m_Subscribers;
	
	public Topic()
	{
		m_Subscribers = new Vector<>();
	}
	
	public void Update(final Object args)
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

    public int NumSubscribers()
    {
        return m_Subscribers.size();
    }
}
