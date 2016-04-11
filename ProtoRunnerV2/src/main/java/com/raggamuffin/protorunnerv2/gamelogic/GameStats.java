package com.raggamuffin.protorunnerv2.gamelogic;

import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;

public class GameStats 
{
	private boolean m_Locked;

    private int m_Score;
    private double m_PlayTime;
    private int m_LivesUsed;

    private double m_WingmanADuration;
    private double m_WingmanBDuration;

	public GameStats(GameLogic Game)
	{
        Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.EnemyDestroyed, new EnemyDestroyedSubscriber());
        Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerSpawned, new PlayerSpawnedSubscriber());
        Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.WingmanDestroyed, new WingmanDestroyedSubscriber());

        ResetStats();
		
		m_Locked = true;
	}

    public void Update(double deltaTime)
    {
        if(!m_Locked)
        {
            m_PlayTime += deltaTime;
        }
    }

    public void ResetStats()
    {
        m_Score = 0;
        m_PlayTime = 0;
        m_LivesUsed = 0;

        m_WingmanADuration = 0;
        m_WingmanBDuration = 0;

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

    public String GetPlayTimeString()
    {
        int min = (int)m_PlayTime / 60;
        int sec = (int)m_PlayTime % 60;

        return min + "." + String.format("%02d", sec);
    }

    public double GetWingmanADuration()
    {
        if(m_WingmanADuration <= 0)
            return m_PlayTime;

        return m_WingmanADuration;
    }

    public double GetWingmanBDuration()
    {
        if(m_WingmanBDuration <= 0)
            return m_PlayTime;

        return m_WingmanBDuration;
    }

    public double GetPlayTime()
    {
        return m_PlayTime;
    }

    public int GetPlayTimeMillis()
    {
        double playTimeMillis = m_PlayTime * 1000;
        return (int)playTimeMillis;
    }

    public int GetLivesUsed()
    {
        return m_LivesUsed;
    }

	private class EnemyDestroyedSubscriber extends Subscriber
	{
		@Override
		public void Update(int args)
		{
			if(!m_Locked)
            {
                m_Score += args;
            }
		}
	}

    private class PlayerSpawnedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            if(!m_Locked)
            {
                m_LivesUsed++;
            }
        }
    }

    private class WingmanDestroyedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            if(!m_Locked)
            {
                if (m_WingmanADuration > 0)
                {
                    m_WingmanBDuration = m_PlayTime;
                }
                else
                {
                    m_WingmanADuration = m_PlayTime;
                }
            }
        }
    }
}