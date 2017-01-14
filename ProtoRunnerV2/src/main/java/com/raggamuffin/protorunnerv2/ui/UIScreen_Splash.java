package com.raggamuffin.protorunnerv2.ui;


import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.utils.Timer_Accumulation;

public class UIScreen_Splash extends UIScreen
{
	private final double SCREEN_DURATION = 3.0;//3
	
	private Timer_Accumulation m_Timer;
	private UILabel m_Company;
	
	private Publisher m_SplashScreenOverPublisher;

	public UIScreen_Splash(GameLogic game, UIManager uiManager)
	{
		super(game, uiManager);

		m_Company = null;
		
		m_Timer = new Timer_Accumulation(SCREEN_DURATION);
		
		PubSubHub pubSub = m_Game.GetPubSubHub();
		m_SplashScreenOverPublisher = pubSub.CreatePublisher(PublishedTopics.SwitchScreen);
	}

	@Override
	public void Create() 
	{
        super.Create();

		m_Company = new UILabel(m_Game.GetGameAudioManager(), m_UIManager);
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