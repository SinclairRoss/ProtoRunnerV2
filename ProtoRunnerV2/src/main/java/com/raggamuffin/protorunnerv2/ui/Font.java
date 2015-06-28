package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class Font 
{
	public enum Alignment { Left, Center, Right };
	public Alignment m_Alignment;
	
	private UIElement m_Anchor;
	private double m_Size;
	private Colour m_Colour;
    private double m_Spacing;
	
	public Font(UIElement Element, double Size)
	{
		m_Alignment = Alignment.Center;
		m_Anchor = Element;
		m_Size = Size;
		m_Colour = new Colour(Colours.White);
	}
	
	public double GetSize()
	{
		return m_Size;
	}
	
	public Colour GetColour()
	{
		return m_Colour;
	}
	
	public void SetColour(double[] Colour)
	{
		m_Colour.SetColour(Colour);
	}
	
	public void SetAlignment(Alignment Align)
	{
		m_Alignment = Align;
		
		ReAlign();
	}
	
	public void ReAlign()
	{
		switch(m_Alignment)
		{
			case Center:
				m_Anchor.CentreHorizontal();
				break;
				
			case Left:
				m_Anchor.AlignLeft();
				break;
				
			case Right:
				m_Anchor.AlignRight();
				break;
				
			default:
				break;
		
		}
	}
	
	public void SetSize(double Size)
	{
		m_Size = Size;
	}
}
