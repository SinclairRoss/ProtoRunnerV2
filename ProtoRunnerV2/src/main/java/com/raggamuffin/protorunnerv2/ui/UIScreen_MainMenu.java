package com.raggamuffin.protorunnerv2.ui;

import android.content.Context;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;

public class UIScreen_MainMenu extends UIScreen
{
	private UILabel m_Title;
    private UILabel m_TitleSubtext;
	private UIButton m_Play;
    private UIButton m_LeaderBoards;
    private UIButton m_Achievements;
    private UIButton m_Tutorial;
    private UIButton m_Credits;

    private UIButton m_SignIn;
    private UIButton m_SignOut;
	
	public UIScreen_MainMenu(GameLogic Game, UIManager Manager)
	{
		super(Game, Manager);

		m_Title 	= null;
		m_Play 		= null;
        m_LeaderBoards = null;
        m_Achievements = null;
		m_Credits   = null;
        m_Tutorial  = null;

        m_SignIn = null;
        m_SignOut = null;
	}

	@Override
	public void Create() 
	{	
		super.Create();

        Context context = m_Game.GetContext();

        m_Title         = CreateTitle(context.getString(R.string.app_name));
        m_TitleSubtext  = CreateLabelSubtext(m_Title, context.getString(R.string.title_subtext));
		m_Play 		    = CreateButton(context.getString(R.string.button_play), PublishedTopics.StartGame, AudioClips.UI_Play);
        m_LeaderBoards  = CreateButton(context.getString(R.string.highscore_picker_screen_title), UIScreens.Leaderboards, AudioClips.UI_Positive);
        m_Achievements  = CreateButton(context.getString(R.string.button_achievements), PublishedTopics.AchievementsPressed, AudioClips.UI_Positive);
        m_Tutorial      = CreateButton(context.getString(R.string.button_tutorial), PublishedTopics.StartTutorial, AudioClips.UI_Play);
        m_Credits	    = CreateButton(context.getString(R.string.credits_screen_title), UIScreens.Credits, AudioClips.UI_Positive);

        PubSubHub pubSub = m_Game.GetPubSubHub();

        pubSub.SubscribeToTopic(PublishedTopics.GooglePlayConnected, new OnConnectSubscriber());
        pubSub.SubscribeToTopic(PublishedTopics.GooglePlayDisconnected, new OnDisconnectSubscriber());

        m_SignIn = new UIButton(m_UIManager, null, 0, m_Game.GetGameAudioManager(), AudioClips.UI_Positive);
        m_SignIn.SetText(context.getString(R.string.sign_in));
        m_SignIn.SetPosition(0.9, -0.8);
        m_SignIn.GetFont().SetAlignment(Font.Alignment.Right);
        m_SignIn.GetFont().SetColour(m_Game.GetColourManager().GetSecondaryColour());

        m_UIManager.AddUIElement(m_SignIn);

        m_SignOut = new UIButton(m_UIManager, null, 0, m_Game.GetGameAudioManager(), AudioClips.UI_Negative);
        m_SignOut.SetText(context.getString(R.string.sign_out));
        m_SignOut.SetPosition(0.9, -0.8);
        m_SignOut.GetFont().SetAlignment(Font.Alignment.Right);
        m_SignOut.GetFont().SetColour(m_Game.GetColourManager().GetPrimaryColour());

        m_UIManager.AddUIElement(m_SignOut);

        if(m_Game.GetGooglePlayService().IsConnected())
            ShowSignOut();
        else
            ShowSignIn();
    }

	@Override
	public void Remove() 
	{
        super.Remove();

        m_UIManager.RemoveUIElement(m_Title);
        m_Title = null;

        m_UIManager.RemoveUIElement(m_TitleSubtext);
        m_TitleSubtext = null;

        m_UIManager.RemoveUIElement(m_LeaderBoards);
        m_LeaderBoards = null;

        m_UIManager.RemoveUIElement(m_Achievements);
        m_Achievements = null;

		m_UIManager.RemoveUIElement(m_Play);
		m_Play = null;

        m_UIManager.RemoveUIElement(m_Credits);
        m_Credits = null;

        m_UIManager.RemoveUIElement(m_Tutorial);
        m_Tutorial = null;

        m_UIManager.RemoveUIElement(m_SignIn);
        m_SignIn = null;

        m_UIManager.RemoveUIElement(m_SignOut);
        m_SignOut = null;
	}

	@Override
	public void Update(double DeltaTime) 
	{
		ControlScheme Scheme = m_Game.GetControlScheme();

        if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_Play))
        {
            m_Play.Pressed();
            Scheme.ResetTouchCoordinates();
            return;
        }

        if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_LeaderBoards))
        {
            m_LeaderBoards.Pressed();
            Scheme.ResetTouchCoordinates();
            return;
        }

        if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_Achievements))
        {
            m_Achievements.Pressed();
            Scheme.ResetTouchCoordinates();
            return;
        }

        if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_Credits))
        {
            m_Credits.Pressed();
            Scheme.ResetTouchCoordinates();
            return;
        }

        if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_Tutorial))
        {
            m_Tutorial.Pressed();
            Scheme.ResetTouchCoordinates();
            return;
        }

        if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_SignIn))
        {
            m_Game.GetGooglePlayService().Connect();
            m_Game.GetDatabaseManager().SetAutoSignIn(true);

            m_SignIn.Pressed();
            Scheme.ResetTouchCoordinates();
            return;
        }

        if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_SignOut))
        {
            m_Game.GetGooglePlayService().Disconnect();
            m_Game.GetDatabaseManager().SetAutoSignIn(false);

            m_SignOut.Pressed();
            Scheme.ResetTouchCoordinates();
            return;
        }
	}

    private void ShowSignIn()
    {
        if(m_SignIn == null|| m_SignOut == null)
            return;

        m_SignOut.Hide();
        m_SignIn.Show();
    }

    private void ShowSignOut()
    {
        if(m_SignIn == null|| m_SignOut == null)
            return;

        m_SignIn.Hide();
        m_SignOut.Show();
    }

    private class OnConnectSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            ShowSignOut();
        }
    }

    private class OnDisconnectSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            ShowSignIn();
        }
    }
}
