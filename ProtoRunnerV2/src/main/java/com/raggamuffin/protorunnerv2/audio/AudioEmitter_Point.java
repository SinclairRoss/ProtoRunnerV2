package com.raggamuffin.protorunnerv2.audio;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class AudioEmitter_Point extends AudioEmitter
{
    public AudioEmitter_Point(GameObject anchor, GameAudioManager audioManager, AudioClips audioClip, EAudioRepeatBehaviour repeatBehaviour)
    {
        super(anchor, audioManager, audioClip, repeatBehaviour);
    }

    @Override
    protected Vector3 CalculateEmitterPosition()
    {
        return m_Anchor.GetPosition();
    }
}
