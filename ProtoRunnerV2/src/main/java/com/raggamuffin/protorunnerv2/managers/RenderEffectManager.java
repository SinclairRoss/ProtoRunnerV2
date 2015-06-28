package com.raggamuffin.protorunnerv2.managers;


import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Runner;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.master.RenderEffectSettings;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.weapons.WeaponSlot;

public class RenderEffectManager
{
	private final double BASE_GLOW_INTENSITY   = 1.5;
	private final double MAX_GLOW_INTENSITY    = 4.0;
	private final double GLOW_DECAY_MULTIPLIER = 4.0;
	
	private double m_GlowIntensity;
	
	private RenderEffectSettings m_RenderEffectSettings;
    private VehicleManager m_VehicleManager;

    private double m_SkyboxColourCounter;
    private double m_SkyboxColourCounterMultiplier;
    private Colour m_SkyboxColour;
    private Colour m_SkyboxColourPrevious;
    private Colour m_SkyboxColourNext;
	
	public RenderEffectManager(GameLogic Game, RenderEffectSettings Settings) 
	{
		m_RenderEffectSettings = Settings;
        m_VehicleManager = Game.GetVehicleManager();
	
		m_GlowIntensity = BASE_GLOW_INTENSITY;

        m_SkyboxColourCounter = 0.0;
        m_SkyboxColourCounterMultiplier = 0.25;
        m_SkyboxColour = new Colour(Colours.White);
        m_SkyboxColourPrevious = new Colour(Colours.White);
        m_SkyboxColourNext = new Colour(Colours.RunnerBlue);

        PubSubHub pubSub = Game.GetPubSubHub();
        pubSub.SubscribeToTopic(PublishedTopics.PlayerHit, new PlayerHitSubscriber());
        pubSub.SubscribeToTopic(PublishedTopics.PlayerSwitchedWeapon, new WeaponChangedSubscriber());
    }

	public void Update(double deltaTime)
	{
		UpdateGlow(deltaTime);
        UpdateSkybox(deltaTime);
	}

    private void UpdateGlow(double deltaTime)
    {
        m_GlowIntensity -= deltaTime * GLOW_DECAY_MULTIPLIER;
        m_GlowIntensity = MathsHelper.Clamp(m_GlowIntensity, BASE_GLOW_INTENSITY, MAX_GLOW_INTENSITY);

        m_RenderEffectSettings.SetGlowIntensity(m_GlowIntensity);
    }

    private void UpdateSkybox(double deltaTime)
    {
        m_SkyboxColourCounter += (deltaTime * m_SkyboxColourCounterMultiplier);
        m_SkyboxColourCounter = MathsHelper.Clamp(m_SkyboxColourCounter, 0, 1.0);

        m_SkyboxColour.Red      = MathsHelper.Lerp(m_SkyboxColourCounter, m_SkyboxColourPrevious.Red,   m_SkyboxColourNext.Red);
        m_SkyboxColour.Green    = MathsHelper.Lerp(m_SkyboxColourCounter, m_SkyboxColourPrevious.Green, m_SkyboxColourNext.Green);
        m_SkyboxColour.Blue     = MathsHelper.Lerp(m_SkyboxColourCounter, m_SkyboxColourPrevious.Blue,  m_SkyboxColourNext.Blue);

        m_RenderEffectSettings.SetSkyboxColour(m_SkyboxColour);
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

    private class WeaponChangedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            Runner player = m_VehicleManager.GetPlayer();

            if(player != null)
            {
                m_SkyboxColourCounter = 0.0;
                m_SkyboxColourPrevious = m_SkyboxColour;
                m_SkyboxColourNext = player.GetPrimaryWeapon().GetColour();
            }
        }
    }
}