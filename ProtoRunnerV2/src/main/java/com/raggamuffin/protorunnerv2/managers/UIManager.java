package com.raggamuffin.protorunnerv2.managers;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.ui.UIScreen_Aftermath;
import com.raggamuffin.protorunnerv2.ui.UIScreen_Credits;
import com.raggamuffin.protorunnerv2.ui.UIScreen_GameOver;
import com.raggamuffin.protorunnerv2.ui.UIScreen_HighScore;
import com.raggamuffin.protorunnerv2.ui.UIScreen_MainMenu;
import com.raggamuffin.protorunnerv2.ui.UIScreen_NewToGame;
import com.raggamuffin.protorunnerv2.ui.UIScreen_NotSignedIn;
import com.raggamuffin.protorunnerv2.ui.UIScreen_Play;
import com.raggamuffin.protorunnerv2.ui.UIScreen_Splash;
import com.raggamuffin.protorunnerv2.ui.UIScreen_TestMode;
import com.raggamuffin.protorunnerv2.ui.UIScreen_Tutorial;
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
	
	private ArrayList<UIElement> m_UIElements;
	private UIScreen m_Screen;
	
	private UIScreen_Splash m_SplashScreen;
	private UIScreen_MainMenu m_MenuScreen;
	private UIScreen_Credits m_CreditsScreen;
	private UIScreen_Play m_PlayScreen;
	private UIScreen_GameOver m_GameOverScreen;
	private UIScreen_Aftermath m_AftermathScreen;
	private UIScreen_Tutorial m_TutorialScreen;
    private UIScreen_NewToGame m_NewToGameScreen;
    private UIScreen_NotSignedIn m_NotSignedInScreen;
    private UIScreen_HighScore m_HighScoreScreen;
	private UIScreen_TestMode m_TestModeScreen;

	public UIManager(GameLogic Game)
	{
		m_Game = Game;
		
		WindowManager wm = (WindowManager) m_Game.GetContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();	
		Point size = new Point();
		display.getRealSize(size);
		
		m_ScreenSize = new Vector2(size);
		m_ScreenRatio = m_ScreenSize.I / m_ScreenSize.J;
		
		m_UIElements = new ArrayList<>();

		m_Screen 	 		 = null;

		m_SplashScreen 		 = new UIScreen_Splash(m_Game, this);
		m_MenuScreen 		 = new UIScreen_MainMenu(m_Game, this);
		m_CreditsScreen		 = new UIScreen_Credits(m_Game, this);
		m_PlayScreen 		 = new UIScreen_Play(m_Game, this);
		m_GameOverScreen 	 = new UIScreen_GameOver(m_Game, this);
		m_AftermathScreen 	 = new UIScreen_Aftermath(m_Game, this);
        m_TutorialScreen     = new UIScreen_Tutorial(m_Game, this);
        m_NewToGameScreen    = new UIScreen_NewToGame(m_Game, this);
        m_NotSignedInScreen  = new UIScreen_NotSignedIn(m_Game, this);
        m_HighScoreScreen    = new UIScreen_HighScore(m_Game, this);
        m_TestModeScreen     = new UIScreen_TestMode(m_Game, this);

		ShowScreen(UIScreens.Splash);
		
		m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.SwitchScreen, new ButtonPressedSubscriber());
	}
	
	public void Update(double deltaTime)
	{
		m_Screen.Update(deltaTime);

        for(UIElement element : m_UIElements)
            element.Update(deltaTime);
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
            case NewToGame:
                return m_NewToGameScreen;
            case NotSignedIn:
                return m_NotSignedInScreen;
            case Leaderboards:
                return m_HighScoreScreen;
            case TestMode:
                return m_TestModeScreen;
            default:
                return null;
        }
    }
	
	public void ShowScreen(UIScreens screen)
	{
		if(m_Screen != null)
        {
            m_Screen.Remove();
        }

		m_Screen = GetScreen(screen);
		m_Screen.Create();
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
        {
            Element.Show();
        }
	}
	
	public void RemoveUIElement(UIElement Element)
	{
		m_Game.RemoveObjectFromRenderer(Element);
		m_UIElements.remove(Element);
	}
	
	public void RemoveUIElement(UIProgressBar Element)
	{
		m_Game.RemoveObjectFromRenderer(Element);
		m_UIElements.remove(Element);
		
		UILabel Label = Element.GetLabel();
		m_Game.RemoveObjectFromRenderer(Label);
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
