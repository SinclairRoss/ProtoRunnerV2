package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;

public class UIButton extends UILabel
{
	Publisher m_PressedPublisher;
	GameAudioManager m_GameAudioManager;
	int m_Args;
	
	public UIButton(GameAudioManager audio)
	{
		super(audio);
		
		m_GameAudioManager = audio;
		m_Type = UIElementType.Button;
	}
	
	public UIButton(Publisher PressedPublisher, GameAudioManager audio, final int args)
	{
		super(audio);
		
		m_PressedPublisher = PressedPublisher;
		m_GameAudioManager = audio;
		m_Args = args;
		
		m_Type = UIElementType.Button;
	}
	
	public void Pressed()
	{
		if(IsHidden())
			return;
		
		m_GameAudioManager.PlaySound(AudioClips.UIClick);
		
		if(m_PressedPublisher != null)
			m_PressedPublisher.Publish(m_Args);
	}
}
