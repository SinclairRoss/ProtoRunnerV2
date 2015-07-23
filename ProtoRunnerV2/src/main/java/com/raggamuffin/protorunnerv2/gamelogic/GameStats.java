package com.raggamuffin.protorunnerv2.gamelogic;

import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;

public class GameStats 
{
	private boolean m_Locked;
	
	private int m_Score;
	private double m_PlayTime;
	private int m_ShotsFired;
	private int m_ShotsLanded;
	private int m_LivesUsed;
	
	private double m_WingmanADuration;
	private double m_WingmanBDuration;
	
	public GameStats(GameLogic Game)
	{
        Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.EnemyDestroyed, new EnemyDestroyedSubscriber());
        Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.EnemyHit, new EnemyHitSubscriber());
        Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerShotFired, new PlayerShotFiredSubscriber());
        Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerSpawned, new PlayerSpawnedSubscriber());
        Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.WingmanDestroyed, new WingmanDestroyedSubscriber());

        ResetStats();
		
		m_Locked = true;
	}
	
	public void Update(double deltaTime)
	{
		if(m_Locked)
			return;
		
		m_PlayTime += deltaTime;
	}
	
	public void ResetStats()
	{
		m_Score = 0;
		m_ShotsFired = 0;
		m_ShotsLanded = 0;
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
	
	public double GetAccuracy()
	{
		if(m_ShotsFired <= 0)
			return 0.0;
		
		return (double) m_ShotsLanded / m_ShotsFired;
	}
	
	public String GetPlayTimeString()
	{	
		int min = (int)m_PlayTime / 60;
		int sec = (int)m_PlayTime % 60;
		
		return min + "." + String.format("%02d", sec);
	}
	
	public double GetPlayTime()
	{
		return m_PlayTime;
	}
	
	public int GetLivesUsed()
	{
		return m_LivesUsed;
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

	private class EnemyHitSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			if(m_Locked)
				return;

			m_ShotsLanded++;
		}	
	}
	
	private class PlayerShotFiredSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			if(m_Locked)
				return;
			
			m_ShotsFired++;
		}	
	}
	
	private class PlayerSpawnedSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			if(m_Locked)
				return;
			
			m_LivesUsed++;
		}	
	}

    private class WingmanDestroyedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            if(m_Locked)
                return;

            if(m_WingmanADuration > 0)
                m_WingmanBDuration = m_PlayTime;
            else
                m_WingmanADuration = m_PlayTime;
        }
    }
}