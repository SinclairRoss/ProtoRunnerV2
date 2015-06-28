package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.GameManager_Tutorial;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class TutorialScreen extends UIScreen
{
    private UIProgressBar m_ConditionProgress;

    public TutorialScreen(GameLogic Game, UIManager Manager)
    {
        super(Game, Manager);

        m_ConditionProgress = null;
    }

    @Override
    public void Create()
    {
        super.Create();

        m_ConditionProgress = new UIProgressBar(2.0, 1.0, Colours.CalvinOrange, Colours.ChaserOrange, Colours.HannahBlue, m_Game.GetContext().getString(R.string.tutorial_progress), UIProgressBar.Alignment.Left, m_Game.GetGameAudioManager());
        m_ConditionProgress.SetPosition(0.0, 0.4);

        m_UIManager.AddUIElement(m_ConditionProgress);
        m_UIManager.AddUIElement(m_ConditionProgress.GetLabel());
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
    }
}
