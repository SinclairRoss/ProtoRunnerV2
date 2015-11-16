package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;

// TODO: THINK ABOUT HOW TO MAKE THIS.

public class MissileLockMarker extends GameObject
{
    public MissileLockMarker(PubSubHub PubSub, GameAudioManager audio)
    {
        super(null, null);
    }

    @Override
    public boolean IsValid()
    {
        return true;
    }

    @Override
    public void CleanUp()
    {

    }
}
