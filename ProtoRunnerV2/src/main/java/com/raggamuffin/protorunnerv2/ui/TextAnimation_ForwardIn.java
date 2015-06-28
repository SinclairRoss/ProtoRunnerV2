package com.raggamuffin.protorunnerv2.ui;

import java.util.Vector;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;

public class TextAnimation_ForwardIn extends TextAnimation
{
	private final double DeltaActivation = 0.05;
	private double m_Timer;
	
	private String m_OutputText;
	
	private Vector<Character> m_CharList;
	private String m_OriginalText;
	private int m_Length;
	
	public TextAnimation_ForwardIn(UILabel Element, GameAudioManager audio, ActivationMode Mode) 
	{
		super(Element, audio, Mode);

		m_OutputText = "";
		
		m_CharList = new Vector<Character>();
		m_OriginalText 	= "";
		m_Length 		= 0;	
	}

	@Override
	protected void InitialiseBehaviour() 
	{
		m_CharList.clear();
		
		m_OriginalText 	= m_Anchor.GetOriginalText();
		m_Length 		= m_OriginalText.length();
		
		m_OutputText = "";
		m_Anchor.SetOutputText(m_OutputText);
		
		for(int i = 0; i < m_Length; i++)
		{
			m_CharList.add(m_OriginalText.charAt(i));
		}
	}

	@Override
	protected void UpdateBehaviour(double DeltaTime) 
	{
		if(m_Length == 0)
			return;
		
		if(m_Anchor.IsHidden())
			return;
		
		if(m_CharList.elementAt(0) == ' ')
			m_Timer = DeltaActivation;
		
		m_Timer += DeltaTime;
		
		if(m_Timer > DeltaActivation)
		{
			m_OutputText = m_OutputText.concat(Character.toString(m_CharList.remove(0)));
			m_Anchor.SetOutputText(m_OutputText);
			
			m_GameAudioManager.PlaySound(AudioClips.UITick);
			
			m_Timer -= DeltaActivation;
			
			if(m_CharList.isEmpty())
			{
				AnimationComplete();
			}
		}
	}
}
