package com.raggamuffin.protorunnerv2.ui;


// Author: Sinclair Ross
// Date:   04/06/2017


import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.master.TouchPointer;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.Vector2;

import junit.framework.Assert;

public class UIScreen_LearnToTouch extends UIScreen
{
    private UIObject_TouchMe m_TouchMe;

    private Publisher m_OnLearnToTouchCompletePublisher;
    private Subscriber m_OnPointerUpSubscriber;

    public UIScreen_LearnToTouch(GameLogic Game, UIManager uiManager)
    {
        super(Game, uiManager);

        m_TouchMe = null;
    }

    @Override
    public void Create()
    {
        m_TouchMe = new UIObject_TouchMe(m_UIManager, m_Game.GetGameAudioManager());

        PubSubHub pubSub = m_Game.GetPubSubHub();

        m_OnLearnToTouchCompletePublisher = pubSub.CreatePublisher(PublishedTopics.OnLearnToTouchComplete);

        m_OnPointerUpSubscriber = new OnPointerUpSubscriber();
        pubSub.SubscribeToTopic(PublishedTopics.OnPointerUp, m_OnPointerUpSubscriber);
    }

    @Override
    public void Destroy()
    {
        m_TouchMe = null;

        PubSubHub pubSub = m_Game.GetPubSubHub();
        pubSub.UnsubscribeFromTopic(PublishedTopics.OnPointerUp, m_OnPointerUpSubscriber);
    }

    @Override
    public void Update(double deltaTime)
    {
        ControlScheme scheme = m_Game.GetControlScheme();

        int activePointerCount = scheme.GetActivePointerCount();
        for (int i = 0; i < activePointerCount; ++i)
        {
            TouchPointer pointer = scheme.GetPointerAtIndex(i);
            Vector2 pointerPos = pointer.GetCurrentPosition();

            if (CollisionDetection.UIElementInteraction(pointerPos, m_TouchMe.GetTouchArea()))
            {
                m_TouchMe.OnHover();
            }
            else
            {
                m_TouchMe.OnHoverOff();
            }
        }

        m_TouchMe.Update(deltaTime);
    }

    private class OnPointerUpSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            TouchPointer pointer = (TouchPointer) args;
            Assert.assertNotNull(pointer);

            Vector2 pointerPos = pointer.GetCurrentPosition();

            if (CollisionDetection.UIElementInteraction(pointerPos, m_TouchMe.GetTouchArea()))
            {
                m_TouchMe.OnPress();
                m_OnLearnToTouchCompletePublisher.Publish();
                return;
            }
        }
    }
}
