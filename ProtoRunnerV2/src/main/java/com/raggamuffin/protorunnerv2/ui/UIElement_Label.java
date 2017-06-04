package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.managers.UIManager;

public class UIElement_Label extends UIElement
{
	private String m_Text;
    private double m_FontSize;
    private Alignment m_Alignment;
	
	public UIElement_Label(String text, double fontSize, double x, double y, Alignment alignment, UIManager uiManager)
	{
		super(UIElementType.Label, uiManager);

        m_Text = text;
        m_FontSize = fontSize;
        m_Alignment = alignment;

        SetPosition(x, y);
	}
	
	@Override
	public void Update(double deltaTime)
    {}

    public String GetText()
    {
        return m_Text;
    }
    public double GetFontSize() { return m_FontSize; }

    @Override
    public void SetPosition(double x, double y)
    {
        switch (m_Alignment)
        {
            case Center:
            {
                double length = CalculateLength() / SCREEN_RATIO;
                x -= (length / 2);

                break;
            }
            case Right:
            {
                double length = CalculateLength() / SCREEN_RATIO;
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