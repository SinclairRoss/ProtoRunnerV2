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
    private final double BASE_FILMGRAIN_INTENSITY = 0.05;
    private final double MAX_FILMGRAIN_INTENSITY = 0.1;
    private final double DECAY_MULTIPLIER = 4.0;
	
	private double m_GlowIntensity;
	private double m_FilmGrainIntensity;

	private RenderEffectSettings m_RenderEffectSettings;

	public RenderEffectManager(GameLogic Game, RenderEffectSettings Settings) 
	{
		m_RenderEffectSettings = Settings;
        m_RenderEffectSettings.SetSkyboxColour(Game.GetColourManager().GetPrimaryColour());

		m_GlowIntensity = BASE_GLOW_INTENSITY;
        m_RenderEffectSettings.SetGlowIntensity(m_GlowIntensity);

        m_FilmGrainIntensity = BASE_FILMGRAIN_INTENSITY;
        m_RenderEffectSettings.SetFilmGrainIntensity(m_FilmGrainIntensity);

        PubSubHub pubSub = Game.GetPubSubHub();
        pubSub.SubscribeToTopic(PublishedTopics.PlayerHit, new PlayerHitSubscriber());
    }

	public void Update(double deltaTime)
	{
		UpdateGlow(deltaTime);
	}

    private void UpdateGlow(double deltaTime)
    {
        m_GlowIntensity -= deltaTime * DECAY_MULTIPLIER;
        m_GlowIntensity = MathsHelper.Clamp(m_GlowIntensity, BASE_GLOW_INTENSITY, MAX_GLOW_INTENSITY);
        m_RenderEffectSettings.SetGlowIntensity(m_GlowIntensity);

        m_FilmGrainIntensity -= deltaTime * DECAY_MULTIPLIER;
        m_FilmGrainIntensity = MathsHelper.Clamp(m_FilmGrainIntensity, BASE_FILMGRAIN_INTENSITY, MAX_FILMGRAIN_INTENSITY);
		m_RenderEffectSettings.SetFilmGrainIntensity(m_FilmGrainIntensity);
    }

	private class PlayerHitSubscriber extends Subscriber
	{
		@Override
		public void Update(final int args)
		{
			m_GlowIntensity = MAX_GLOW_INTENSITY;
			m_GlowIntensity = MathsHelper.Clamp(m_GlowIntensity, BASE_GLOW_INTENSITY, MAX_GLOW_INTENSITY);

            m_FilmGrainIntensity = MAX_FILMGRAIN_INTENSITY;
            m_FilmGrainIntensity = MathsHelper.Clamp(m_FilmGrainIntensity, BASE_FILMGRAIN_INTENSITY, MAX_FILMGRAIN_INTENSITY);
		}
	}
}