package com.raggamuffin.protorunnerv2.ui;

import android.content.Context;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;

public class UIScreen_Credits extends UIScreen
{
	private UIElement m_Title;
	private UIButton m_Back;
	
	private UILabel m_Line1;
	private UILabel m_Line2;
    private UILabel m_Line2Subtext;
	private UILabel m_Break;
	private UILabel m_Line3;
	private UILabel m_Line4;
    private UILabel m_Line4Subtext;
	private UILabel m_Line5;
    private UILabel m_Line5Subtext;
	private UILabel m_Line6;
    private UILabel m_Line6Subtext;

	public UIScreen_Credits(GameLogic game, UIManager uiManager)
	{
		super(game, uiManager);

		m_Title = null;
		m_Back  = null;
		m_Line1 = null;
        m_Line2 = null;
        m_Line2Subtext = null;
        m_Break = null;
        m_Line3 = null;
        m_Line4 = null;
        m_Line4Subtext = null;
		m_Line5 = null;
        m_Line5Subtext = null;
        m_Line6 = null;
        m_Line6Subtext = null;
	}
	
	@Override
	public void Create()
	{
		super.Create();
		
		m_Title = CreateTitle(m_Game.GetContext().getString(R.string.credits_screen_title));
		m_Back  = CreateBackButton(UIScreens.MainMenu);

        Context context = m_Game.GetContext();
		
		m_Line1 = CreateLabel(context.getString(R.string.credits_text1));
		m_Line2 = CreateLabel(context.getString(R.string.credits_text2));
        m_Line2.SetColour(m_Game.GetColourManager().GetPrimaryColour());
        m_Line2Subtext = CreateLabelSubtext(m_Line2, context.getString(R.string.credits_text2_sub));
		m_Break = CreateLabel(context.getString(R.string.empty));
		m_Line3 = CreateLabel(context.getString(R.string.credits_text3));
		m_Line4 = CreateLabel(context.getString(R.string.credits_text4));
        m_Line4.SetColour(m_Game.GetColourManager().GetPrimaryColour());
        m_Line4Subtext  = CreateLabelSubtext(m_Line4, context.getString(R.string.credits_text4_sub));
		m_Line5 = CreateLabel(context.getString(R.string.credits_text5));
        m_Line5.SetColour(m_Game.GetColourManager().GetPrimaryColour());
        m_Line5Subtext  = CreateLabelSubtext(m_Line5, context.getString(R.string.credits_text5_sub));
        m_Line6 = CreateLabel(context.getString(R.string.credits_text6));
        m_Line6.SetColour(m_Game.GetColourManager().GetPrimaryColour());
        m_Line6Subtext  = CreateLabelSubtext(m_Line6, context.getString(R.string.credits_text6_sub));
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

        m_UIManager.RemoveUIElement(m_Line2Subtext);
        m_Line2Subtext = null;
		
		m_UIManager.RemoveUIElement(m_Break);
		m_Break = null;
		
		m_UIManager.RemoveUIElement(m_Line3);
		m_Line3 = null;

        m_UIManager.RemoveUIElement(m_Line4);
        m_Line4 = null;

        m_UIManager.RemoveUIElement(m_Line4Subtext);
        m_Line4Subtext = null;

        m_UIManager.RemoveUIElement(m_Line5);
        m_Line5 = null;

        m_UIManager.RemoveUIElement(m_Line5Subtext);
        m_Line5Subtext = null;

        m_UIManager.RemoveUIElement(m_Line6);
        m_Line6 = null;

        m_UIManager.RemoveUIElement(m_Line6Subtext);
        m_Line6Subtext = null;
	}

	@Override
	public void Update(double DeltaTime) 
	{
		ControlScheme Scheme = m_Game.GetControlScheme();
		
		if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_Back))
		{
			m_Back.Pressed();
			Scheme.ResetTouchCoordinates();
		}
	}
}