package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.colours.ColourBehaviour;
import com.raggamuffin.protorunnerv2.colours.ColourBehaviour_FadeAway;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gameobjects.FloorGrid;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Explosion extends GameObject
{
	private double m_Size;
	private double m_Theta;
	private double m_MaxSize;
	private double m_RateOfExpansion;

	public Explosion(PubSubHub PubSub, GameAudioManager audio, Colour colour, Vector3 position, AffiliationKey affilitation, double maxSize, double rateOfExpansion) 
	{
		super(PubSub, audio);

		m_Model = ModelType.Explosion;
		SetAffiliation(affilitation);
		
		AddChild(new FloorGrid(m_Colour));

		ColourBehaviour_FadeAway fadeBehaviour = new ColourBehaviour_FadeAway(this, ColourBehaviour.ActivationMode.Triggered, 1 / rateOfExpansion);
		AddColourBehaviour(fadeBehaviour);
		fadeBehaviour.TriggerBehaviour();
		
		m_BaseColour.SetColour(colour);
        m_BaseColour.Alpha = 1.0;

		m_Position.SetVector(position);
		m_Size = 0.0;
		m_Theta = 0.0;
		m_MaxSize = maxSize;
		m_RateOfExpansion = rateOfExpansion;
		
		m_GameAudioManager.PlaySound(m_Position, AudioClips.Explosion);	
	}

	@Override
	public void Update(double deltaTime)
	{
		super.Update(deltaTime);
		
		m_Theta += m_RateOfExpansion * deltaTime;
		m_Size = MathsHelper.Lerp(m_Theta, 0, m_MaxSize);
				
		m_Yaw += deltaTime;
		m_Scale.SetVector(m_Size, m_Size * 0.75, m_Size);
		m_BoundingRadius = m_Size;
	}
	
	public double GetDamageOutput(double deltaTime)
	{
		return (1.0 - m_Theta) * (5000.0f * deltaTime);
	}
	
	@Override
	public boolean IsValid() 
	{
		return !(m_Theta >= 1.0 || m_ForciblyInvalidated);
	}

    @Override
    public void CleanUp()
    {

    }
}