package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.utils.Timer;

public class UIScreen_Splash extends UIScreen
{
	private final double SCREEN_DURATION = 3.0;
	
	private Timer m_Timer;
	private UIElement_Label m_Company;
	
	private Publisher m_SplashScreenOverPublisher;

	public UIScreen_Splash(GameLogic game, UIManager uiManager)
	{
		super(game, uiManager);

		m_Company = null;
		
		m_Timer = new Timer(SCREEN_DURATION);
		
		PubSubHub pubSub = m_Game.GetPubSubHub();
		m_SplashScreenOverPublisher = pubSub.CreatePublisher(PublishedTopics.SwitchScreen);
	}

	@Override
	public void Create() 
	{
		String text = m_Game.GetContext().getString(R.string.company_name);
		m_Company = new UIElement_Label(text, UIConstants.FONTSIZE_TITLE, 0, 0, Alignment.Center);
		m_UIManager.AddUIElement(m_Company);

        m_Timer.Start();
	}

	@Override
	public void CleanUp()
	{
		m_Company = null;
	}

	@Override
	public void Update(double deltaTime)
	{
		if(m_Timer.HasElapsed())
		{
			m_SplashScreenOverPublisher.Publish(UIScreens.MainMenu.ordinal());
		}
	}
}