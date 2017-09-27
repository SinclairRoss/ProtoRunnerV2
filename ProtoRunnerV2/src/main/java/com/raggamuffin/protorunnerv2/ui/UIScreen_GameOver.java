package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.utils.Timer;

public class UIScreen_GameOver extends UIScreen
{
	private final double SCREEN_DURATION = 2.0;

	private Timer m_Timer;
	private UIElement_Label m_GameOverMessage;
	
	private Publisher m_GameOverScreenOverPublisher;
	
	public UIScreen_GameOver(GameLogic Game, UIManager Manager)
	{
		super(Game, Manager);

		m_GameOverMessage = null;
		m_Timer = null;
		
		PubSubHub pubSub = Game.GetPubSubHub();
		m_GameOverScreenOverPublisher = pubSub.CreatePublisher(PublishedTopics.SwitchScreen);
	}
	
	@Override
	public void Create()
	{
		String text = m_Game.GetContext().getString(R.string.game_over_message);
		m_GameOverMessage = new UIElement_Label(text, UIConstants.FONTSIZE_TITLE, 0, 0, Alignment.Center);
		m_UIManager.AddLabel(m_GameOverMessage);

		m_Timer = new Timer(SCREEN_DURATION);
		m_Timer.Start();
	}

	@Override
	public void CleanUp()
	{
		m_Timer = null;
		m_GameOverMessage = null;
	}

	@Override
	public void Update(double deltaTime) 
	{
		if(m_Timer.HasElapsed())
		{
			m_GameOverScreenOverPublisher.Publish(UIScreens.MainMenu.ordinal());
		}
	}
}
