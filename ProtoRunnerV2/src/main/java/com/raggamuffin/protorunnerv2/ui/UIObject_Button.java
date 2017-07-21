package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   03/06/2017

import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class UIObject_Button
{
    private static final double PADDING = 0.1;

    private static final double FILL_RATE = 5.0;
    private static final double ARRIVAL_DISTANCE = 0.1;

    private Colour m_Colour_Off;
    private Colour m_Colour_On;

    private UIElement_Label m_Label;
    private UIElement_Block m_Background;
    private UIElement_Block m_EndBlock;

    private Publisher m_OnClickPublisher;
    private Object m_Args;

    private final double m_Height;

    private final double m_BackgroundLength_Extended;
    private final double m_BackgroundLength_Retracted;
    private double m_BackgroundLength_Target;
    private double m_BackgroundLength;

    private UITouchArea m_TouchArea;

    public  UIObject_Button(String text, double x, double y, Alignment alignment, Publisher onClickPublisher, Object args, UIManager uiManager)
    {
        m_Colour_On = new Colour(UIConstants.COLOUR_ON, 0.2);
        m_Colour_Off = new Colour(UIConstants.COLOUR_OFF, 0.1);

        m_OnClickPublisher = onClickPublisher;
        m_Args = args;

        m_Label = new UIElement_Label(text, UIConstants.FONTSIZE_STANDARD, x, y, alignment);
        uiManager.AddUIElement(m_Label);

        m_Height = UIConstants.FONTSIZE_STANDARD * 2;
        m_BackgroundLength_Retracted = m_Height / UIConstants.GOLDEN_RATIO;
        m_BackgroundLength_Extended = (m_Label.CalculateLength() + (PADDING * 2));

        double xPos = x + (alignment == Alignment.Right ? PADDING : -PADDING);

        m_EndBlock = new UIElement_Block(m_Colour_Off, alignment);
        m_EndBlock.SetAlpha(0.6);
        m_EndBlock.SetPosition(xPos, y);
        m_EndBlock.SetScale(m_BackgroundLength_Retracted, m_Height);
        uiManager.AddUIElement(m_EndBlock);

        m_Background = new UIElement_Block(m_Colour_Off, alignment);
        m_Background.SetAlpha(0.1);
        m_Background.SetPosition(xPos, y);
        m_Background.SetScale(m_BackgroundLength_Retracted, m_Height);
        uiManager.AddUIElement(m_Background);

        double touchX = m_Label.GetPosition().X;
        double EndBlockSize = m_BackgroundLength_Retracted + PADDING;

        switch (alignment)
        {
            case Left:
                m_TouchArea = new UITouchArea(y + (m_Height / 2), y - (m_Height / 2), touchX - EndBlockSize, touchX + m_BackgroundLength_Extended);
                break;
            case Center:
            case Right:
                m_TouchArea = new UITouchArea(y + (m_Height / 2), y - (m_Height / 2), touchX, touchX + m_BackgroundLength_Extended + EndBlockSize);
                break;
        }
    }

    public void Update(double deltaTime)
    {
        double deltaProgress = m_BackgroundLength_Target - m_BackgroundLength;
        deltaProgress = MathsHelper.SignedNormalise(deltaProgress, -ARRIVAL_DISTANCE, ARRIVAL_DISTANCE);

        m_BackgroundLength += (deltaProgress * FILL_RATE * deltaTime);
        m_Background.SetScale(m_BackgroundLength, m_Height);
    }

    public void OnPress()
    {
        m_OnClickPublisher.Publish(m_Args);
    }

    public void OnHover(UIElement_TouchMarker marker)
    {
        if(Double.compare(m_BackgroundLength_Target, m_BackgroundLength_Extended) != 0)
        {
            m_BackgroundLength_Target = m_BackgroundLength_Extended;
            m_Background.SetColour(m_Colour_On);
            m_EndBlock.SetColour(m_Colour_On);

            marker.SetColour(m_Colour_On);
        }
    }

    public void OnHoverOff()
    {
        if(Double.compare(m_BackgroundLength_Target, m_BackgroundLength_Retracted) != 0)
        {
            m_BackgroundLength_Target = m_BackgroundLength_Retracted;
            m_Background.SetColour(m_Colour_Off);
            m_EndBlock.SetColour(m_Colour_Off);
        }
    }

    public UIElement_Label GetLabel()
    {
        return m_Label;
    }

    public UITouchArea GetTouchArea() { return m_TouchArea; }
}
