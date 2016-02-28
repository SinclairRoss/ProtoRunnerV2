package com.raggamuffin.protorunnerv2.ui;

import android.content.Context;
import android.util.Log;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gamelogic.GameStats;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;

public class AftermathScreen extends UIScreen
{
	private UILabel m_Title;
	private UIButton m_Back;

    private UILabel m_NewBestScoreLabel;
	private UILabel m_ScoreLabel;
    private UILabel m_NewBestPlayTimeLabel;
	private UILabel m_PlayTimeLabel;
	private UILabel m_TimesRebootedLabel;
	private UIProgressBar m_WingmanALifeBar;
	private UIProgressBar m_WingmanBLifeBar;
	
	public AftermathScreen(GameLogic Game, UIManager Manager) 
	{
		super(Game, Manager);

		m_Title = null;
		m_Back = null;

        m_NewBestScoreLabel = null;
		m_ScoreLabel = null;
        m_NewBestPlayTimeLabel = null;
		m_PlayTimeLabel = null;
		m_TimesRebootedLabel = null;
		m_WingmanALifeBar = null;
		m_WingmanBLifeBar = null;
    }
	
	@Override
	public void Create()
	{
		super.Create();

		Context context = m_Game.GetContext();
		GameStats stats = m_Game.GetGameStats();

		m_Title = CreateTitle(context.getString(R.string.aftermath_screen_title));

        m_Back = CreateNextButton(UIScreens.MainMenu);

		m_ScoreLabel = CreateLabel(context.getString(R.string.label_score) + stats.GetScore());
        m_NewBestScoreLabel = CreateLabelSubtext(m_ScoreLabel, context.getString(R.string.label_new_personal_Best));

        Log.e("yeah", "highScore " + m_Game.GetDatabaseManager().GetLocallySavedHighScore());
        Log.e("yeah", "time " + m_Game.GetDatabaseManager().GetLocallySavedHighestPlayTime());

        boolean hideNewBestScoreLabel = (stats.GetScore() <= m_Game.GetDatabaseManager().GetLocallySavedHighScore());
        m_NewBestScoreLabel.SetHidden(hideNewBestScoreLabel);

		m_PlayTimeLabel = CreateLabel(context.getString(R.string.label_play_time) + stats.GetPlayTimeString());
        m_NewBestPlayTimeLabel = CreateLabelSubtext(m_PlayTimeLabel, context.getString(R.string.label_new_personal_Best));

        boolean hideNewBestTimeLabel = (stats.GetPlayTimeMillis() <= m_Game.GetDatabaseManager().GetLocallySavedHighestPlayTime());
        m_NewBestPlayTimeLabel.SetHidden(hideNewBestTimeLabel);

        m_TimesRebootedLabel = CreateLabel(context.getString(R.string.label_num_reboots) + Integer.toString(stats.GetLivesUsed()));

		m_WingmanALifeBar = CreateProgressBar(context.getString(R.string.label_wingman_a_life), stats.GetPlayTime());
		m_WingmanALifeBar.SetValue(stats.GetWingmanADuration());

		m_WingmanBLifeBar = CreateProgressBar(context.getString(R.string.label_wingman_b_life), stats.GetPlayTime());
		m_WingmanBLifeBar.SetValue(stats.GetWingmanBDuration());

        m_Game.SaveScores();
	}
	
	@Override
	public void Remove() 
	{
        super.Remove();

        m_UIManager.RemoveUIElement(m_Title);
        m_Title = null;

        m_UIManager.RemoveUIElement(m_Back);
        m_Back = null;
		
		m_UIManager.RemoveUIElement(m_ScoreLabel);
		m_ScoreLabel = null;

        m_UIManager.RemoveUIElement(m_NewBestScoreLabel);
        m_NewBestScoreLabel = null;

		m_UIManager.RemoveUIElement(m_PlayTimeLabel);
		m_PlayTimeLabel = null;

        m_UIManager.RemoveUIElement(m_NewBestPlayTimeLabel);
        m_NewBestPlayTimeLabel = null;

		m_UIManager.RemoveUIElement(m_TimesRebootedLabel);
		m_TimesRebootedLabel = null;

		m_UIManager.RemoveUIElement(m_WingmanALifeBar);
		m_WingmanALifeBar = null;

		m_UIManager.RemoveUIElement(m_WingmanBLifeBar);
        m_WingmanBLifeBar = null;
	}

	@Override
	public void Update(double deltaTime)
	{
        super.Update(deltaTime);

		ControlScheme scheme = m_Game.GetControlScheme();
		
		if(CollisionDetection.UIElementInteraction(scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_Back))
		{
			m_Back.Pressed();
            scheme.ResetTouchCoordinates();
			return;
		}
	}
}