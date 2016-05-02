package com.raggamuffin.protorunnerv2.tutorial;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.ui.InGameMessageHandler;
import com.raggamuffin.protorunnerv2.ui.MessageOrientation;
import com.raggamuffin.protorunnerv2.ui.UIScreen_Tutorial;
import com.raggamuffin.protorunnerv2.ui.UIScreens;

public abstract class TutorialCondition
{
    protected final GameLogic m_Game;
    private final String m_Message;
    private final OptionalElement m_OptionalElement;

    private final UIScreen_Tutorial m_TutorialScreen;
    private final InGameMessageHandler m_MessageHandler;
    private TutorialEffect[] m_Effects;

    public TutorialCondition(GameLogic game, String message, OptionalElement element, TutorialEffect... effects)
    {
        m_Game = game;
        m_Message = message;
        m_OptionalElement = element;
        m_Effects = effects;

        m_TutorialScreen = (UIScreen_Tutorial)m_Game.GetUIManager().GetScreen(UIScreens.Tutorial);
        m_MessageHandler = m_TutorialScreen.GetMessageHandler();
    }

    public abstract boolean ConditionComplete();
    public abstract double GetProgress();

    public void Initialise()
    {
        m_MessageHandler.DisplayMessage(m_Message, MessageOrientation.Top, 0.9, 1, -1.0, 0.0);
        CreateOptionalElement();
    }

    public abstract void Update(double deltaTime);

    public void Reset()
    {
        m_MessageHandler.Clear();
        ClearOptionalElement();
    }

    private void CreateOptionalElement()
    {
        switch(m_OptionalElement)
        {
            case None:
                break;
            case ProgressBar:
                m_TutorialScreen.ShowProgressBar();
                break;
            case NextButton:
                m_TutorialScreen.ShowNextButton();
        }
    }

    private void ClearOptionalElement()
    {
        switch(m_OptionalElement)
        {
            case None:
                break;
            case ProgressBar:
                m_TutorialScreen.HideProgressBar();
                break;
            case NextButton:
                m_TutorialScreen.HideNextButton();
                break;
        }
    }

    public TutorialEffect[] GetEffects()
    {
        return m_Effects;
    }
}
