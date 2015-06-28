package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.tutorial.TutorialCondition;
import com.raggamuffin.protorunnerv2.tutorial.TutorialCondition_ShotsFired;
import com.raggamuffin.protorunnerv2.tutorial.TutorialCondition_Time;
import com.raggamuffin.protorunnerv2.tutorial.TutorialCondition_TurnAmount;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.ui.InGameMessageHandler;
import com.raggamuffin.protorunnerv2.ui.MessageOrientation;
import com.raggamuffin.protorunnerv2.ui.OptionalTextType;
import com.raggamuffin.protorunnerv2.ui.UIScreens;
import com.raggamuffin.protorunnerv2.utils.Timer;

import java.util.ArrayList;

public class GameManager_Tutorial extends GameManager
{
    private enum TutorialState
    {
        Starting,
        Idle,
        Inbetween,
        Complete
    }

    private TutorialState m_TutorialState;

    private InGameMessageHandler m_MessageHandler;
    private ArrayList<TutorialCondition> m_Conditions;
    private TutorialCondition m_ActiveCondition;
    private Timer m_InbetweenTimer;
    private int m_TutorialIndex;
    private final int MAX_INDEX;
    private Publisher m_TutorialCompletePublisher;

    public GameManager_Tutorial(GameLogic game)
    {
        super(game);

        m_TutorialState = TutorialState.Starting;
        m_MessageHandler = m_Game.GetUIManager().GetScreen(UIScreens.Tutorial).GetMessageHandler();
        m_InbetweenTimer = new Timer(1.0);

        m_TutorialIndex = 0;

        m_Conditions = new ArrayList<TutorialCondition>();
        m_Conditions.add(new TutorialCondition_Time(m_Game, m_Game.GetContext().getString(R.string.tutorial_start), 3.0));
        m_Conditions.add(new TutorialCondition_TurnAmount(m_Game, m_Game.GetContext().getString(R.string.tutorial_turning), 3));
        m_Conditions.add(new TutorialCondition_ShotsFired(m_Game, m_Game.GetContext().getString(R.string.tutorial_firing), 16));
        m_Conditions.add(new TutorialCondition_Time(m_Game, m_Game.GetContext().getString(R.string.tutorial_end), 3.0));

        m_ActiveCondition = m_Conditions.get(0);

        MAX_INDEX = m_Conditions.size() - 1;

        m_TutorialCompletePublisher = m_Game.GetPubSubHub().CreatePublisher(PublishedTopics.TutorialComplete);
    }

    @Override
    public void Update(double deltaTime)
    {
        switch (m_TutorialState)
        {
            case Starting:

                m_TutorialState = TutorialState.Idle;
                m_MessageHandler.DisplayMessage(m_ActiveCondition.GetMessage(), OptionalTextType.None, MessageOrientation.Top, 1, -1.0, 0.0);

                break;

            case Idle:
                m_ActiveCondition.Update(deltaTime);

                if(m_ActiveCondition.ConditionComplete())
                {
                    m_MessageHandler.RemoveMessage();
                    m_TutorialState = TutorialState.Inbetween;
                }
                break;

            case Inbetween:
                m_InbetweenTimer.Update(deltaTime);

                if(m_InbetweenTimer.TimedOut())
                {
                    m_InbetweenTimer.ResetTimer();
                    m_TutorialState = TutorialState.Starting;
                    NextState();
                }

                break;

            case Complete:
                break;
        }
    }

    @Override
    public void Initialise()
    {
        VehicleManager vManager = m_Game.GetVehicleManager();
        vManager.SpawnPlayer();
    }

    private void NextState()
    {
        m_TutorialIndex ++;

        if(m_TutorialIndex <= MAX_INDEX)
        {
            m_ActiveCondition = m_Conditions.get(m_TutorialIndex);
        }
        else
        {
            m_TutorialState = TutorialState.Complete;
            m_TutorialCompletePublisher.Publish();
            ResetTutorial();
        }
    }

    private void ResetTutorial()
    {
        m_TutorialIndex = 0;

        for(TutorialCondition condition : m_Conditions)
            condition.Reset();
    }

    public double GetConditionProgress()
    {
        if(m_ActiveCondition == null)
            return 1.0;

        return m_ActiveCondition.GetProgress();
    }
}
