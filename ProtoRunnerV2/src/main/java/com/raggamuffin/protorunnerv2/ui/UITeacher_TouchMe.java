package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   04/06/2017

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.master.TouchPointer;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.utils.Action;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector2;

import java.util.ArrayList;

public class UITeacher_TouchMe extends UITeacher
{
    private static final double SCALE = 0.2;

    private GameLogic m_Game;
    private GameAudioManager m_AudioManager;
    private UIManager m_UIManager;
    private ControlScheme m_ControlScheme;

    private UIAnimation m_TriangleAnimation;
    private UIAnimation m_Animation_Pulse;
    private UIAnimation m_Animation_FadeAndGrow;

    private Subscriber m_OnPointerUpSubscriber;

    private UIElement_Triangle m_TouchTriangle;
    private ArrayList<UIObject_ChevronRow> m_ChevronRows;
    private UITouchArea m_TouchArea;

    private Timer m_CompleteLessonDelay;

    private final double[] m_Colour_Idle;
    private final double[] m_Colour_Hover;

    private UIElementColourFader m_ColourFader;
    private UIElementAlphaFader m_AlphaFader;

    private boolean m_HoverOn;

    public UITeacher_TouchMe(GameLogic game, UIManager uiManager)
    {
        m_Game = game;
        m_AudioManager = game.GetGameAudioManager();
        m_UIManager = uiManager;
        m_ControlScheme = game.GetControlScheme();

        m_Colour_Idle = UIConstants.COLOUR_OFF;
        m_Colour_Hover = UIConstants.COLOUR_ON;

        m_CompleteLessonDelay = new Timer(1.0);

        m_TouchArea = new UITouchArea(0.3, -0.3, -0.3, 0.3);
    }

    @Override
    public void Initialise()
    {
        m_OnPointerUpSubscriber = new OnPointerUpSubscriber();
        m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.OnPointerUp, m_OnPointerUpSubscriber);

        m_TouchTriangle = new UIElement_Triangle(30);
        m_TouchTriangle.SetScale(SCALE);
        m_TouchTriangle.SetColour(m_Colour_Idle);
        m_UIManager.AddUIElement(m_TouchTriangle);

        m_ColourFader = new UIElementColourFader(0.25, m_Colour_Idle);
        m_ColourFader.AddElement(m_TouchTriangle);

        m_AlphaFader = new UIElementAlphaFader(0.5, 0);
        m_AlphaFader.AddElement(m_TouchTriangle);
        m_AlphaFader.StartFade(0.0, 1.0);

        m_Animation_Pulse = new UIAnimation_Pulse(m_TouchTriangle, new Action_OnGrow());
        m_Animation_FadeAndGrow = new UIAnimation_Grow(m_TouchTriangle, 0.5);
        m_TriangleAnimation = m_Animation_Pulse;
        m_TriangleAnimation.Start();

        m_ChevronRows = new ArrayList<>();
        Vector2 pos = new Vector2(0, -0.5);
        double rotation = 0;

        for(int i = 0; i < 3; ++i)
        {
            UIObject_ChevronRow chevronRow = new UIObject_ChevronRow(pos, rotation, SCALE / 2, m_UIManager);

            double deltaRotation = (Math.PI * 2) / 3;
            rotation += deltaRotation;
            pos.Rotate(deltaRotation);

            ArrayList<UIElement_Chevron> chevrons = chevronRow.GetChevrons();
            int numChevrons = chevrons.size();
            for(int j = 0; j < numChevrons; ++j)
            {
                m_ColourFader.AddElement(chevrons.get(j));
            }

            m_ChevronRows.add(chevronRow);
        }

        m_HoverOn = false;
    }

    @Override
    public void CleanUp()
    {
        m_UIManager.RemoveUIElement(m_TouchTriangle);
        for (int i = 0; i < 3; ++i)
        {
            m_ChevronRows.get(i).CleanUp();
        }

        m_Game.GetPubSubHub().UnsubscribeFromTopic(PublishedTopics.OnPointerUp, m_OnPointerUpSubscriber);

        m_TriangleAnimation = null;
        m_Animation_FadeAndGrow = null;
        m_Animation_Pulse = null;
    }

    @Override
    protected void UpdateTouchInputs()
    {
        int activePointerCount = m_ControlScheme.GetActivePointerCount();
        for (int i = 0; i < activePointerCount; ++i)
        {
            TouchPointer pointer = m_ControlScheme.GetPointerAtIndex(i);
            Vector2 pointerPos = pointer.GetCurrentPosition();

            if (CollisionDetection.UIElementInteraction(pointerPos, m_TouchArea))
            {
                OnHover();
            }
            else
            {
                OnHoverOff();
            }
        }
    }

    @Override
    protected void UpdateLogic(double deltaTime)
    {}

    @Override
    protected void UpdateAesthetic(double deltaTime)
    {
        m_TriangleAnimation.Update(deltaTime);
        m_ColourFader.Update();
        m_AlphaFader.Update();

        int numRows = m_ChevronRows.size();
        for(int i = 0; i < numRows; ++i)
        {
            m_ChevronRows.get(i).Update();
        }
    }

    private void OnHover()
    {
        if(!m_HoverOn)
        {
            m_ColourFader.StartFade(m_Colour_Idle, m_Colour_Hover);
            m_HoverOn = true;
        }
    }

    private void OnHoverOff()
    {
        if(m_HoverOn)
        {
            m_ColourFader.StartFade(m_Colour_Hover, m_Colour_Idle);
            m_HoverOn = false;
        }
    }

    public void OnPress()
    {
        if(!m_CompleteLessonDelay.IsActive())
        {
            int numRows = m_ChevronRows.size();
            for (int i = 0; i < numRows; ++i)
            {
                m_ChevronRows.get(i).StopAnimation();
            }

            m_TriangleAnimation = m_Animation_FadeAndGrow;
            m_TriangleAnimation.Start();

            m_AlphaFader.StartFade(1.0, 0.0);

            m_CompleteLessonDelay.Start();

            m_AudioManager.PlaySound(AudioClips.UI_Positive);
        }
    }

    @Override
    public boolean HasCompletedLesson()
    {
        return m_CompleteLessonDelay.HasElapsed();
    }

    private class OnPointerUpSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            TouchPointer pointer = (TouchPointer) args;
            Vector2 pointerPos = pointer.GetCurrentPosition();

            if (CollisionDetection.UIElementInteraction(pointerPos, m_TouchArea))
            {
                OnPress();
            }
        }
    }

    private class Action_OnGrow extends Action
    {
        @Override
        public void Fire()
        {
            m_AudioManager.PlaySound(AudioClips.TouchMe);

            int numRows = m_ChevronRows.size();
            for (int i = 0; i < numRows; ++i)
            {
                m_ChevronRows.get(i).ClearChevrons();
            }
        }
    }
}
