package com.raggamuffin.protorunnerv2.ui;

import android.content.Context;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gamelogic.GameStats;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;

public class UIScreen_Aftermath extends UIScreen
{
	private UIElement_Label m_Title;

    private UIElement_Label m_NewBestScoreLabel;
	private UIElement_Label m_ScoreLabel;
    private UIElement_Label m_NewBestPlayTimeLabel;
	private UIElement_Label m_PlayTimeLabel;
	private UIElement_Label m_TimesRebootedLabel;
	
	public UIScreen_Aftermath(GameLogic Game, UIManager Manager)
	{
		super(Game, Manager);

		m_Title = null;

        m_NewBestScoreLabel = null;
		m_ScoreLabel = null;
        m_NewBestPlayTimeLabel = null;
		m_PlayTimeLabel = null;
		m_TimesRebootedLabel = null;
    }
	
	@Override
	public void Create()
	{
		Context context = m_Game.GetContext();
		GameStats stats = m_Game.GetGameStats();

		//m_Title = CreateTitle(context.getString(R.string.aftermath_screen_title));

        //m_Back = CreateNextButton(UIScreens.MainMenu);
//
		//m_ScoreLabel = CreateLabel(context.getString(R.string.label_score) + stats.GetScore());
        //m_NewBestScoreLabel = CreateLabelSubtext(m_ScoreLabel, context.getString(R.string.label_new_personal_Best));
//
        //boolean hideNewBestScoreLabel = (stats.GetScore() <= m_Game.GetDatabaseManager().GetLocallySavedHighScore());
        //m_NewBestScoreLabel.SetHidden(hideNewBestScoreLabel);
//
		//m_PlayTimeLabel = CreateLabel(context.getString(R.string.label_play_time) + stats.GetPlayTimeString());
        //m_NewBestPlayTimeLabel = CreateLabelSubtext(m_PlayTimeLabel, context.getString(R.string.label_new_personal_Best));

        boolean hideNewBestTimeLabel = (stats.GetPlayTimeMillis() <= m_Game.GetDatabaseManager().GetLocallySavedHighestPlayTime());
       //m_NewBestPlayTimeLabel.SetHidden(hideNewBestTimeLabel);

       //m_TimesRebootedLabel = CreateLabel(context.getString(R.string.label_num_reboots) + Integer.toString(stats.GetLivesUsed()));

	//m_WingmanALifeBar = CreateProgressBar(context.getString(R.string.label_wingman_a_life), stats.GetPlayTime());
	//m_WingmanALifeBar.SetValue(stats.GetWingmanADuration());

	//m_WingmanBLifeBar = CreateProgressBar(context.getString(R.string.label_wingman_b_life), stats.GetPlayTime());
	//m_WingmanBLifeBar.SetValue(stats.GetWingmanBDuration());

        m_Game.SaveScores();
	}
	
	@Override
	public void Destroy()
	{
        m_Title = null;
		m_ScoreLabel = null;
        m_NewBestScoreLabel = null;
		m_PlayTimeLabel = null;
        m_NewBestPlayTimeLabel = null;
		m_TimesRebootedLabel = null;
	}

	@Override
	public void Update(double deltaTime)
	{}
}