package com.raggamuffin.protorunnerv2.ui;

import android.content.Context;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.ColourManager;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;

import java.util.ArrayList;

public class NewToGameScreen extends UIScreen
{
    private int m_Index;
    private UIButton m_Continue;
    private UIButton m_YesButton;
    private UIButton m_NoButton;

    private ArrayList<String> m_Strings;

    public NewToGameScreen(GameLogic Game, UIManager uiManager)
    {
        super(Game, uiManager);

        m_Strings = null;

        m_Continue = null;
        m_YesButton = null;
        m_NoButton = null;
    }

    @Override
    public void Create()
    {
        super.Create();

        Context context = m_Game.GetContext();
        ColourManager cManager = m_Game.GetColourManager();
        GameAudioManager audio = m_Game.GetGameAudioManager();

        m_Strings = new ArrayList<String>();

        m_Strings.add(context.getString(R.string.new_player_1));
        m_Strings.add(context.getString(R.string.new_player_2));
        m_Strings.add(context.getString(R.string.new_player_3));
        m_Strings.add(context.getString(R.string.new_player_4));

        // Next button
        m_Continue = new UIButton(audio, m_UIManager);
        m_Continue.SetText(context.getString(R.string.tutorial_next));
        m_Continue.GetFont().SetColour(cManager.GetAccentingColour());
        m_Continue.SetPosition(0.80, -0.70);
        m_Continue.GetFont().SetAlignment(Font.Alignment.Right);

        m_UIManager.AddUIElement(m_Continue);

        // Yes button
        Publisher yesPub = m_Game.GetPubSubHub().CreatePublisher(PublishedTopics.StartTutorial);
        m_YesButton = new UIButton(yesPub, audio, m_UIManager);
        m_YesButton.SetText(context.getString(R.string.tutorial_yes));
        m_YesButton.GetFont().SetColour(cManager.GetAccentingColour());
        m_YesButton.SetPosition(0.50, -0.30);
        m_YesButton.GetFont().SetAlignment(Font.Alignment.Right);

        m_UIManager.AddUIElement(m_YesButton);
        m_YesButton.SetHidden(true);

        // No button
        Publisher noPub = m_Game.GetPubSubHub().CreatePublisher(PublishedTopics.StartGame);
        m_NoButton = new UIButton(noPub, audio, m_UIManager);
        m_NoButton.SetText(context.getString(R.string.tutorial_no));
        m_NoButton.GetFont().SetColour(cManager.GetPrimaryColour());
        m_NoButton.SetPosition(-0.50, -0.30);
        m_NoButton.GetFont().SetAlignment(Font.Alignment.Left);

        m_UIManager.AddUIElement(m_NoButton);
        m_NoButton.SetHidden(true);

        m_Index = 0;
        ShowMessage(m_Index);
    }

    @Override
    public void Remove()
    {
        super.Remove();

        m_Strings.clear();
        m_Strings = null;

        m_UIManager.RemoveUIElement(m_Continue);
        m_Continue = null;

        m_UIManager.RemoveUIElement(m_YesButton);
        m_YesButton = null;

        m_UIManager.RemoveUIElement(m_NoButton);
        m_NoButton = null;
    }

    @Override
    public void Update(double deltaTime)
    {
        super.Update(deltaTime);

        ControlScheme Scheme = m_Game.GetControlScheme();

        if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_Continue))
        {
            m_Index ++;

            if(m_Index < m_Strings.size() - 1)
            {
                ShowMessage(m_Index);
            }
            else
            {
                ShowFinalPage();
            }

            m_Continue.Pressed();
            Scheme.ResetTouchCoordinates();
            return;
        }

        if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_YesButton))
        {
            m_YesButton.Pressed();
            Scheme.ResetTouchCoordinates();
            return;
        }

        if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_NoButton))
        {
            m_Game.GetDatabaseManager().TutorialOffered();

            m_NoButton.Pressed();
            Scheme.ResetTouchCoordinates();
            return;
        }
    }

    private void ShowMessage(int index)
    {
        m_MessageHandler.DisplayMessage(m_Strings.get(index), MessageOrientation.Top, 0.9, 1, -1.0, 0.0);
    }

    private void ShowFinalPage()
    {
        ShowMessage(m_Index);
        m_Continue.Hide();
        m_YesButton.Show();
        m_NoButton.Show();

    }
}