package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.UIManager;
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
    private ProgressBarAnimation m_ClosingAnimation;

    public UIProgressBar(double maxLength, double maxValue, Colour highColour, Colour lowColour, Colour underColour, String label, Alignment align, GameAudioManager audio, UIManager uiManager)
    {
        super(uiManager);

        m_HighColour    = highColour;
        m_LowColour     = lowColour;
        m_UnderColour   = underColour;

        m_Alignment = align;

        m_Label = new UILabel(audio, uiManager);
        m_Label.GetFont().SetSize(0.07);
        m_Label.SetText(label);
        m_Label.GetFont().SetAlignment(Font.Alignment.Left);

        m_Type = UIElementType.ProgressBar;

        m_MaxLength = maxLength;

        m_Progress = 0.0;
        m_MaxValue  = maxValue;
        m_CurrentValue = m_MaxValue;

        m_UnderBarPosition = new Vector2();
        m_UnderBarScale = new Vector2();
        CalculateMaxSize();

        m_OpeningAnimation = new ProgressBarAnimation_GrowIn(this);
        m_ClosingAnimation = new ProgressBarAnimation_ShrinkOut(this);
    }

	@Override
	public void Update(double deltaTime)
	{
        m_OpeningAnimation.Update(deltaTime);
        m_ClosingAnimation.Update(deltaTime);

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
		m_Size.I = MathsHelper.Lerp(m_Progress, 0.0, m_UnderBarScale.I);
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
		m_Label.SetPositionAbsolute(x - (m_MaxLength * 0.5), y);
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

    @Override
    protected void TriggerClosingAnimation()
    {
        m_Label.TriggerClosingAnimation();
        m_ClosingAnimation.TriggerBehaviour();
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

    @Override
    public void Hide()
    {
        super.Hide();
        m_Label.Hide();
    }

    @Override
    public void Show(double delay)
    {
        super.Show(delay);
        m_Label.Show();
    }

    @Override
    public void SetHidden(boolean hide)
    {
        super.SetHidden(hide);
        m_Label.SetHidden(hide);
    }
}
