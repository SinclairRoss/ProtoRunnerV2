package com.raggamuffin.protorunnerv2.ui;

import android.content.Context;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;

public class UIScreen_HighScore extends UIScreen
{
    private UIElement m_Title;
    private UIButton m_Back;

    private UIButton m_HighScoreButton;
    private UIButton m_HighTimeButton;

    public UIScreen_HighScore(GameLogic game, UIManager uiManager)
    {
        super(game, uiManager);

        m_Title = null;
        m_Back = null;

        m_HighScoreButton = null;
        m_HighTimeButton = null;
    }

    @Override
    public void Create()
    {
        super.Create();

        Context context = m_Game.GetContext();

        m_Title = CreateTitle(context.getString(R.string.highscore_picker_screen_title));
        m_Back = CreateBackButton(UIScreens.MainMenu);

        m_HighScoreButton = CreateButton(context.getString(R.string.highscore_points_button), PublishedTopics.HighScorePressed, AudioClips.UI_Positive);
        m_HighTimeButton = CreateButton(context.getString(R.string.highscore_time_button), PublishedTopics.HighTimePressed, AudioClips.UI_Positive);
    }

    @Override
    public void Remove()
    {
        super.Remove();

        m_UIManager.RemoveUIElement(m_Title);
        m_Title = null;

        m_UIManager.RemoveUIElement(m_Back);
        m_Back = null;

        m_UIManager.RemoveUIElement(m_HighScoreButton);
        m_HighScoreButton = null;

        m_UIManager.RemoveUIElement(m_HighTimeButton);
        m_HighTimeButton = null;
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

        if(CollisionDetection.UIElementInteraction(scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_HighScoreButton))
        {
            m_HighScoreButton.Pressed();
            scheme.ResetTouchCoordinates();
            return;
        }

        if(CollisionDetection.UIElementInteraction(scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_HighTimeButton))
        {
            m_HighTimeButton.Pressed();
            scheme.ResetTouchCoordinates();
            return;
        }
    }
}
