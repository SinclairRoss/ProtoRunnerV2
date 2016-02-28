package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;

public class CreditsScreen extends UIScreen
{
	private UIElement m_Title;
	private UIButton m_Back;
	
	private UILabel m_Line1;
	private UILabel m_Line2;
	private UILabel m_Break;
	private UILabel m_Line3;
	private UILabel m_Line4;

	public CreditsScreen(GameLogic game, UIManager uiManager)
	{
		super(game, uiManager);

		m_Title = null;
		m_Back  = null;
		m_Line1 = null;
        m_Line2 = null;
        m_Break = null;
        m_Line3 = null;
        m_Line4 = null;
	}
	
	@Override
	public void Create()
	{
		super.Create();
		
		m_Title = CreateTitle(m_Game.GetContext().getString(R.string.credits_screen_title));
		m_Back  = CreateBackButton(UIScreens.MainMenu);
		
		m_Line1 = CreateLabel(m_Game.GetContext().getString(R.string.credits_text1));
		m_Line2 = CreateLabel(m_Game.GetContext().getString(R.string.credits_text2));
		m_Break = CreateLabel(m_Game.GetContext().getString(R.string.empty));
		m_Line3 = CreateLabel(m_Game.GetContext().getString(R.string.credits_text3));
		m_Line4 = CreateLabel(m_Game.GetContext().getString(R.string.credits_text4));
	}
	
	@Override
	public void Remove()
	{
        super.Remove();

		m_UIManager.RemoveUIElement(m_Title);
		m_Title = null;	
		
		m_UIManager.RemoveUIElement(m_Back);
		m_Back = null;
		
		m_UIManager.RemoveUIElement(m_Line1);
		m_Line1 = null;
		
		m_UIManager.RemoveUIElement(m_Line2);
		m_Line2 = null;
		
		m_UIManager.RemoveUIElement(m_Break);
		m_Break = null;
		
		m_UIManager.RemoveUIElement(m_Line3);
		m_Line3 = null;
		
		m_UIManager.RemoveUIElement(m_Line4);
		m_Line4 = null;
	}

	@Override
	public void Update(double DeltaTime) 
	{
		ControlScheme Scheme = m_Game.GetControlScheme();
		
		if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_Back))
		{
			m_Back.Pressed();
			Scheme.ResetTouchCoordinates();
			return;
		}
	}
}