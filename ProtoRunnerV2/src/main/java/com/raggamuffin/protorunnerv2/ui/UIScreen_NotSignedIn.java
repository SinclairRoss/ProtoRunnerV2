package com.raggamuffin.protorunnerv2.ui;

import android.content.Context;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.ColourManager;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;

public class UIScreen_NotSignedIn extends UIScreen
{
    private UIButton m_YesButton;
    private UIButton m_NoButton;

    public UIScreen_NotSignedIn(GameLogic Game, UIManager uiManager)
    {
        super(Game, uiManager);

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

        // Yes button
        Publisher yesPub = m_Game.GetPubSubHub().CreatePublisher(PublishedTopics.SwitchScreen);
        m_YesButton = new UIButton(yesPub, audio, m_UIManager, UIScreens.MainMenu.ordinal());
        m_YesButton.SetText(context.getString(R.string.tutorial_yes));
        m_YesButton.GetFont().SetColour(cManager.GetAccentingColour());
        m_YesButton.SetPosition(0.50, -0.30);
        m_YesButton.GetFont().SetAlignment(Font.Alignment.Right);

        m_UIManager.AddUIElement(m_YesButton);

        // No button
        Publisher noPub = m_Game.GetPubSubHub().CreatePublisher(PublishedTopics.SwitchScreen);
        m_NoButton = new UIButton(noPub, audio, m_UIManager, UIScreens.MainMenu.ordinal());
        m_NoButton.SetText(context.getString(R.string.tutorial_no));
        m_NoButton.GetFont().SetColour(cManager.GetPrimaryColour());
        m_NoButton.SetPosition(-0.50, -0.30);
        m_NoButton.GetFont().SetAlignment(Font.Alignment.Left);

        m_UIManager.AddUIElement(m_NoButton);
        m_MessageHandler.DisplayMessage(context.getString(R.string.not_signed_in), MessageOrientation.Top, 0.8, 1, -1, 0.0);
    }

    @Override
    public void Update(double deltaTime)
    {
        ControlScheme Scheme = m_Game.GetControlScheme();

        if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(),m_UIManager.GetScreenSize(),m_YesButton))
        {
            m_Game.GetGooglePlayService().Connect();
            m_Game.GetGooglePlayService().DisplayLeaderBoard();

            m_YesButton.Pressed();
            Scheme.ResetTouchCoordinates();
            return;
        }

        if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(),m_UIManager.GetScreenSize(),m_NoButton))
        {
            m_NoButton.Pressed();
            Scheme.ResetTouchCoordinates();
            return;
        }
    }

    @Override
    public void Remove()
    {
        super.Remove();

        m_UIManager.RemoveUIElement(m_YesButton);
        m_YesButton = null;

        m_UIManager.RemoveUIElement(m_NoButton);
        m_NoButton = null;
    }
}
