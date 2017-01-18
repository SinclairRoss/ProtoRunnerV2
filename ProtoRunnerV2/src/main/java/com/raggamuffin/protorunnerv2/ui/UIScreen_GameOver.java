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
	private UILabel m_GameOverMessage;
	
	private Publisher m_GameOverScreenOverPublisher;
	
	public UIScreen_GameOver(GameLogic Game, UIManager Manager)
	{
		super(Game, Manager);

		m_GameOverMessage = null;
		m_Timer = new Timer(SCREEN_DURATION);
		
		PubSubHub pubSub = Game.GetPubSubHub();
		m_GameOverScreenOverPublisher = pubSub.CreatePublisher(PublishedTopics.SwitchScreen);
	}
	
	@Override
	public void Create()
	{
        super.Create();

		m_GameOverMessage = new UILabel(m_Game.GetGameAudioManager(), m_UIManager);
		m_GameOverMessage.SetText(m_Game.GetContext().getString(R.string.game_over_message));
		m_GameOverMessage.SetPosition(0, 0);
		m_GameOverMessage.CentreHorizontal();

		m_UIManager.AddUIElement(m_GameOverMessage, false);

		m_Timer.Start();

		m_GameOverMessage.Show();
	}

	@Override
	public void Remove() 
	{
        super.Remove();

		m_UIManager.RemoveUIElement(m_GameOverMessage);
		m_GameOverMessage = null;
	}

	@Override
	public void Update(double deltaTime) 
	{
		if(m_Timer.HasElapsed())
		{
			m_GameOverScreenOverPublisher.Publish(UIScreens.Aftermath.ordinal());
		}
	}
}
