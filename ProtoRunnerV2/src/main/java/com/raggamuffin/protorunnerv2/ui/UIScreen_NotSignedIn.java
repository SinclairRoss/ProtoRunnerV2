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

public class UIScreen_NotSignedIn extends UIScreen
{
    private UIElement_Label m_Label_NotSignedIn;

    private UIObject_Button m_Button_SignIn;
    private UIObject_Button m_Button_Back;

    private Subscriber m_OnPointerUpSubscriber;

    public UIScreen_NotSignedIn(GameLogic Game, UIManager uiManager)
    {
        super(Game, uiManager);
    }

    @Override
    public void Create()
    {
        Context context = m_Game.GetContext();
        PubSubHub pubSub = m_Game.GetPubSubHub();

        m_Label_NotSignedIn = new UIElement_Label(context.getString(R.string.not_signed_in), UIConstants.FONTSIZE_STANDARD, 0, 0.5, Alignment.Center);
        m_UIManager.AddLabel(m_Label_NotSignedIn);

        Publisher publisher = pubSub.CreatePublisher(PublishedTopics.SwitchScreen);
        m_Button_Back = new UIObject_Button(context.getString(R.string.button_back), -0.5, 0, Alignment.Left, publisher, UIScreens.MainMenu.ordinal(), m_UIManager);
        m_Button_SignIn = new UIObject_Button(context.getString(R.string.sign_in), 0.5, 0, Alignment.Right, publisher, UIScreens.MainMenu.ordinal(), m_UIManager);

        m_OnPointerUpSubscriber = new OnPointerUpSubscriber();
        pubSub.SubscribeToTopic(PublishedTopics.OnPointerUp, m_OnPointerUpSubscriber);
    }

    @Override
    public void Update(double deltaTime)
    {
        ControlScheme scheme = m_Game.GetControlScheme();

        m_Button_Back.OnHoverOff();
        m_Button_SignIn.OnHoverOff();

        int activePointerCount = scheme.GetActivePointerCount();
        for (int i = 0; i < activePointerCount; ++i)
        {
            TouchPointer pointer = scheme.GetPointerAtIndex(i);
            Vector2 pointerPos = pointer.GetCurrentPosition();

            UIElement_TouchMarker marker = m_UIManager.GetTouchDisplay().GetMarkerWithID(pointer.GetId());
            marker.SetColour(UIConstants.COLOUR_OFF);

            if (CollisionDetection.UIElementInteraction(pointerPos, m_Button_Back.GetTouchArea()))
            {
                m_Button_Back.OnHover(marker);
            }

            if (CollisionDetection.UIElementInteraction(pointerPos, m_Button_SignIn.GetTouchArea()))
            {
                m_Button_SignIn.OnHover(marker);
            }
        }

        m_Label_NotSignedIn.Update(deltaTime);
        m_Button_Back.Update(deltaTime);
        m_Button_SignIn.Update(deltaTime);
    }

    @Override
    public void CleanUp()
    {
        m_Label_NotSignedIn = null;
        m_Button_Back = null;
        m_Button_SignIn = null;

        PubSubHub pubSub = m_Game.GetPubSubHub();
        pubSub.UnsubscribeFromTopic(PublishedTopics.OnPointerUp, m_OnPointerUpSubscriber);
    }

    private class OnPointerUpSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            TouchPointer pointer = (TouchPointer) args;
            Assert.assertNotNull(pointer);

            Vector2 pointerPos = pointer.GetCurrentPosition();

            if (CollisionDetection.UIElementInteraction(pointerPos, m_Button_Back.GetTouchArea()))
            {
                m_Button_Back.OnPress();
                return;
            }

            if (CollisionDetection.UIElementInteraction(pointerPos, m_Button_SignIn.GetTouchArea()))
            {
                m_Button_SignIn.OnPress();
                return;
            }
        }
    }
}
