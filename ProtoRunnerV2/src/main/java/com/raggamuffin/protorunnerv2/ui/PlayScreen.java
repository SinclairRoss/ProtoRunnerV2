package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gamelogic.GameSettings;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class PlayScreen extends UIScreen
{
	private final double LABEL_DURATION = 2.0;

	private Vehicle m_Player;
	
	private UIProgressBar m_HealthBar;

	private String m_DownButNotOutText;
	private String m_RespawnText;
	private String m_WingmanDownText;

	public PlayScreen(GameLogic game, UIManager uiManager) 
	{
		super(game, uiManager);

		m_HealthBar = null;
	
		m_DownButNotOutText = m_Game.GetContext().getString(R.string.down_but_not_out_text);
		m_RespawnText = m_Game.GetContext().getString(R.string.revive_text);
		m_WingmanDownText = m_Game.GetContext().getString(R.string.wingman_down);

		m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerDestroyed, new PlayerDestroyedSubscriber());
		m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerSpawned, new PlayerSpawnedSubscriber());
		m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.WingmanDestroyed, new WingmanDownSubscriber());
    }

	@Override
	public void Create() 
	{
		super.Create();
		
		String HealthString = m_Game.GetContext().getString(R.string.empty);	
		m_HealthBar = new UIProgressBar(3.0, m_Player.GetMaxHullPoints(), Colours.EmeraldGreen, Colours.Crimson, Colours.Clear, HealthString, UIProgressBar.Alignment.Center, m_Game.GetGameAudioManager());
		m_HealthBar.SetPosition(0.0, 0.9);
		
		m_UIManager.AddUIElement(m_HealthBar);
	}

	@Override
	public void Remove() 
	{
        super.Remove();

		m_UIManager.RemoveUIElement(m_HealthBar);
		m_HealthBar = null;
	}

	@Override
	public void Update(double deltaTime) 
	{
        super.Update(deltaTime);

		m_HealthBar.SetValue(m_Player.GetHullPoints());
		m_HealthBar.Update(deltaTime);
	}
	
	private class PlayerDestroyedSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			m_MessageHandler.DisplayMessage(m_DownButNotOutText, OptionalTextType.SecondWindTimeRemaining, MessageOrientation.Center, 2, GameSettings.SECOND_WIND_DURATION, 0.5);
		}	
	}
	
	private class PlayerSpawnedSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			m_Player = m_Game.GetVehicleManager().GetPlayer(); 
			
			m_MessageHandler.DisplayMessage(m_RespawnText, OptionalTextType.None, MessageOrientation.Center, 2, LABEL_DURATION, 0.0);
		}	
	}

	private class WingmanDownSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			m_MessageHandler.DisplayMessage(m_WingmanDownText, OptionalTextType.None, MessageOrientation.Center, 1, LABEL_DURATION, 0.0);
		}
	}
}
