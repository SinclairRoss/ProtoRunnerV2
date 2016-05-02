package com.raggamuffin.protorunnerv2.gamelogic;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;

public class InGameSoundEffectsManager
{
    GameAudioManager m_GameAudioManager;

    public InGameSoundEffectsManager(GameLogic game)
    {
        m_GameAudioManager = game.GetGameAudioManager();
        game.GetPubSubHub().SubscribeToTopic(PublishedTopics.EnemyDestroyed, new EnemyDestroyedSubscriber());
    }

    private class EnemyDestroyedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_GameAudioManager.PlaySound(AudioClips.EnemyDestroyed);
        }
    }
}
