package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.master.RenderEffectSettings;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class RenderEffectManager
{
	private final double BASE_GLOW_INTENSITY   = 1.5;
	private final double MAX_GLOW_INTENSITY    = 4.0;
	private final double GLOW_DECAY_MULTIPLIER = 4.0;
	
	private double m_GlowIntensity;
	
	private RenderEffectSettings m_RenderEffectSettings;

	public RenderEffectManager(GameLogic Game, RenderEffectSettings Settings) 
	{
		m_RenderEffectSettings = Settings;
        m_RenderEffectSettings.SetSkyboxColour(Game.GetColourManager().GetPrimaryColour());

		m_GlowIntensity = BASE_GLOW_INTENSITY;

        PubSubHub pubSub = Game.GetPubSubHub();
        pubSub.SubscribeToTopic(PublishedTopics.PlayerHit, new PlayerHitSubscriber());
    }

	public void Update(double deltaTime)
	{
		UpdateGlow(deltaTime);
	}

    private void UpdateGlow(double deltaTime)
    {
        m_GlowIntensity -= deltaTime * GLOW_DECAY_MULTIPLIER;
        m_GlowIntensity = MathsHelper.Clamp(m_GlowIntensity, BASE_GLOW_INTENSITY, MAX_GLOW_INTENSITY);

        m_RenderEffectSettings.SetGlowIntensity(m_GlowIntensity);
    }

	private class PlayerHitSubscriber extends Subscriber
	{
		@Override
		public void Update(final int args)
		{
			m_GlowIntensity += 10.0;
			m_GlowIntensity = MathsHelper.Clamp(m_GlowIntensity, BASE_GLOW_INTENSITY, MAX_GLOW_INTENSITY);
		}
	}
}