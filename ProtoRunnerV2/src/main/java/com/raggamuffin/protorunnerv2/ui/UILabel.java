package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.ui.TextAnimation.AnimationState;

public class UILabel extends UIElement
{
	private String m_OriginalText;
	private String m_OutputText;
	private Font m_Font;
	
	private TextAnimation m_OpeningAnimation;
	
	public UILabel(GameAudioManager audio)
	{
		super();
		
		m_Font = new Font(this, 0.1);
		
		SetText("Hello World");
		CalculateSize();
		
		m_OpeningAnimation = new TextAnimation_ForwardIn(this, audio, TextAnimation.ActivationMode.Triggered);
		
		m_Type = UIElementType.Label;
	}
	
	@Override
	public void Update(double DeltaTime)
	{
		m_OpeningAnimation.Update(DeltaTime);
	}
	
	protected void CalculateSize()
	{
		m_Size.I = (m_OriginalText.length() * m_Font.GetSize());
		m_Size.J = m_Font.GetSize();
	}
	
	public void ResetText()
	{
		m_OutputText = m_OriginalText;
	}
	
	@Override
	protected void TriggerOpenAnimation(double delay)
	{
		m_OpeningAnimation.TriggerBehaviour(delay);
	}
	
	///// Setters.
	public void SetText(String Text)
	{
		m_OriginalText = Text;
		m_OutputText = m_OriginalText;
		
		CalculateSize();
		m_Font.ReAlign();
	}
	
	// Used by animations to update the text without loosing the orignal data.
	public void SetOutputText(String Text)
	{
		m_OutputText = Text;
	}
	
	public Font GetFont()
	{
		return m_Font;
	}
	
	public String GetOriginalText()
	{
		return m_OriginalText;
	}
	
	public String GetText()
	{
		return m_OutputText;
	}
	
	public boolean IsAnimationComplete()
	{
		return m_OpeningAnimation.GetState() == AnimationState.Idle;
	}
}
