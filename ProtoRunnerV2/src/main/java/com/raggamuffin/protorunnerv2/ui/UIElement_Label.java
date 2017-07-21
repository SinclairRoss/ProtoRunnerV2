package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.master.GameActivity;

public class UIElement_Label extends UIElement
{
	private String m_Text;
    private double m_FontSize;
    private Alignment m_Alignment;

    private UIAnimation_FadeIn m_Animation_FadeIn;
	
	public UIElement_Label(String text, double fontSize, double x, double y, Alignment alignment)
    {
        super(UIElementType.Label);

        m_Text = text;
        m_FontSize = fontSize;
        m_Alignment = alignment;

        SetPosition(x, y);

        m_Animation_FadeIn = new UIAnimation_FadeIn(this);
        m_Animation_FadeIn.Start();
    }

    public String GetText()
    {
        return m_Text;
    }
    public double GetFontSize() { return m_FontSize; }

    @Override
    public void Update(double deltaTime)
    {
        m_Animation_FadeIn.Update(deltaTime);
    }

    @Override
    public void SetPosition(double x, double y)
    {
        switch (m_Alignment)
        {
            case Center:
            {
                double length = CalculateLength() / GameActivity.SCREEN_RATIO;
                x -= (length / 2);

                break;
            }
            case Right:
            {
                double length = CalculateLength() / GameActivity.SCREEN_RATIO;
                x -= (length);

                break;
            }
        }

        super.SetPosition(x, y);
    }


    public double CalculateLength()
    {
        int numCharacters = m_Text.length();
        double length = (numCharacters * m_FontSize);

        return length;
    }
}