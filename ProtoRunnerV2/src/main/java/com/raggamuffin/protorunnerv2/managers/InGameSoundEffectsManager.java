package com.raggamuffin.protorunnerv2.managers;

import android.util.Log;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;

public class InGameSoundEffectsManager
{
    GameLogic m_Game;

    public InGameSoundEffectsManager(GameLogic game)
    {
        m_Game = game;
        game.GetPubSubHub().SubscribeToTopic(PublishedTopics.EnemyDestroyed, new EnemyDestroyedSubscriber());
        game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerHit, new PlayerHitSubscriber());
    }

    private class EnemyDestroyedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            if(m_Game.GetGameMode() == GameMode.Play)
            {
                m_Game.GetGameAudioManager().PlaySound(AudioClips.EnemyDestroyed);
            }
        }
    }

    private class PlayerHitSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_Game.GetGameAudioManager().PlaySound(AudioClips.PlayerDamaged);
        }
    }
}
