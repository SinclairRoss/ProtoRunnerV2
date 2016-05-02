package com.raggamuffin.protorunnerv2.audio;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class AudioEmitter
{
    protected GameAudioManager m_AudioManager;
    protected AudioClips m_AudioClip;
    protected EAudioRepeatBehaviour m_AudioRepeatBehaviour;

    private int m_StreamID;

    protected GameObject m_Anchor;

    public AudioEmitter(GameObject anchor, GameAudioManager audioManager, AudioClips audioClip, EAudioRepeatBehaviour repeatBehaviour)
    {
        m_AudioManager = audioManager;
        m_AudioClip = audioClip;
        m_AudioRepeatBehaviour = repeatBehaviour;

        m_StreamID = -1;

        m_Anchor = anchor;
    }

    protected abstract Vector3 CalculateEmitterPosition();

    public void Start()
    {
        Vector3 emitterPos = CalculateEmitterPosition();
        m_StreamID = m_AudioManager.PlaySound(emitterPos, m_AudioClip, m_AudioRepeatBehaviour);
    }

    public void Stop()
    {
        if(m_AudioRepeatBehaviour == EAudioRepeatBehaviour.Manual)
        {
            m_AudioManager.StopSound(m_StreamID);
        }
    }

    public void Destroy()
    {
        Stop();

        m_AudioManager = null;
    }
}
