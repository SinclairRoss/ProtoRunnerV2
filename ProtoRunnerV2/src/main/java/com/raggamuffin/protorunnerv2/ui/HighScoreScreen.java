package com.raggamuffin.protorunnerv2.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.util.Log;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.data.HighScoreRow;
import com.raggamuffin.protorunnerv2.data.HighScoreTable;
import com.raggamuffin.protorunnerv2.data.TableRow;
import com.raggamuffin.protorunnerv2.data.TableType;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class HighScoreScreen extends UIScreen
{
	private final int ELEMENTS_PER_PAGE = 6;
	private final int FINAL_PAGE = (HighScoreTable.MAX_HIGH_SCORES / ELEMENTS_PER_PAGE) - 1;
	private int m_Page;

	private UIElement m_Title;
	private UIButton m_Back;
	
	private ArrayList<HighScoreRow> m_HighScores;
	private ArrayList<UIButton> m_ScoreButtons;
	private ArrayList<UILabel> m_Filler;
	private int m_NewestScoreID;
	
	public HighScoreScreen(GameLogic Game, UIManager Manager) 
	{
		super(Game, Manager);

		m_Page = 0;
		
		m_Title = null;
		m_Back = null;
		
		m_ScoreButtons = null;
	}
	
	@Override
	public void Create()
	{
		super.Create();
		
		m_Title = CreateTitle(m_Game.GetContext().getString(R.string.button_scores));
		m_Back  = CreateBackButton(UIScreens.MainMenu);
		
		m_HighScores  = GetHighScores();
		m_ScoreButtons = new ArrayList<UIButton>();
		m_Filler = new ArrayList<UILabel>();
		m_NewestScoreID = GetNewestScoreID();
		
		m_Page = 0;
		RefreshPage(m_Page);
	}

	private ArrayList<HighScoreRow> GetHighScores()
	{
		ArrayList<HighScoreRow> highScores = new ArrayList<HighScoreRow>();
		ArrayList<TableRow> rows = m_Game.GetDatabaseManager().GetTableData(TableType.Highscore);
		
		for(TableRow row : rows)
		{
			highScores.add((HighScoreRow) row);
		}
		
		return highScores;
	}
	
	private int GetNewestScoreID()
	{
		int highestId = Integer.MIN_VALUE;
		
		for(HighScoreRow row : m_HighScores)
		{
			int id = row.GetId();
			
			if(id > highestId)
			{
				highestId = id;
			}
		}
		
		return highestId;
	}
	
	@Override
	public void Remove() 
	{
        super.Remove();

		m_UIManager.RemoveUIElement(m_Title);
		m_Title = null;	
		
		m_UIManager.RemoveUIElement(m_Back);
		m_Back = null;
		
		ClearScores();
		m_ScoreButtons = null;
	}
	
	private void ClearScores()
	{
		for(UIButton button : m_ScoreButtons)
		{
			m_UIManager.RemoveUIElement(button);
		}
		
		m_ScoreButtons.clear();
		
		for(UILabel label : m_Filler)
		{
			m_UIManager.RemoveUIElement(label);
		}
		
		m_Filler.clear();
	}
	
	private void RefreshPage(int page)
	{
		ClearScores();
		ResetNumElements();

		int start = page  * ELEMENTS_PER_PAGE;
		int end   = start + ELEMENTS_PER_PAGE - 1;
		
		int place = start;

		for(HighScoreRow score : m_HighScores)
		{
			int index = m_HighScores.indexOf(score);
			if(index < start || index > end)
				continue;
			
			place ++;
			String labelText = "" + place + ": " + score.GetName() + " " + String.format("%08d", score.GetScore());
			UIButton button = CreateButton(labelText, UIScreens.Aftermath);

			if(score.GetId() == m_NewestScoreID)
			{
				button.GetFont().SetColour(Colours.EmeraldGreen);
			}
			
			m_ScoreButtons.add(button);
		}
		
		int EmptyLabels = ELEMENTS_PER_PAGE - m_ScoreButtons.size();

		for(int i = 0; i < EmptyLabels; i++)
		{
			place++;
			
			String labelText = "" + place + ": --- " + String.format("%08d", 0);
			UILabel label = CreateLabel(labelText);
			
			m_Filler.add(label);
		}
	}

	@Override
	public void Update(double DeltaTime) 
	{
		ControlScheme Scheme = m_Game.GetControlScheme();
		
		for(Iterator<UIButton> Iter = m_ScoreButtons.iterator(); Iter.hasNext();)
		{
			UIButton button = Iter.next();
			
			if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), button))
			{
				int index = m_ScoreButtons.indexOf(button);
				m_Game.GetGameStats().SetStatsFromScoreTable(m_HighScores.get(index + (m_Page * ELEMENTS_PER_PAGE)));
				
				button.Pressed();
				Scheme.ResetTouchCoordinates();
				return;
			}
		}
		
		if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_Back))
		{
			m_Page --;

			if(m_Page < 0)		
				m_Back.Pressed();			
			else
				RefreshPage(m_Page);
			
			
			Scheme.ResetTouchCoordinates();
			return;
		}
	}
}
