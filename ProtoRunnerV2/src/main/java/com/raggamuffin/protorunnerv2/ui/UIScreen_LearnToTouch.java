package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   04/06/2017

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;

import java.util.ArrayList;

public class UIScreen_LearnToTouch extends UIScreen
{
    private Publisher m_OnLearnToTouchCompletePublisher;

    private ArrayList<UITeacher> m_Teachers;
    private UITeacher m_ActiveTeacher;

    public UIScreen_LearnToTouch(GameLogic Game, UIManager uiManager)
    {
        super(Game, uiManager);

        m_Teachers = null;
        m_ActiveTeacher = null;
    }

    @Override
    public void Create()
    {
        m_Teachers = new ArrayList<>(1);
        m_Teachers.add(new UITeacher_TouchMe(m_Game, m_UIManager));
        //m_Teachers.add(new UITeacher_HoldMe(m_Game, m_UIManager));

        StartNextLesson();

        PubSubHub pubSub = m_Game.GetPubSubHub();
        m_OnLearnToTouchCompletePublisher = pubSub.CreatePublisher(PublishedTopics.OnLearnToTouchComplete);
    }

    @Override
    public void CleanUp()
    {
        m_Teachers.clear();
        m_Teachers = null;
    }

    @Override
    public void Update(double deltaTime)
    {
        m_ActiveTeacher.Update(deltaTime);

        if(m_ActiveTeacher.HasCompletedLesson())
        {
            m_ActiveTeacher.CleanUp();

            if(m_Teachers.isEmpty())
            {
                m_OnLearnToTouchCompletePublisher.Publish();
            }
            else
            {
                StartNextLesson();
            }
        }
    }

    private void StartNextLesson()
    {
        m_ActiveTeacher = m_Teachers.remove(0);
        m_ActiveTeacher.Initialise();
    }
}
