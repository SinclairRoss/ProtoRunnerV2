package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.ColourManager;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;

public class TutorialScreen extends UIScreen
{
    private UIProgressBar m_ConditionProgress;
    private UIButton m_NextButton;

    public TutorialScreen(GameLogic Game, UIManager Manager)
    {
        super(Game, Manager);

        m_ConditionProgress = null;
    }

    @Override
    public void Create()
    {
        super.Create();

        ColourManager cManager = m_Game.GetColourManager();
        m_ConditionProgress = new UIProgressBar(2.0, 1.0, cManager.GetAccentingColour(), cManager.GetAccentTintColour(), cManager.GetPrimaryColour(), m_Game.GetContext().getString(R.string.tutorial_progress), UIProgressBar.Alignment.Left, m_Game.GetGameAudioManager(), m_UIManager);
        m_ConditionProgress.SetPosition(0.0, 0.3);

        m_UIManager.AddUIElement(m_ConditionProgress);
        m_UIManager.AddUIElement(m_ConditionProgress.GetLabel());

        m_ConditionProgress.SetHidden(true);

        m_NextButton = CreateButton(m_Game.GetContext().getString(R.string.tutorial_next), PublishedTopics.NextTutorialButtonPressed);
        m_NextButton.GetFont().SetColour(m_Game.GetColourManager().GetAccentingColour());
        m_NextButton.SetPosition(0.85, 0);
        m_NextButton.GetFont().SetAlignment(Font.Alignment.Right);
        m_NextButton.SetHidden(true);
    }

    @Override
    public void Remove()
    {
        super.Remove();

        m_UIManager.RemoveUIElement(m_ConditionProgress);
        m_ConditionProgress = null;
    }

    @Override
    public void Update(double deltaTime)
    {
        super.Update(deltaTime);

        m_ConditionProgress.SetValue(m_Game.GetTutorial().GetConditionProgress());

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
}
