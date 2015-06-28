package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;

public class MenuScreen extends UIScreen
{
	private UIElement m_Title;
	private UIButton m_Play;
	private UIButton m_HighScore;
	private UIButton m_Credits;
    private UIButton m_Tutorial;
	
	public MenuScreen(GameLogic Game, UIManager Manager) 
	{
		super(Game, Manager);

		m_Title 	= null;
		m_Play 		= null;
		m_Credits   = null;
        m_Tutorial  = null;
	}

	@Override
	public void Create() 
	{	
		super.Create();
		
		m_Title 	= CreateTitle(m_Game.GetContext().getString(R.string.app_name));
		m_Play 		= CreateButton(m_Game.GetContext().getString(R.string.button_play), PublishedTopics.StartGame);
		m_HighScore = CreateButton(m_Game.GetContext().getString(R.string.button_scores), UIScreens.HighScore);
        m_Tutorial  = CreateButton(m_Game.GetContext().getString(R.string.button_tutorial), PublishedTopics.StartTutorial);
        m_Credits	= CreateButton(m_Game.GetContext().getString(R.string.button_credits), UIScreens.Credits);
    }

	@Override
	public void Remove() 
	{
        super.Remove();

		m_UIManager.RemoveUIElement(m_Title);
		m_Title = null;
		
		m_UIManager.RemoveUIElement(m_Play);
		m_Play = null;
		
		m_UIManager.RemoveUIElement(m_HighScore);
		m_HighScore = null;

        m_UIManager.RemoveUIElement(m_Credits);
        m_Credits = null;

        m_UIManager.RemoveUIElement(m_Tutorial);
        m_Tutorial = null;
	}

	@Override
	public void Update(double DeltaTime) 
	{
		ControlScheme Scheme = m_Game.GetControlScheme();
		
		if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_Play))
		{	
			m_Play.Pressed();
			Scheme.ResetTouchCoordinates();
			return;
		}
		
		if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_HighScore))
		{	
			m_HighScore.Pressed();
			Scheme.ResetTouchCoordinates();
			return;
		}

        if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_Credits))
        {
            m_Credits.Pressed();
            Scheme.ResetTouchCoordinates();
            return;
        }

        if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_Tutorial))
        {
            m_Tutorial.Pressed();
            Scheme.ResetTouchCoordinates();
            return;
        }
	}
}
