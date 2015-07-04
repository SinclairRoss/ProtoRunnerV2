package com.raggamuffin.protorunnerv2.gamelogic;

import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;

public class GameStats 
{
	private GameLogic m_Game;
	
	private boolean m_Locked;
    private boolean m_PanicSwitchLocked;
	private boolean m_RealData;
	
	private int m_Score;
	private double m_PlayTime;
	private int m_ShotsFired;
	private int m_ShotsLanded;
	private int m_LivesUsed;
	
	private double m_WingmanADuration;
	private double m_WingmanBDuration;
	
	public GameStats(GameLogic Game)
	{
		m_Game = Game;

		m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.EnemyDestroyed, new EnemyDestroyedSubscriber());
		m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.EnemyHit, new EnemyHitSubscriber());
		m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerShotFired, new PlayerShotFiredSubscriber());
		m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerSpawned, new PlayerSpawnedSubscriber());
        m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.WingmanDestroyed, new WingmanDestroyedSubscriber());
        m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PanicSwitchFired, new PanicSwitchFiredSubscriber());
        m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PanicSwitchDepleted, new PanicSwitchDepletedSubscriber());

		ResetStats();
		
		m_Locked = true;
        m_PanicSwitchLocked = false;
		m_RealData = false;
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
        m_PanicSwitchLocked = false;
        m_RealData = true;
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
	
	public int GetShotsFired()
	{
		return m_ShotsFired;
	}
	
	public int GetShotsLanded()
	{
		return m_ShotsLanded;
	}
	
	public void SetStatsFromScoreTable()
	{
		//m_Score = row.GetScore();
		//m_ShotsFired = row.GetShotsFired();
		//m_ShotsLanded = row.GetShotsLanded();
		//m_PlayTime = row.GetPlayTime();
		//m_LivesUsed = row.GetNumReboots();
		
		//m_WingmanADuration = row.GetWingmanALife();
		//m_WingmanBDuration = row.GetWingmanBLife();
		
		m_RealData = false;
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
	
	public boolean isRealData()
	{
		return m_RealData;
	}
	
	private class EnemyDestroyedSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			if(m_Locked)
				return;

            if(m_PanicSwitchLocked)
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

            if(m_PanicSwitchLocked)
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

    private class PanicSwitchFiredSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_PanicSwitchLocked = true;
        }
    }

    private class PanicSwitchDepletedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_PanicSwitchLocked = false;
        }
    }
}