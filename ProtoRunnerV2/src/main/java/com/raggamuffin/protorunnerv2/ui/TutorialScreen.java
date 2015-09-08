package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Runner;
import com.raggamuffin.protorunnerv2.managers.ColourManager;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class TutorialScreen extends UIScreen
{
    private Runner m_Player;
    private UIProgressBar m_HealthBar;
    private UIProgressBar m_ConditionProgress;
    private UIButton m_NextButton;
    private UIPanel m_LeftPanel;
    private UIPanel m_RightPanel;

    public TutorialScreen(GameLogic Game, UIManager Manager)
    {
        super(Game, Manager);

        m_ConditionProgress = null;
        m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerSpawned, new PlayerSpawnedSubscriber());
    }

    @Override
    public void Create()
    {
        super.Create();

        ColourManager cManager = m_Game.GetColourManager();

        // Condition progress bar
        m_ConditionProgress = new UIProgressBar(1.2, 1.0, cManager.GetAccentingColour(), cManager.GetAccentTintColour(), cManager.GetPrimaryColour(), m_Game.GetContext().getString(R.string.tutorial_progress), UIProgressBar.Alignment.Left, m_Game.GetGameAudioManager(), m_UIManager);
        m_ConditionProgress.SetPosition(0.0, 0.3);

        m_UIManager.AddUIElement(m_ConditionProgress);
        m_UIManager.AddUIElement(m_ConditionProgress.GetLabel());

        m_ConditionProgress.SetHidden(true);

        // Next button
        m_NextButton = CreateButton(m_Game.GetContext().getString(R.string.tutorial_next), PublishedTopics.NextTutorialButtonPressed);
        m_NextButton.GetFont().SetColour(m_Game.GetColourManager().GetAccentingColour());
        m_NextButton.SetPosition(0.85, 0);
        m_NextButton.GetFont().SetAlignment(Font.Alignment.Right);
        m_NextButton.SetHidden(true);

        m_LeftPanel = new UIPanel(m_UIManager,cManager.GetAccentingColour());
        m_LeftPanel.SetPosition(-0.5, 0.0);
        m_UIManager.AddUIElement(m_LeftPanel);
        m_LeftPanel.SetHidden(true);

        m_RightPanel = new UIPanel(m_UIManager, cManager.GetPrimaryColour());
        m_RightPanel.SetPosition(0.5, 0.0);
        m_UIManager.AddUIElement(m_RightPanel);
        m_RightPanel.SetHidden(true);

        // Health bar
        Runner player = m_Game.GetVehicleManager().GetPlayer();

        String HealthString = m_Game.GetContext().getString(R.string.empty);
        m_HealthBar = new UIProgressBar(1.6, player.GetMaxHullPoints(), cManager.GetAccentingColour(), cManager.GetAccentTintColour(), new Colour(Colours.Clear), HealthString, UIProgressBar.Alignment.Center, m_Game.GetGameAudioManager(), m_UIManager);
        m_HealthBar.SetPosition(0.0, 0.9);

        m_UIManager.AddUIElement(m_HealthBar);

        m_HealthBar.SetHidden(true);
    }

    @Override
    public void Remove()
    {
        super.Remove();

        m_UIManager.RemoveUIElement(m_ConditionProgress);
        m_ConditionProgress = null;

        m_UIManager.RemoveUIElement(m_NextButton);
        m_NextButton = null;

        m_UIManager.RemoveUIElement(m_HealthBar);
        m_HealthBar = null;

        m_UIManager.RemoveUIElement(m_LeftPanel);
        m_LeftPanel = null;

        m_UIManager.RemoveUIElement(m_RightPanel);
        m_RightPanel = null;
    }

    @Override
    public void Update(double deltaTime)
    {
        super.Update(deltaTime);

        m_ConditionProgress.SetValue(m_Game.GetTutorial().GetConditionProgress());
        m_HealthBar.SetValue(m_Player.GetHullPoints());

        ControlScheme Scheme = m_Game.GetControlScheme();

        if(CollisionDetection.UIElementInteraction(Scheme.GetTouchCoordinates(), m_UIManager.GetScreenSize(), m_NextButton))
        {
            m_NextButton.Pressed();
            Scheme.ResetTouchCoordinates();

            return;
        }
    }

    public void HideProgressBar()
    {
        if(m_ConditionProgress == null)
            return;

        m_ConditionProgress.Hide();
    }

    public void ShowProgressBar()
    {
        if(m_ConditionProgress == null)
            return;

        m_ConditionProgress.Show();
    }

    public void ShowNextButton()
    {
        if(m_NextButton == null)
            return;

        m_NextButton.Show();
    }

    public void HideNextButton()
    {
        if(m_NextButton == null)
            return;

        m_NextButton.Hide();
    }

    public void ShowHealthBar()
    {
        if(m_HealthBar == null)
            return;

        m_HealthBar.Show();
    }

    public void HideHealthBar()
    {
        if(m_HealthBar == null)
            return;

        m_HealthBar.Hide();
    }

    public void ShowLeftPanel()
    {
        if(m_LeftPanel == null)
            return;

        m_LeftPanel.Show();
    }

    public void HideLeftPanel()
    {
        if(m_LeftPanel == null)
            return;

        m_LeftPanel.Hide();
    }

    public void ShowRightPanel()
    {
        if(m_RightPanel == null)
            return;

        m_RightPanel.Show();
    }

    public void HideRightPanel()
    {
        if(m_RightPanel == null)
            return;

        m_RightPanel.Hide();
    }

    private class PlayerSpawnedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_Player = m_Game.GetVehicleManager().GetPlayer();
        }
    }
}
