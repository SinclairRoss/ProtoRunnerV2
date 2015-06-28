package com.raggamuffin.protorunnerv2.ui;


import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.utils.Timer;

public class SplashScreen extends UIScreen
{
	private final double SCREEN_DURATION = 3.0;
	
	private Timer m_Timer;
	private UILabel m_Company;
	
	private Publisher m_SplashScreenOverPublisher;

	public SplashScreen(GameLogic Game, UIManager Manager) 
	{
		super(Game, Manager);

		m_Company = null;
		
		m_Timer = new Timer(SCREEN_DURATION);
		
		PubSubHub pubSub = Game.GetPubSubHub();
		m_SplashScreenOverPublisher = pubSub.CreatePublisher(PublishedTopics.SwitchScreen);
	}

	@Override
	public void Create() 
	{
        super.Create();

		m_Company = new UILabel(m_Game.GetGameAudioManager());
		m_Company.SetText(m_Game.GetContext().getString(R.string.company_name));
		m_Company.SetPosition(0, 0);
		m_Company.CentreHorizontal();
		
		m_UIManager.AddUIElement(m_Company);
		
		m_Company.Show(1.0);
		
		m_Timer.ResetTimer();
	}

	@Override
	public void Remove()
	{
        super.Remove();

		m_UIManager.RemoveUIElement(m_Company);
		m_Company = null;
	}

	@Override
	public void Update(double DeltaTime) 
	{
		m_Timer.Update(DeltaTime);
		
		if(m_Timer.TimedOut())
		{
			m_SplashScreenOverPublisher.Publish(UIScreens.MainMenu.ordinal());
		}
	}
}