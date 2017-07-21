package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   13/06/2017

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.master.TouchPointer;
import com.raggamuffin.protorunnerv2.utils.Action;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector2;

public class UITeacher_HoldMe extends UITeacher
{
    private static final double FILL_RATE = 1.0 / 2.0;
    private static final double EMPTY_RATE = 1.0 / 1.0;
    private static final double SCALE = 0.2;

    private final double[] m_Colour_Idle;
    private final double[] m_Colour_Hover;

    private UIManager m_UIManager;
    private GameLogic m_Game;

    private UIElement_Triangle m_TouchTriangle;
    private UIElement_Radial m_Radial;

    private UIAnimation m_TriangleAnimation;
    private UIAnimation m_Animation_Pulse;
    private UIAnimation m_Animation_FadeAndGrow;

    private UIElementColourFader m_ColourFader;
    private UIElementAlphaFader m_AlphaFader;

    private UITouchArea m_TouchArea;

    private Timer m_CompleteLessonDelay;

    private boolean m_HoverOn;

    public UITeacher_HoldMe(GameLogic game, UIManager uiManager)
    {
        m_UIManager = uiManager;
        m_Game = game;

        m_Colour_Idle = UIConstants.COLOUR_OFF;
        m_Colour_Hover = UIConstants.COLOUR_ON;

        m_CompleteLessonDelay = new Timer(1.0);

        m_TouchArea = new UITouchArea(0.3, -0.3, -0.3, 0.3);
    }

    @Override
    public void Initialise()
    {
        m_TouchTriangle = new UIElement_Triangle(30);
        m_TouchTriangle.SetScale(SCALE);
        m_TouchTriangle.SetColour(m_Colour_Idle);
        m_UIManager.AddUIElement(m_TouchTriangle);

        m_Radial = new UIElement_Radial();
        m_Radial.SetScale(SCALE * 2);
        m_Radial.SetColour(m_Colour_Idle);
        m_UIManager.AddUIElement(m_Radial);

        m_Animation_Pulse = new UIAnimation_Pulse(m_TouchTriangle, new Action_OnGrow());
        m_Animation_FadeAndGrow = new UIAnimation_Grow(m_TouchTriangle, 0.5);
        m_TriangleAnimation = m_Animation_Pulse;
        m_TriangleAnimation.Start();

        m_ColourFader = new UIElementColourFader(0.25, m_Colour_Idle);
        m_ColourFader.AddElements(m_TouchTriangle, m_Radial);

        m_AlphaFader = new UIElementAlphaFader(0.5, 0.0);
        m_AlphaFader.AddElements(m_Radial, m_TouchTriangle);
        m_AlphaFader.StartFade(0.0, 1.0);

        m_HoverOn = false;
    }

    @Override
    public void CleanUp()
    {
        m_UIManager.RemoveUIElement(m_TouchTriangle);
        m_UIManager.RemoveUIElement(m_Radial);

        m_TriangleAnimation = null;
        m_Animation_FadeAndGrow = null;
        m_Animation_Pulse = null;
    }

    @Override
    protected void UpdateAesthetic(double deltaTime)
    {
        m_ColourFader.Update();
        m_TriangleAnimation.Update(deltaTime);
        m_AlphaFader.Update();
    }

    @Override
    protected void UpdateLogic(double deltaTime)
    {
        if(!m_CompleteLessonDelay.IsActive())
        {
            double progress = m_Radial.GetProgress();
            progress += (m_HoverOn ? FILL_RATE : -EMPTY_RATE) * deltaTime;

            if (progress >= 1.0)
            {
                m_CompleteLessonDelay.Start();

                m_AlphaFader.StartFade(1.0, 0.0);

                m_TriangleAnimation = m_Animation_FadeAndGrow;
                m_TriangleAnimation.Start();
            }

            progress = MathsHelper.Clamp(progress, 0, 1);

            m_Radial.SetProgress(progress);
        }
    }

    @Override
    protected void UpdateTouchInputs()
    {
        ControlScheme controlScheme = m_Game.GetControlScheme();

        boolean touchDetected = false;
        int activePointerCount = controlScheme.GetActivePointerCount();
        for (int i = 0; i < activePointerCount; ++i)
        {
            TouchPointer pointer = controlScheme.GetPointerAtIndex(i);
            Vector2 pointerPos = pointer.GetCurrentPosition();

            if (CollisionDetection.UIElementInteraction(pointerPos, m_TouchArea))
            {
                touchDetected = true;
                break;
            }
        }

        if (touchDetected)
        {
            OnHover();
        }
        else
        {
            OnHoverOff();
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

    @Override
    public boolean HasCompletedLesson()
    {
        return m_CompleteLessonDelay.HasElapsed();
    }

    private class Action_OnGrow extends Action
    {
        @Override
        public void Fire()
        {
            GameAudioManager audioManager = m_Game.GetGameAudioManager();
            audioManager.PlaySound(AudioClips.TouchMe);
        }
    }
}
