package com.raggamuffin.protorunnerv2.ui;

import android.provider.MediaStore;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;

public class UIButton extends UILabel
{
	Publisher m_OnPressPublisher;
    int m_Args;

	GameAudioManager m_GameAudioManager;
	AudioClips m_OnPressClip;

    public UIButton(UIManager uiManager, Publisher onPressPublisher, int publisherArgs, GameAudioManager audioManager, AudioClips onPressClip)
    {
        super(audioManager, uiManager);

        m_OnPressPublisher = onPressPublisher;
        m_Args = publisherArgs;

        m_GameAudioManager = audioManager;
        m_OnPressClip = onPressClip;

        m_Type = UIElementType.Label;
    }
	
	public void Pressed()
	{
		if(!IsHidden())
		{
			m_GameAudioManager.PlaySound(m_OnPressClip);

			if (m_OnPressPublisher != null)
			{
                m_OnPressPublisher.Publish(m_Args);
			}
		}
	}
}
