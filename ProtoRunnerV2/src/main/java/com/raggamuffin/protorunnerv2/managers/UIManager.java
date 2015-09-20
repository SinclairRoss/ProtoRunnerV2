package com.raggamuffin.protorunnerv2.managers;

import java.util.Iterator;
import java.util.Vector;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.ui.AftermathScreen;
import com.raggamuffin.protorunnerv2.ui.CreditsScreen;
import com.raggamuffin.protorunnerv2.ui.GameOverScreen;
import com.raggamuffin.protorunnerv2.ui.MenuScreen;
import com.raggamuffin.protorunnerv2.ui.NewToGameScreen;
import com.raggamuffin.protorunnerv2.ui.NotSignedInScreen;
import com.raggamuffin.protorunnerv2.ui.PlayScreen;
import com.raggamuffin.protorunnerv2.ui.RebootScreen;
import com.raggamuffin.protorunnerv2.ui.SplashScreen;
import com.raggamuffin.protorunnerv2.ui.TutorialScreen;
import com.raggamuffin.protorunnerv2.ui.UIElement;
import com.raggamuffin.protorunnerv2.ui.UILabel;
import com.raggamuffin.protorunnerv2.ui.UIProgressBar;
import com.raggamuffin.protorunnerv2.ui.UIScreen;
import com.raggamuffin.protorunnerv2.ui.UIScreens;
import com.raggamuffin.protorunnerv2.utils.Vector2;

public class UIManager
{	
	private GameLogic m_Game;
	
	private Vector2 m_ScreenSize;
	private double m_ScreenRatio;
	
	private Vector<UIElement> m_UIElements;
	private UIScreen m_Screen;
	
	private SplashScreen m_SplashScreen;
	private MenuScreen m_MenuScreen;
	private CreditsScreen m_CreditsScreen;
	private PlayScreen m_PlayScreen;
	private GameOverScreen m_GameOverScreen;
	private AftermathScreen m_AftermathScreen;
	private TutorialScreen m_TutorialScreen;
    private RebootScreen m_RebootScreen;
    private NewToGameScreen m_NewToGameScreen;
    private NotSignedInScreen m_NotSignedInScreen;

    private UIScreens m_PreviousScreen;
    private UIScreens m_CurrentScreen;

	public UIManager(GameLogic Game)
	{
		m_Game = Game;
		
		WindowManager wm = (WindowManager) m_Game.GetContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();	
		Point size = new Point();
		display.getRealSize(size);
		
		m_ScreenSize = new Vector2(size);
		m_ScreenRatio = m_ScreenSize.I / m_ScreenSize.J;
		
		m_UIElements = new Vector<UIElement>();

		m_Screen 	 		 = null;

		m_SplashScreen 		 = new SplashScreen(m_Game, this);
		m_MenuScreen 		 = new MenuScreen(m_Game, this);
		m_CreditsScreen		 = new CreditsScreen(m_Game, this);
		m_PlayScreen 		 = new PlayScreen(m_Game, this);
		m_GameOverScreen 	 = new GameOverScreen(m_Game, this);
		m_AftermathScreen 	 = new AftermathScreen(m_Game, this);
        m_TutorialScreen     = new TutorialScreen(m_Game, this);
        m_RebootScreen       = new RebootScreen(m_Game, this);
        m_NewToGameScreen    = new NewToGameScreen(m_Game, this);
        m_NotSignedInScreen  = new NotSignedInScreen(m_Game, this);

        m_CurrentScreen  = UIScreens.None;
        m_PreviousScreen = UIScreens.None;

		ShowScreen(UIScreens.Splash);
		
		m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.SwitchScreen, new ButtonPressedSubscriber());
	}
	
	public void Update(double DeltaTime)
	{
		m_Screen.Update(DeltaTime);
		
		for(Iterator<UIElement> Iter = m_UIElements.iterator(); Iter.hasNext();)
		{
			Iter.next().Update(DeltaTime);
		}
	}

    public UIScreen GetScreen(UIScreens screen)
    {
        switch(screen)
        {
            case Splash:
                return m_SplashScreen;
            case MainMenu:
                return m_MenuScreen;
            case Credits:
                return m_CreditsScreen;
            case Play:
                return m_PlayScreen;
            case GameOverScreen:
                return m_GameOverScreen;
            case Aftermath:
                return m_AftermathScreen;
            case Tutorial:
                return m_TutorialScreen;
            case Reboot:
                return m_RebootScreen;
            case NewToGame:
                return m_NewToGameScreen;
            case NotSignedIn:
                return m_NotSignedInScreen;
            default:
                return null;
        }
    }
	
	public void ShowScreen(UIScreens screen)
	{
		if(screen == UIScreens.None)
			return;

		if(m_Screen != null)
			m_Screen.Remove();

        m_PreviousScreen = m_CurrentScreen;
        m_CurrentScreen = screen;

		m_Screen = GetScreen(m_CurrentScreen);
		m_Screen.Create();
	}

    public void ShowPreviousScreen()
    {
        ShowScreen(m_PreviousScreen);
    }
	
	public void AddUIElement(UIElement element)
	{
		AddUIElement(element, true);
	}

	public void AddUIElement(UIElement Element, boolean visible)
	{
		m_Game.AddObjectToRenderer(Element);
		m_UIElements.add(Element);
		
		if(visible)
			Element.Show();
	}
	
	public void RemoveUIElement(UIElement Element)
	{
		m_Game.RemoveTrailFromRenderer(Element);
		m_UIElements.remove(Element);
	}
	
	public void RemoveUIElement(UIProgressBar Element)
	{
		m_Game.RemoveTrailFromRenderer(Element);
		m_UIElements.remove(Element);
		
		UILabel Label = Element.GetLabel();
		m_Game.RemoveTrailFromRenderer(Label);
		m_UIElements.remove(Label);
	}

	public Vector2 GetScreenSize()
	{
		return m_ScreenSize;
	}
	
	public double GetScreenRatio()
	{
		return m_ScreenRatio;
	}
	
	private class ButtonPressedSubscriber extends Subscriber
	{
		@Override
		public void Update(int args) 
		{
			ShowScreen(UIScreens.values()[args]);
		}	
	}
}
