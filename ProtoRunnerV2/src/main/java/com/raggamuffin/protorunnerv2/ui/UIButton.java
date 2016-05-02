package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;

public class UIButton extends UILabel
{
	Publisher m_PressedPublisher;
	GameAudioManager m_GameAudioManager;
	int m_Args;
	
	public UIButton(GameAudioManager audio, UIManager uiManager)
	{
		super(audio, uiManager);
		
		m_GameAudioManager = audio;
		m_Type = UIElementType.Label;
	}

    public UIButton(Publisher PressedPublisher, GameAudioManager audio, UIManager uiManager, final int args)
    {
        super(audio, uiManager);

        m_PressedPublisher = PressedPublisher;
        m_GameAudioManager = audio;
        m_Args = args;

        m_Type = UIElementType.Label;
    }

    public UIButton(Publisher PressedPublisher, GameAudioManager audio, UIManager uiManager)
    {
        super(audio, uiManager);

        m_PressedPublisher = PressedPublisher;
        m_GameAudioManager = audio;

        m_Type = UIElementType.Label;
    }
	
	public void Pressed()
	{
		if(!IsHidden())
		{
			m_GameAudioManager.PlaySound(AudioClips.UIClickFWD);

			if (m_PressedPublisher != null)
			{
				m_PressedPublisher.Publish(m_Args);
			}
		}
	}
}
