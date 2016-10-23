package com.raggamuffin.protorunnerv2.gamelogic;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle_Runner;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.WeaponSlot;

public class SecondWindHandler 
{
	private enum SecondWindState
	{
		Active,
		Idle,
		Respawn
	}

	private SecondWindState m_SecondWindState;
	
	private GameLogic m_Game;
	private Vehicle_Runner m_Player;

    private final double SECOND_WIND_DURATION = 5.0;
	private Timer m_SecondWindTimer;
	
	private Publisher m_GameOverPublisher;
	
	private Vector3 m_PlayerPosition;
	private double m_PlayerOrientation;
	
	private WeaponSlot m_LastSelectedWeapon;

    private boolean m_AutoRespawn;
	
	public SecondWindHandler(GameLogic game)
	{
		m_Game = game;
		m_SecondWindTimer = new Timer(SECOND_WIND_DURATION);

		m_SecondWindState = SecondWindState.Idle;
		
		PubSubHub pubSubHub = m_Game.GetPubSubHub();
		
		m_GameOverPublisher = pubSubHub.CreatePublisher(PublishedTopics.EndGame);

		pubSubHub.SubscribeToTopic(PublishedTopics.PlayerDestroyed, new PlayerDestroyedSubscriber());	
		pubSubHub.SubscribeToTopic(PublishedTopics.EnemyDestroyed, new EnemyDestroyedSubscriber());
		pubSubHub.SubscribeToTopic(PublishedTopics.PlayerSpawned, new PlayerSpawnedSubscriber());
		pubSubHub.SubscribeToTopic(PublishedTopics.PlayerSwitchedWeapon, new PlayerSwitchedWeaponSubscriber());
		
		m_PlayerPosition = new Vector3();
		m_PlayerOrientation = 0.0;
		
		m_LastSelectedWeapon = WeaponSlot.Left;

        m_AutoRespawn = false;
	}
	
	public void Update(double deltaTime)
	{
		switch(m_SecondWindState)
		{
			case Idle:
			{
				break;
			}
			case Active:
			{
				m_SecondWindTimer.Update(deltaTime);
			
				if(m_SecondWindTimer.TimedOut())
				{
                    if(m_AutoRespawn)
                    {
                        m_SecondWindState = SecondWindState.Respawn;
                    }
                    else
                    {
                        m_GameOverPublisher.Publish();
                        m_SecondWindState = SecondWindState.Idle;
                    }
				}
				
				break;
			}
			
			case Respawn:
			{
				m_Game.GetVehicleManager().SpawnPlayer();
				m_SecondWindState = SecondWindState.Idle;
				m_Player.SelectWeaponBySlot(m_LastSelectedWeapon);

                m_Game.GetGameAudioManager().PlaySound(AudioClips.Respawn);

				break;
			}
		}
	}

    public void Reset()
    {
        m_PlayerPosition.SetVector(0);
        m_PlayerOrientation = 0.0;
    }
	
	private class PlayerSpawnedSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			m_SecondWindTimer.ResetTimer();
			m_Player = m_Game.GetVehicleManager().GetPlayer();
			m_Player.SetPosition(m_PlayerPosition);
			m_Player.SetYaw(m_PlayerOrientation);
		}	
	}
	
	private class PlayerDestroyedSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
            if(m_Player != null)
            {
                m_SecondWindState = SecondWindState.Active;
                m_PlayerPosition.SetVector(m_Player.GetPosition());
                m_PlayerOrientation = m_Player.GetYaw();
                m_Player = null;
            }
		}	
	}
	
	private class EnemyDestroyedSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			if(m_SecondWindState == SecondWindState.Active)
			{
				m_SecondWindState = SecondWindState.Respawn;
			}
		}	
	}
	
	private class PlayerSwitchedWeaponSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			if(m_Player != null)
			{
				m_LastSelectedWeapon = m_Player.GetWeaponSlot();
			}
		}	
	}

    public double GetInverseProgress()
    {
        return m_SecondWindTimer.GetInverseProgress();
    }

    public void AutoSpawnOn()
    {
        m_AutoRespawn = true;
    }

    public void AutoSpawnOff()
    {
        m_AutoRespawn = false;
    }
}	
