package com.raggamuffin.protorunnerv2.gamelogic;

import com.raggamuffin.protorunnerv2.gameobjects.Runner;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.Weapon;
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
	private Runner m_Player;
	
	private Timer m_SecondWindTimer;
	
	private Publisher m_GameOverPublisher;
	
	private Vector3 m_PlayerPosition;
	private double m_PlayerOrientation;
	private int m_HealthDivider;
	
	private WeaponSlot m_LastSelectedWeapon;
	
	public SecondWindHandler(GameLogic game)
	{
		m_Game = game;
		m_SecondWindTimer = new Timer(GameSettings.SECOND_WIND_DURATION);

		m_SecondWindState = SecondWindState.Idle;
		
		PubSubHub pubSubHub = m_Game.GetPubSubHub();
		
		m_GameOverPublisher = pubSubHub.CreatePublisher(PublishedTopics.EndGame);
		
		pubSubHub.SubscribeToTopic(PublishedTopics.EndGame, new EndGameSubscriber());
		pubSubHub.SubscribeToTopic(PublishedTopics.PlayerDestroyed, new PlayerDestroyedSubscriber());	
		pubSubHub.SubscribeToTopic(PublishedTopics.EnemyDestroyed, new EnemyDestroyedSubscriber());
		pubSubHub.SubscribeToTopic(PublishedTopics.PlayerSpawned, new PlayerSpawnedSubscriber());
		pubSubHub.SubscribeToTopic(PublishedTopics.PlayerSwitchedWeapon, new PlayerSwitchedWeaponSubscriber());
		
		m_PlayerPosition = new Vector3();
		m_PlayerOrientation = 0.0;
		
		m_HealthDivider = 1;
		
		m_LastSelectedWeapon = WeaponSlot.Left;
	}
	
	public void Update(double deltaTime)
	{
		switch(m_SecondWindState)
		{
			case Idle:
			{	
				if(m_Player == null)
					break;
				
				m_PlayerPosition.SetVector(m_Player.GetPosition());
				m_PlayerOrientation = m_Player.GetOrientation();
				
				break;
			}
			case Active:
			{
				m_SecondWindTimer.Update(deltaTime);
			
				if(m_SecondWindTimer.TimedOut())
				{
					m_GameOverPublisher.Publish();
					m_SecondWindState = SecondWindState.Idle;

					return;
				}
				
				break;
			}
			
			case Respawn:
			{
				m_Game.GetVehicleManager().SpawnPlayer();
				m_SecondWindState = SecondWindState.Idle;
				m_Player.SelectWeaponBySlot(m_LastSelectedWeapon);
				break;
			}
		}
	}
	
	private class PlayerSpawnedSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			m_SecondWindTimer.ResetTimer();
			m_Player = m_Game.GetVehicleManager().GetPlayer();
			m_Player.SetPosition(m_PlayerPosition);
			m_Player.SetOrientation(m_PlayerOrientation); 
			m_Player.SetHullPoints(m_Player.GetMaxHullPoints() / m_HealthDivider);
			m_HealthDivider = 2;
		}	
	}
	
	private class EndGameSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			m_PlayerPosition.SetVector(0);
			m_PlayerOrientation = 0.0;
			m_HealthDivider = 1;
		}	
	}
	
	private class PlayerDestroyedSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			m_SecondWindState = SecondWindState.Active;
			m_Player = null;
		}	
	}
	
	private class EnemyDestroyedSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			if(m_SecondWindState != SecondWindState.Active)
				return;

			m_SecondWindState = SecondWindState.Respawn;
		}	
	}
	
	private class PlayerSwitchedWeaponSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			if(m_Player != null)
				m_LastSelectedWeapon = m_Player.GetWeaponSlot();
		}	
	}
	
	public double GetSecondWindTimeRemaining()
	{
		return m_SecondWindTimer.GetInverseTimeRemaining();
	}
}	
