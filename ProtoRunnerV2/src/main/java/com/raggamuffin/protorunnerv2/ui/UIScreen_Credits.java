package com.raggamuffin.protorunnerv2.ui;

import android.content.Context;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.master.TouchPointer;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Vector2;

import junit.framework.Assert;

public class UIScreen_Credits extends UIScreen
{
	private UIElement m_Title;
	
	private UIElement_Label m_Line1;
	private UIElement_Label m_Line2;
    private UIElement_Label m_Line2Subtext;
	private UIElement_Label m_Break;
	private UIElement_Label m_Line3;
	private UIElement_Label m_Line4;
    private UIElement_Label m_Line4Subtext;
	private UIElement_Label m_Line5;
    private UIElement_Label m_Line5Subtext;

	private UIObject_Button m_BackButton;

    private Subscriber m_OnPointerUpSubscriber;

	public UIScreen_Credits(GameLogic game, UIManager uiManager)
	{
		super(game, uiManager);
	}
	
	@Override
	public void Create()
	{
		Context context = m_Game.GetContext();

		m_Title = new UIElement_Label(context.getString(R.string.credits_screen_title), UIConstants.FONTSIZE_TITLE, -0.9, 0.0, Alignment.Left);
		m_UIManager.AddUIElement(m_Title);

		m_Line1 = new UIElement_Label(context.getString(R.string.credits_text1), UIConstants.FONTSIZE_TITLE, 0.9, 0.8, Alignment.Right);
		m_UIManager.AddUIElement(m_Line1);

		m_Line2 = new UIElement_Label(context.getString(R.string.credits_text2), UIConstants.FONTSIZE_TITLE, 0.9, 0.6, Alignment.Right);
		m_Line2.SetColour(Colours.RunnerBlue);//m_Game.GetColourManager().GetPrimaryColour());
		m_UIManager.AddUIElement(m_Line2);

		PubSubHub pubSub = m_Game.GetPubSubHub();

		Publisher publisher = pubSub.CreatePublisher(PublishedTopics.SwitchScreen);
		m_BackButton = new UIObject_Button(context.getString(R.string.button_back), -0.8, -0.8, Alignment.Left, publisher, UIScreens.MainMenu.ordinal(), m_UIManager);

        m_OnPointerUpSubscriber = new OnPointerUpSubscriber();
        pubSub.SubscribeToTopic(PublishedTopics.OnPointerUp, m_OnPointerUpSubscriber);

        //m_Line2Subtext = CreateLabelSubtext(m_Line2, context.getString(R.string.credits_text2_sub));
		//m_Break = CreateLabel(context.getString(R.string.empty));
		//m_Line3 = CreateLabel(context.getString(R.string.credits_text3));
		//m_Line4 = CreateLabel(context.getString(R.string.credits_text4));
        //m_Line4.SetColour(m_Game.GetColourManager().GetPrimaryColour());
        //m_Line4Subtext  = CreateLabelSubtext(m_Line4, context.getString(R.string.credits_text4_sub));
		//m_Line5 = CreateLabel(context.getString(R.string.credits_text5));
        //m_Line5.SetColour(m_Game.GetColourManager().GetPrimaryColour());
        //m_Line5Subtext  = CreateLabelSubtext(m_Line5, context.getString(R.string.credits_text5_sub));
	}
	
	@Override
	public void CleanUp()
    {
		m_Title = null;
		m_Line1 = null;
		m_Line2 = null;
        m_Line2Subtext = null;
		m_Break = null;
		m_Line3 = null;
        m_Line4 = null;
        m_Line4Subtext = null;
        m_Line5 = null;
        m_Line5Subtext = null;

        PubSubHub pubSub = m_Game.GetPubSubHub();
        pubSub.UnsubscribeFromTopic(PublishedTopics.OnPointerUp, m_OnPointerUpSubscriber);

        m_BackButton = null;
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