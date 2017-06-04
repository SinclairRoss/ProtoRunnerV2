package com.raggamuffin.protorunnerv2.ui;

import android.content.Context;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.audio.AudioClips;
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
    public UIScreen_NotSignedIn(GameLogic Game, UIManager uiManager)
    {
        super(Game, uiManager);
    }

    @Override
    public void Create()
    {
      //  Context context = m_Game.GetContext();
      //  ColourManager cManager = m_Game.GetColourManager();
      //  GameAudioManager audio = m_Game.GetGameAudioManager();
//
      //  // Yes button
      //  Publisher yesPub = m_Game.GetPubSubHub().CreatePublisher(PublishedTopics.SwitchScreen);
      //  m_YesButton = new UIElement_Button(m_UIManager, yesPub, UIScreens.MainMenu.ordinal(), audio, AudioClips.UI_Positive);
      //  m_YesButton.SetText(context.getString(R.string.tutorial_yes));
      //  m_YesButton.GetFont().SetColour(cManager.GetSecondaryColour());
      //  m_YesButton.SetPosition(0.50, -0.30);
      //  m_YesButton.GetFont().SetAlignment(Alignment.Right);

      //  m_UIManager.AddUIElement(m_YesButton);

        // No button
      //  Publisher noPub = m_Game.GetPubSubHub().CreatePublisher(PublishedTopics.SwitchScreen);
      //  m_NoButton = new UIElement_Button(m_UIManager, noPub, UIScreens.MainMenu.ordinal(), audio, AudioClips.UI_Negative);
      //  m_NoButton.SetText(context.getString(R.string.tutorial_no));
      //  m_NoButton.GetFont().SetColour(cManager.GetPrimaryColour());
      //  m_NoButton.SetPosition(-0.50, -0.30);
     //   m_NoButton.GetFont().SetAlignment(Alignment.Left);

     //   m_UIManager.AddUIElement(m_NoButton);
    }

    @Override
    public void Update(double deltaTime)
    {}

    @Override
    public void Destroy()
    {}
}
