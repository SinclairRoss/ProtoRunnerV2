package com.raggamuffin.protorunnerv2.ui;

import android.content.Context;
import android.media.AudioManager;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.master.TouchPointer;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Vector2;

import junit.framework.Assert;

public class UIScreen_MainMenu extends UIScreen
{
	private UIElement_Label m_Title;
    private UIElement_Label m_TitleSubtext;

    private UIObject_Button m_Play;
    private UIObject_Button m_LeaderBoards;
    private UIObject_Button m_Achievements;
    private UIObject_Button m_Credits;

    private Subscriber m_OnPointerUpSubscriber;
	
	public UIScreen_MainMenu(GameLogic Game, UIManager Manager)
	{
		super(Game, Manager);

		m_Title	= null;
		m_Play = null;
        m_LeaderBoards = null;
        m_Achievements = null;
		m_Credits = null;
	}

	@Override
	public void Create() 
	{
        Context context = m_Game.GetContext();

        m_Title = new UIElement_Label(context.getString(R.string.app_name), UIConstants.FONTSIZE_TITLE, -0.9, 0.0, Alignment.Left, m_UIManager);
        m_UIManager.AddUIElement(m_Title);

        PubSubHub pubSub = m_Game.GetPubSubHub();
        GameAudioManager audioManager = m_Game.GetGameAudioManager();

        Publisher buttonPublisher = pubSub.CreatePublisher(PublishedTopics.StartGame);
        String buttonText = context.getString(R.string.button_play);
        m_Play = new UIObject_Button(buttonText, Colours.RunnerBlue, 0.8, 0.7, Alignment.Right, buttonPublisher, null, m_UIManager);

        buttonPublisher = pubSub.CreatePublisher(PublishedTopics.SwitchScreen);
        buttonText = context.getString(R.string.highscore_picker_screen_title);
        m_LeaderBoards = new UIObject_Button(buttonText, Colours.Crimson, 0.8, 0.4, Alignment.Right,buttonPublisher, UIScreens.Leaderboards.ordinal(), m_UIManager);

        buttonPublisher = pubSub.CreatePublisher(PublishedTopics.AchievementsPressed);
        buttonText = context.getString(R.string.button_achievements);
        m_Achievements = new UIObject_Button(buttonText, Colours.CalvinOrange, 0.8, 0.1, Alignment.Right,buttonPublisher, null, m_UIManager);

        buttonPublisher = pubSub.CreatePublisher(PublishedTopics.SwitchScreen);
        buttonText = context.getString(R.string.credits_screen_title);
        m_Credits = new UIObject_Button(buttonText, Colours.EmeraldGreen, 0.8, -0.2, Alignment.Right,buttonPublisher, UIScreens.Credits.ordinal(), m_UIManager);

        m_OnPointerUpSubscriber = new OnPointerUpSubscriber();
        pubSub.SubscribeToTopic(PublishedTopics.OnPointerUp, m_OnPointerUpSubscriber);
    }

	@Override
	public void Destroy()
	{
        m_Title = null;
        m_TitleSubtext = null;
        m_LeaderBoards = null;
        m_Achievements = null;
		m_Play = null;
        m_Credits = null;

        PubSubHub pubSub = m_Game.GetPubSubHub();
        pubSub.UnsubscribeFromTopic(PublishedTopics.OnPointerUp, m_OnPointerUpSubscriber);
	}

	@Override
	public void Update(double deltaTime)
	{
		ControlScheme scheme = m_Game.GetControlScheme();

        m_Play.OnHoverOff();
        m_LeaderBoards.OnHoverOff();
        m_Achievements.OnHoverOff();
        m_Credits.OnHoverOff();

        int activePointerCount = scheme.GetActivePointerCount();
        for (int i = 0; i < activePointerCount; ++i)
        {
            TouchPointer pointer = scheme.GetPointerAtIndex(i);
            Vector2 pointerPos = pointer.GetCurrentPosition();

            if (CollisionDetection.UIElementInteraction(pointerPos, m_Play.GetTouchArea()))
            {
                m_Play.OnHover();
            }
            if (CollisionDetection.UIElementInteraction(pointerPos, m_LeaderBoards.GetTouchArea()))
            {
                m_LeaderBoards.OnHover();
            }
            if (CollisionDetection.UIElementInteraction(pointerPos, m_Achievements.GetTouchArea()))
            {
                m_Achievements.OnHover();
            }
            if (CollisionDetection.UIElementInteraction(pointerPos, m_Credits.GetTouchArea()))
            {
             //   UIElement_TouchMarker marker = m_UIManager.GetTouchDisplay().GetMarkerWithID(pointer.GetId());
                m_Credits.OnHover();
            }
        }

        m_Play.Update(deltaTime);
        m_LeaderBoards.Update(deltaTime);
        m_Achievements.Update(deltaTime);
        m_Credits.Update(deltaTime);
	}

    private class OnPointerUpSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            TouchPointer pointer = (TouchPointer) args;
            Assert.assertNotNull(pointer);

            Vector2 pointerPos = pointer.GetCurrentPosition();

            if (CollisionDetection.UIElementInteraction(pointerPos, m_Play.GetTouchArea()))
            {
                m_Play.OnPress();
                return;
            }
            if (CollisionDetection.UIElementInteraction(pointerPos, m_LeaderBoards.GetTouchArea()))
            {
                m_LeaderBoards.OnPress();
                return;
            }
            if (CollisionDetection.UIElementInteraction(pointerPos, m_Achievements.GetTouchArea()))
            {
                m_Achievements.OnPress();
                return;
            }
            if (CollisionDetection.UIElementInteraction(pointerPos, m_Credits.GetTouchArea()))
            {
                m_Credits.OnPress();
                return;
            }
        }
    }
}
