package com.raggamuffin.protorunnerv2.managers;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.ui.TouchDisplay;
import com.raggamuffin.protorunnerv2.ui.UIElement;
import com.raggamuffin.protorunnerv2.ui.UIScreen;
import com.raggamuffin.protorunnerv2.ui.UIScreen_Aftermath;
import com.raggamuffin.protorunnerv2.ui.UIScreen_Credits;
import com.raggamuffin.protorunnerv2.ui.UIScreen_GameOver;
import com.raggamuffin.protorunnerv2.ui.UIScreen_HighScore;
import com.raggamuffin.protorunnerv2.ui.UIScreen_LearnToTouch;
import com.raggamuffin.protorunnerv2.ui.UIScreen_MainMenu;
import com.raggamuffin.protorunnerv2.ui.UIScreen_NotSignedIn;
import com.raggamuffin.protorunnerv2.ui.UIScreen_Play;
import com.raggamuffin.protorunnerv2.ui.UIScreen_Splash;
import com.raggamuffin.protorunnerv2.ui.UIScreens;

import java.util.ArrayList;

public class UIManager
{
	private GameLogic m_Game;

	private ArrayList<UIElement> m_UIElements;
	private UIScreen m_Screen;
	
	private UIScreen_Splash m_SplashScreen;
	private UIScreen_MainMenu m_MenuScreen;
	private UIScreen_Credits m_CreditsScreen;
	private UIScreen_Play m_PlayScreen;
	private UIScreen_GameOver m_GameOverScreen;
	private UIScreen_Aftermath m_AftermathScreen;
    private UIScreen_NotSignedIn m_NotSignedInScreen;
    private UIScreen_HighScore m_HighScoreScreen;
    private UIScreen_LearnToTouch m_LearnToTouch;

	private TouchDisplay m_TouchDisplay;

	private UIScreen m_NextScreen;

	public UIManager(GameLogic game)
	{
		m_Game = game;
		
		WindowManager wm = (WindowManager) m_Game.GetContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();	
		Point size = new Point();
		display.getRealSize(size);

		m_UIElements = new ArrayList<>();

		m_TouchDisplay = new TouchDisplay(m_Game);

		m_Screen 	 		 = null;

		m_SplashScreen 		 = new UIScreen_Splash(m_Game, this);
		m_MenuScreen 		 = new UIScreen_MainMenu(m_Game, this);
		m_CreditsScreen		 = new UIScreen_Credits(m_Game, this);
		m_PlayScreen 		 = new UIScreen_Play(m_Game, this);
		m_GameOverScreen 	 = new UIScreen_GameOver(m_Game, this);
		m_AftermathScreen 	 = new UIScreen_Aftermath(m_Game, this);
        m_NotSignedInScreen  = new UIScreen_NotSignedIn(m_Game, this);
        m_HighScoreScreen    = new UIScreen_HighScore(m_Game, this);
        m_LearnToTouch       = new UIScreen_LearnToTouch(m_Game, this);

		ShowScreen_Immediate(m_LearnToTouch);
        m_NextScreen = null;
		
		m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.SwitchScreen, new ButtonPressedSubscriber());
	}
	
	public void Update(double deltaTime)
	{
		int elementCount = m_UIElements.size();
		for (int i = 0; i < elementCount; ++i)
		{
			m_UIElements.get(i).Update(deltaTime);
		}

        m_Screen.Update(deltaTime);
        m_TouchDisplay.Update(deltaTime);

		if(m_NextScreen != null)
		{
            ShowScreen_Immediate(m_NextScreen);
            m_NextScreen = null;
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
            case NotSignedIn:
                return m_NotSignedInScreen;
            case Leaderboards:
                return m_HighScoreScreen;
            case LearnToTouch:
                return m_LearnToTouch;
            default:
                return null;
        }
    }
	
	public void ShowScreen(UIScreens screen)
	{
		m_NextScreen = GetScreen(screen);
	}

	private void ShowScreen_Immediate(UIScreen screen)
    {
        CleanScreen();

        m_Screen = screen;
        m_Screen.Create();
    }

	private void CleanScreen()
	{
        if(m_Screen != null)
        {
			int numUIElements = m_UIElements.size();
			for(int i = 0; i < numUIElements; ++i)
			{
				m_Game.RemoveObjectFromRenderer(m_UIElements.get(i));
			}
			m_UIElements.clear();

			m_Screen.CleanUp();
        }
    }

    public void AddUIElement(UIElement element)
	{
		m_Game.AddObjectToRenderer(element);
		m_UIElements.add(element);
	}

	public void RemoveUIElement(UIElement element)
	{
		m_Game.RemoveObjectFromRenderer(element);
		m_UIElements.remove(element);
	}

	private class ButtonPressedSubscriber extends Subscriber
	{
		@Override
		public void Update(Object args)
		{
			int screenOrdinal = (int)args;
			ShowScreen(UIScreens.values()[screenOrdinal]);
		}	
	}

	public TouchDisplay GetTouchDisplay() { return m_TouchDisplay; }
}
