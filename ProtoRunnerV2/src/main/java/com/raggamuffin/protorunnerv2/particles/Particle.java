package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.colours.ColourBehaviour.ActivationMode;
import com.raggamuffin.protorunnerv2.colours.ColourBehaviour_LerpTo;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;

public abstract class Particle extends GameObject
{
	protected Timer m_DissipationTimer;
	private ColourBehaviour_LerpTo m_ColourBehaviour;
	
	protected double m_FadeIn;
    protected double m_FadeOut;
	
	public Particle()
	{
		super(null, null);
		
		m_Mass = 100.0;
		
		m_Model = ModelType.StandardPoint;
		m_BoundingRadius = 1.0;
		m_DissipationTimer = new Timer(5);
		m_ColourBehaviour = new ColourBehaviour_LerpTo(this, ActivationMode.Continuous);
		AddColourBehaviour(m_ColourBehaviour);

		m_DragCoefficient = 0.0;
		
		m_FadeIn = 0.6;
		m_FadeOut = 0.7;
		
		m_BaseColour.SetColour(Colours.Black);
	}
	
	public void Update(double deltaTime) 
	{	
		m_DissipationTimer.Update(deltaTime);
		m_ColourBehaviour.SetIntensity(m_DissipationTimer.GetProgress());

		HandleFade();
		
		super.Update(deltaTime);
	}
	
	private void HandleFade()
	{
		double normLifeSpan = m_DissipationTimer.GetProgress();
		double alpha = 1.0;
		
		if(normLifeSpan <= m_FadeIn)
			alpha = MathsHelper.Normalise(normLifeSpan, 0, m_FadeIn);

		if(normLifeSpan >= m_FadeOut)
			alpha = 1.0 - MathsHelper.Normalise(normLifeSpan, m_FadeOut, 1.0);
		
		m_BaseColour.Alpha = alpha;
	}
	
	public void Activate(ParticleEmitter origin)
	{
		m_Position.SetVector(origin.GetPosition());
		m_Velocity.SetVector(origin.GetVelocity());
		m_Forward.SetVector(origin.CalculateParticleForward());

        m_Scale.I = origin.GetParticleSize();
		
		ApplyForce(m_Forward, origin.GetEmissionForce());
		
		m_Model = origin.GetParticleModel();
		SetBaseColour(origin.GetStartColour());
		
		m_DissipationTimer.SetLimit(origin.GetLifeSpan());
		m_DissipationTimer.ResetTimer();
	}
	
	public abstract ParticleType GetParticleType();
}
