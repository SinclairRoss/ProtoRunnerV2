package com.raggamuffin.protorunnerv2.gamelogic;

import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;

public class GameStats 
{
	private boolean m_Locked;
	
	private int m_Score;

	public GameStats(GameLogic Game)
	{
        Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.EnemyDestroyed, new EnemyDestroyedSubscriber());

        ResetStats();
		
		m_Locked = true;
	}
	
	public void ResetStats()
	{
		m_Score = 0;
        m_Locked = false;
    }

    public void Lock()
    {
        m_Locked = true;
    }
	
	public int GetScore()
	{
		return m_Score;
	}

	private class EnemyDestroyedSubscriber extends Subscriber
	{
		@Override
		public void Update(int args)
		{
			if(m_Locked)
				return;

			m_Score += args;
		}
	}
}