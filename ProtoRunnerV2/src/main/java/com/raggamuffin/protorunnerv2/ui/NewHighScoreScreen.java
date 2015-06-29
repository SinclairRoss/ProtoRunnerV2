package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.data.HighScoreRow;
import com.raggamuffin.protorunnerv2.data.TableType;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gamelogic.GameStats;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;

public class NewHighScoreScreen extends UIScreen
{
	private final double DELAY = 3.0;
	
	private UILabel m_Title;
	private UILabel m_Score;
	private UIButton m_NextButton;
	
	private CharPicker m_CharPickerA;
	private CharPicker m_CharPickerB;
	private CharPicker m_CharPickerC;

	public NewHighScoreScreen(GameLogic Game, UIManager Manager) 
	{
		super(Game, Manager);

		m_Title = null;
		m_Score = null;
		m_NextButton = null;
		
		m_CharPickerA = new CharPicker(-0.4, 0,  m_Game.GetGameAudioManager(), Manager, m_Game.GetControlScheme());
		m_CharPickerB = new CharPicker( 0,   0,  m_Game.GetGameAudioManager(), Manager, m_Game.GetControlScheme());
		m_CharPickerC = new CharPicker( 0.4, 0,  m_Game.GetGameAudioManager(), Manager, m_Game.GetControlScheme());
	}
	
	@Override
	public void Create()
	{
        super.Create();

		m_Score = new UILabel(m_Game.GetGameAudioManager(), m_UIManager);
		m_Score.SetText(m_Game.GetContext().getString(R.string.label_enter_your_name));
		m_Score.SetPosition(0.0, 0.7);
		m_Score.GetFont().SetAlignment(Font.Alignment.Center);
		m_UIManager.AddUIElement(m_Score);
		m_Score.Hide();
		m_Score.Show(DELAY);
		
		
		m_Title = new UILabel(m_Game.GetGameAudioManager(), m_UIManager);
		m_Title.SetText(m_Game.GetContext().getString(R.string.label_score) + m_Game.GetGameStats().GetScore());
		m_Title.SetPosition(0.0, -0.7);
		m_Title.GetFont().SetAlignment(Font.Alignment.Center);
		m_UIManager.AddUIElement(m_Title);
		m_Title.Hide();
		m_Title.Show(DELAY);
		
		m_CharPickerA.Create(DELAY);
		m_CharPickerB.Create(DELAY + 0.125);
		m_CharPickerC.Create(DELAY + 0.25);
		
		m_MessageHandler.Activate();
		m_MessageHandler.DisplayMessage(m_Game.GetContext().getString(R.string.label_new_high_score), MessageOrientation.Center, 1, 1, 3.0, 0.5);
		
		m_NextButton = CreateNextButton(UIScreens.HighScore);
		m_NextButton.Hide();
		m_NextButton.Show(DELAY);
	}

	@Override
	public void Remove() 
	{
        super.Remove();

		m_UIManager.RemoveUIElement(m_Title);
		m_Title = null;
		
		m_UIManager.RemoveUIElement(m_Score);
		m_Score = null;
		
		m_UIManager.RemoveUIElement(m_NextButton);
		m_NextButton = null;

		m_CharPickerA.Remove();
		m_CharPickerB.Remove();
		m_CharPickerC.Remove();
		
		m_MessageHandler.Clear();
		m_MessageHandler.Deactivate();
	}

	@Override
	public void Update(double deltaTime) 
	{
		m_MessageHandler.Update(deltaTime);
		
		m_CharPickerA.Update(deltaTime);
		m_CharPickerB.Update(deltaTime);
		m_CharPickerC.Update(deltaTime);
		
		ControlScheme scheme = m_Game.GetControlScheme();
		
		if(CollisionDetection.UIElementInteraction(scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_NextButton))
		{
			GameStats stats = m_Game.GetGameStats();
			
			String name = m_CharPickerA.GetChar() + m_CharPickerB.GetChar() + m_CharPickerC.GetChar();
			
			m_Game.GetDatabaseManager().AddToTable(TableType.Highscore, new HighScoreRow(0, name, stats.GetScore(), (float)stats.GetPlayTime(), stats.GetLivesUsed(), stats.GetShotsFired(), stats.GetShotsLanded(), (float)stats.GetWingmanADuration(), (float)stats.GetWingmanBDuration()));
			scheme.ResetTouchCoordinates();
			m_NextButton.Pressed();
			return;
		}
	}

}
