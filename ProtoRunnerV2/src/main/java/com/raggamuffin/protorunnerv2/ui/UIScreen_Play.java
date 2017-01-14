package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.ColourManager;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;

import java.util.ArrayList;

public class UIScreen_Play extends UIScreen
{
	private final double LABEL_DURATION = 2.0;

	private String m_RespawnText;
	private String m_WingmanDownText;

	private UINumericMeter m_ScoreMeter;

    private UILabel m_SystemFailingLabel;
    private UIProgressBar m_SystemFailingBar;

    private boolean m_ShowRebootMessage;
    private boolean m_ShowMessages;

    private boolean m_SystemFailing;

	public UIScreen_Play(GameLogic game, UIManager uiManager)
	{
		super(game, uiManager);

		m_RespawnText = m_Game.GetContext().getString(R.string.revive_text);
		m_WingmanDownText = m_Game.GetContext().getString(R.string.wingman_down);

        m_ScoreMeter = null;
        m_SystemFailingLabel = null;
        m_SystemFailingBar = null;

        PubSubHub pubSub = m_Game.GetPubSubHub();
        pubSub.SubscribeToTopic(PublishedTopics.GameReady, new GameReadySubscriber());
        pubSub.SubscribeToTopic(PublishedTopics.WingmanDestroyed, new WingmanDownSubscriber());
        pubSub.SubscribeToTopic(PublishedTopics.EndGame, new EndGameSubscriber());
        pubSub.SubscribeToTopic(PublishedTopics.PlayerDestroyed, new PlayerDestroyedSubscriber());
        pubSub.SubscribeToTopic(PublishedTopics.PlayerSpawned, new PlayerSpawnedSubscriber());

        m_ShowRebootMessage = false;
        m_ShowMessages = false;
        m_SystemFailing = false;
    }

	@Override
	public void Create() 
	{
		super.Create();

        m_ScoreMeter = new UINumericMeter(m_Game.GetGameAudioManager(), m_UIManager, Font.Alignment.Left, 0.075, -0.9, 0.8, m_Game.GetContext().getString(R.string.label_points_ui), m_Game.GetColourManager().GetUIPrimaryColour());

        m_SystemFailingLabel = new UILabel(m_Game.GetGameAudioManager(), m_UIManager);
        m_SystemFailingLabel.SetText(m_Game.GetContext().getString(R.string.down_but_not_out_text));
        m_SystemFailingLabel.SetPosition(0, 0);
        m_SystemFailingLabel.GetFont().SetAlignment(Font.Alignment.Center);

        ColourManager cManager = m_Game.GetColourManager();

        m_SystemFailingBar = new UIProgressBar(1.5, 1, cManager.GetUISecondaryColour(), cManager.GetUISecondaryColour(), cManager.GetUIAccentColour(), m_Game.GetContext().getString(R.string.empty), UIProgressBar.Alignment.Center, m_Game.GetGameAudioManager(), m_UIManager);
        m_SystemFailingBar.SetPosition(0, -0.1);
        m_SystemFailingBar.SnapToValue(1.0);

        m_UIManager.AddUIElement(m_ScoreMeter.GetLabel());
        m_UIManager.AddUIElement(m_SystemFailingLabel);
        m_UIManager.AddUIElement(m_SystemFailingBar);

        HideSystemFailingUI();

        m_ShowRebootMessage = true;
        m_ShowMessages = false;
	}

	@Override
	public void Remove() 
	{
        super.Remove();

        m_UIManager.RemoveUIElement(m_ScoreMeter.GetLabel());
        m_ScoreMeter = null;

        m_UIManager.RemoveUIElement(m_SystemFailingBar);
        m_SystemFailingBar = null;

        m_UIManager.RemoveUIElement(m_SystemFailingLabel);
        m_SystemFailingLabel = null;
	}

	@Override
	public void Update(double deltaTime) 
	{
        super.Update(deltaTime);

        int score = m_Game.GetGameStats().GetScore();
        m_ScoreMeter.SetValue(score);
        m_ScoreMeter.Update(deltaTime);

        if(m_SystemFailing)
        {
            m_SystemFailingBar.SetValue(m_Game.GetSecondWindHandler().GetInverseProgress());
        }
	}

	private class WingmanDownSubscriber extends Subscriber
	{
		@Override
		public void Update(int args)
		{
            if(m_ShowMessages)
            {
                m_MessageHandler.DisplayMessage(m_WingmanDownText, MessageOrientation.Center, 0.9, 1, LABEL_DURATION, 0.0);
            }
		}
	}

    private class EndGameSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_ShowRebootMessage = false;
        }
    }

    private class GameReadySubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_ShowMessages = true;
        }
    }

    private class PlayerDestroyedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_SystemFailing = true;
            ShowSystemFailingUI();
        }
    }

    private class PlayerSpawnedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_SystemFailing = false;
            HideSystemFailingUI();
            m_MessageHandler.DisplayMessage(m_RespawnText, MessageOrientation.Center, 0.9, 1, LABEL_DURATION, 0.0);
        }
    }

    private void ShowSystemFailingUI()
    {
        if(m_SystemFailingLabel != null)
        {
            m_SystemFailingLabel.Show();
        }

        if(m_SystemFailingBar != null)
        {
            m_SystemFailingBar.Show();
            m_SystemFailingBar.SnapToValue(1.0);
        }
    }

    private void HideSystemFailingUI()
    {
        if(m_SystemFailingLabel != null)
        {
            m_SystemFailingLabel.Hide();
        }

        if(m_SystemFailingBar != null)
        {
            m_SystemFailingBar.SetHidden(true);
        }
    }
}