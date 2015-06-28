package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector2;

public class UIProgressBar extends UIElement
{
	public enum Alignment { Left, Center, Right }
	
	private Alignment m_Alignment;
	
	private UILabel m_Label;
	
	private final double HEIGHT = 0.01;
	private final double FILL_RATE = 1.0;
	private final double ARRIVAL_DISTANCE = 0.1;

	private double m_MaxLength;
	
	private double m_Progress;
	private double m_MaxValue;
	private double m_CurrentValue;
	
	private Colour m_HighColour;
	private Colour m_LowColour;
	private Colour m_UnderColour;

    private Vector2 m_UnderBarPosition;
	private Vector2 m_UnderBarScale;

    private ProgressBarAnimation m_OpeningAnimation;
	
	public UIProgressBar(double MaxLength, double MaxValue, double[] HighColour, double[] LowColour, double[] UnderColour, String Label, Alignment Align, GameAudioManager audio)
	{
		super();
		
		m_Alignment = Align;
		
		m_Label = new UILabel(audio);
		m_Label.GetFont().SetSize(0.07);
		m_Label.SetText(Label);
		m_Label.GetFont().SetAlignment(Font.Alignment.Left);
	
		m_Type = UIElementType.ProgressBar;
		
		m_MaxLength = MaxLength;
		
		m_Progress = 0.0;
		m_MaxValue  = MaxValue;
		m_CurrentValue = m_MaxValue;
		
		m_HighColour = new Colour(HighColour);
		m_LowColour = new Colour(LowColour);
		
		m_UnderColour = new Colour(UnderColour);

        m_UnderBarPosition = new Vector2();
		m_UnderBarScale = new Vector2();
		CalculateMaxSize();

        m_OpeningAnimation = new ProgressBarAnimation_GrowIn(this);
	}

	@Override
	public void Update(double deltaTime)
	{
        m_OpeningAnimation.Update(deltaTime);

        UpdateProgress(deltaTime);
		CalculateSize();
		UpdatePosition();
		CalculateColour();
	}
	
	private void UpdateProgress(double DeltaTime)
	{
		double actualProgress = MathsHelper.Normalise(m_CurrentValue, 0.0, m_MaxValue);
		double deltaProgress = actualProgress - m_Progress;

		deltaProgress = MathsHelper.SignedNormalise(deltaProgress, -ARRIVAL_DISTANCE, ARRIVAL_DISTANCE);

		m_Progress += (deltaProgress * FILL_RATE * DeltaTime);
	}
	
	private void CalculateSize() 
	{	
		m_Size.I = MathsHelper.Lerp(m_Progress, 0.0, m_MaxLength);
		m_Size.J = HEIGHT;
	}

	private void UpdatePosition()
	{
		switch (m_Alignment)
		{
		case Center:
			break;

		case Left:
			m_Position.I = (m_OriginalPosition.I - (m_MaxLength * 0.5)) + (m_Size.I * 0.5);
			break;

		case Right:
			m_Position.I = (m_OriginalPosition.I + (m_MaxLength * 0.5)) - (m_Size.I * 0.5);
			break;
		}
	}

    private void UpdateUnderBarPosition()
    {
        switch (m_Alignment)
        {
            case Center:
                break;

            case Left:
                m_UnderBarPosition.I = (m_OriginalPosition.I - (m_MaxLength * 0.5)) + (m_UnderBarScale.I * 0.5);
                break;

            case Right:
                m_UnderBarPosition.I = (m_OriginalPosition.I + (m_MaxLength * 0.5)) - (m_UnderBarScale.I * 0.5);
                break;
        }
    }

	private void CalculateMaxSize()
	{
		m_UnderBarScale.I =  m_MaxLength;
		m_UnderBarScale.J = HEIGHT;
	}
	
	private void CalculateColour() 
	{
		m_Colour.Red 	= MathsHelper.Lerp(m_Progress, m_LowColour.Red,   m_HighColour.Red);
		m_Colour.Green 	= MathsHelper.Lerp(m_Progress, m_LowColour.Green, m_HighColour.Green);
		m_Colour.Blue 	= MathsHelper.Lerp(m_Progress, m_LowColour.Blue,  m_HighColour.Blue);
	}

	@Override
	public void SetPosition(double x, double y)
	{
		super.SetPosition(x, y);
        m_UnderBarPosition.SetVector(x,y);
		m_Label.SetPosition(x - (m_MaxLength * 0.5), y);
	}
	
	public void SetValue(double Value)
	{
		Value = MathsHelper.Clamp(Value, 0.0, m_MaxValue);
		m_CurrentValue = Value;
	}
	
	@Override
	protected void TriggerOpenAnimation(double delay)
	{
		m_Label.TriggerOpenAnimation(delay);
        m_OpeningAnimation.TriggerBehaviour(delay);
	}

	public UILabel GetLabel()
	{
		return m_Label;
	}
	
	public double GetMaxLength()
	{
		return m_MaxLength;
	}
	
	public Colour GetUnderColour()
	{
		return m_UnderColour;
	}
	
	public Vector2 GetUnderBarScale()
	{
		return m_UnderBarScale;
	}

    public void SetBackBarScale(double scale)
    {
        m_UnderBarScale.I = MathsHelper.Lerp(scale, 0.0, m_MaxLength);
        UpdateUnderBarPosition();
    }

    public Vector2 GetUnderBarPosition()
    {
        return m_UnderBarPosition;
    }
}
