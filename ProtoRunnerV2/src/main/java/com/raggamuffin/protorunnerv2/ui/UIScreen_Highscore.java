package com.raggamuffin.protorunnerv2.ui;

import android.content.Context;

import com.raggamuffin.protorunnerv2.R;
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

public class UIScreen_HighScore extends UIScreen
{
    private UIElement m_Title;

    private UIObject_Button m_BackButton;
    private Subscriber m_OnPointerUpSubscriber;

    public UIScreen_HighScore(GameLogic game, UIManager uiManager)
    {
        super(game, uiManager);
    }

    @Override
    public void Create()
    {
        Context context = m_Game.GetContext();

        m_Title = new UIElement_Label(context.getString(R.string.highscore_picker_screen_title), UIConstants.FONTSIZE_TITLE, -0.9, 0.0, Alignment.Left);
        m_UIManager.AddUIElement(m_Title);

        PubSubHub pubSub = m_Game.GetPubSubHub();

        Publisher publisher = pubSub.CreatePublisher(PublishedTopics.SwitchScreen);
        m_BackButton = new UIObject_Button(context.getString(R.string.button_back), -0.8, -0.8, Alignment.Left, publisher, UIScreens.MainMenu.ordinal(), m_UIManager);

        m_OnPointerUpSubscriber = new OnPointerUpSubscriber();
        pubSub.SubscribeToTopic(PublishedTopics.OnPointerUp, m_OnPointerUpSubscriber);
    }

    @Override
    public void CleanUp()
    {
        m_Title = null;

        m_BackButton = null;

        PubSubHub pubSub = m_Game.GetPubSubHub();
        pubSub.UnsubscribeFromTopic(PublishedTopics.OnPointerUp, m_OnPointerUpSubscriber);
    }

    @Override
    public void Update(double deltaTime)
    {
        ControlScheme scheme = m_Game.GetControlScheme();

        m_BackButton.OnHoverOff();

        int activePointerCount = scheme.GetActivePointerCount();
        for (int i = 0; i < activePointerCount; ++i)
        {
            TouchPointer pointer = scheme.GetPointerAtIndex(i);
            Vector2 pointerPos = pointer.GetCurrentPosition();

            UIElement_TouchMarker marker = m_UIManager.GetTouchDisplay().GetMarkerWithID(pointer.GetId());
            marker.SetColour(UIConstants.COLOUR_OFF);

            if (CollisionDetection.UIElementInteraction(pointerPos, m_BackButton.GetTouchArea()))
            {
                // UIElement_TouchMarker marker = m_UIManager.GetTouchDisplay().GetMarkerWithID(pointer.GetId());
                m_BackButton.OnHover(marker);
            }
        }

        m_BackButton.Update(deltaTime);
    }

    private class OnPointerUpSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            TouchPointer pointer = (TouchPointer) args;
            Assert.assertNotNull(pointer);

            Vector2 pointerPos = pointer.GetCurrentPosition();

            if (CollisionDetection.UIElementInteraction(pointerPos, m_BackButton.GetTouchArea()))
            {
                m_BackButton.OnPress();
                return;
            }
        }
    }
}
