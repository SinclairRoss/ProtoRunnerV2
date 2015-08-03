package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.Vehicles.Vehicle;
import com.raggamuffin.protorunnerv2.managers.ColourManager;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class PlayScreen extends UIScreen
{
	private final double LABEL_DURATION = 2.0;

	private Vehicle m_Player;
	
	private UIProgressBar m_HealthBar;

	private String m_RespawnText;
	private String m_WingmanDownText;

	public PlayScreen(GameLogic game, UIManager uiManager) 
	{
		super(game, uiManager);

		m_HealthBar = null;

		m_RespawnText = m_Game.GetContext().getString(R.string.revive_text);
		m_WingmanDownText = m_Game.GetContext().getString(R.string.wingman_down);

		m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerSpawned, new PlayerSpawnedSubscriber());
		m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.WingmanDestroyed, new WingmanDownSubscriber());
    }

	@Override
	public void Create() 
	{
		super.Create();
		
		String HealthString = m_Game.GetContext().getString(R.string.empty);

        ColourManager cManager = m_Game.GetColourManager();
		m_HealthBar = new UIProgressBar(1.6, m_Player.GetMaxHullPoints(), cManager.GetAccentingColour(), cManager.GetAccentTintColour(), new Colour(Colours.Clear), HealthString, UIProgressBar.Alignment.Center, m_Game.GetGameAudioManager(), m_UIManager);
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
	}

	private class PlayerSpawnedSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			m_Player = m_Game.GetVehicleManager().GetPlayer(); 
			
			m_MessageHandler.DisplayMessage(m_RespawnText, MessageOrientation.Center, 0.9, 1, LABEL_DURATION, 0.0);
		}	
	}

	private class WingmanDownSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			m_MessageHandler.DisplayMessage(m_WingmanDownText, MessageOrientation.Center, 0.9, 1, LABEL_DURATION, 0.0);
		}
	}
}
