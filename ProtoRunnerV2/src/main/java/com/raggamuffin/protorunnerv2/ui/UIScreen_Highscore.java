package com.raggamuffin.protorunnerv2.ui;

import android.content.Context;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;

public class UIScreen_HighScore extends UIScreen
{
    private UIElement m_Title;

    public UIScreen_HighScore(GameLogic game, UIManager uiManager)
    {
        super(game, uiManager);

        m_Title = null;
    }

    @Override
    public void Create()
    {
        Context context = m_Game.GetContext();

      //  m_Title = CreateTitle(context.getString(R.string.highscore_picker_screen_title));
      //  m_Back = CreateBackButton(UIScreens.MainMenu);
//
      //  m_HighScoreButton = CreateButton(context.getString(R.string.highscore_points_button), PublishedTopics.HighScorePressed, AudioClips.UI_Positive);
      //  m_HighTimeButton = CreateButton(context.getString(R.string.highscore_time_button), PublishedTopics.HighTimePressed, AudioClips.UI_Positive);
    }

    @Override
    public void Destroy()
    {
        m_Title = null;
    }

    @Override
    public void Update(double deltaTime)
    { }
}
