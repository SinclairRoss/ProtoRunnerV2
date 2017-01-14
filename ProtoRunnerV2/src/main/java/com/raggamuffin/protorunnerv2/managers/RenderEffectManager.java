package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.master.RenderEffectSettings;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer_Accumulation;

public class RenderEffectManager
{
	private final double BASE_GLOW_INTENSITY   = 1.5;
	private final double MAX_GLOW_INTENSITY    = 4.0;
    private final double BASE_FILMGRAIN_INTENSITY = 0.055;
    private final double MAX_FILMGRAIN_INTENSITY = 0.3;
    private final double DAMAGE_EFFECT_DURATION = 1.0;

    private Timer_Accumulation m_DamageEffectTimer;

	private RenderEffectSettings m_RenderEffectSettings;

	public RenderEffectManager(GameLogic Game, RenderEffectSettings Settings) 
	{
		m_RenderEffectSettings = Settings;
        m_RenderEffectSettings.SetSkyboxColour(Game.GetColourManager().GetPrimaryColour());

        m_RenderEffectSettings.SetGlowIntensity(BASE_GLOW_INTENSITY);
        m_RenderEffectSettings.SetFilmGrainIntensity(BASE_FILMGRAIN_INTENSITY);

        m_DamageEffectTimer = new Timer_Accumulation(DAMAGE_EFFECT_DURATION);

        PubSubHub pubSub = Game.GetPubSubHub();
        pubSub.SubscribeToTopic(PublishedTopics.PlayerHit, new PlayerHitSubscriber());
    }

	public void Update(double deltaTime)
	{
		UpdateGlow(deltaTime);
	}

    private void UpdateGlow(double deltaTime)
    {
        m_DamageEffectTimer.Update(deltaTime);
        double intensity = m_DamageEffectTimer.GetInverseProgress();

		double glowIntensity = MathsHelper.Lerp(intensity, BASE_GLOW_INTENSITY, MAX_GLOW_INTENSITY);
        m_RenderEffectSettings.SetGlowIntensity(glowIntensity);

        double filmGrainIntensity = MathsHelper.Lerp(intensity, BASE_FILMGRAIN_INTENSITY, MAX_FILMGRAIN_INTENSITY);
		m_RenderEffectSettings.SetFilmGrainIntensity(filmGrainIntensity);
    }

	private class PlayerHitSubscriber extends Subscriber
	{
		@Override
		public void Update(final int args)
		{
            m_DamageEffectTimer.ResetTimer();
		}
	}
}