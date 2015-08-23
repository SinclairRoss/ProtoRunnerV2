package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class ParticleEmitter extends GameObject
{
	protected GameObject m_Anchor;
	protected ParticleManager m_ParticleManager;

    protected double m_MinSize;
    protected double m_MaxSize;

	protected Vector3 m_ParticleForward;
	protected double m_ParticleLifeSpan;
	protected double m_EmissionForce;
	protected double m_Range;
	
	protected Colour m_ParticleStartColour;
	protected Colour m_ParticleFinalColour;
	
	private EmissionBehaviour m_Behaviour;
	
	protected ModelType m_ParticleModel;

	public ParticleEmitter(GameObject anchor, ParticleManager pManager, EmissionBehaviour behaviour, double lifeSpan)
	{
		super(null, null);
		
		m_Anchor = anchor;
		m_ParticleManager = pManager;

        m_MinSize = 30.0f;
        m_MaxSize = 90.0f;

		m_ParticleForward = new Vector3();
		m_ParticleLifeSpan = lifeSpan;
		m_EmissionForce = 1.0f;
		m_Range = Math.PI * 2;

        m_ParticleStartColour = anchor.GetColour();
        m_ParticleFinalColour = anchor.GetAltColour();

		m_Behaviour = behaviour;

		m_ParticleModel = ModelType.StandardPoint;
		m_Model = ModelType.Nothing;
	}
	
	protected abstract Vector3 CalculateParticleForward();
	
	public void Update(double deltaTime)
	{
		Emit(m_Behaviour.GetEmissionCount(deltaTime));
	}

	protected void Emit(int count)
	{
		for(int i = 0; i < count; i++)
			m_ParticleManager.CreateParticle(this);	
	}
	
	@Override
	public boolean IsValid()
	{
		return true;
	}

    public double GetParticleSize()
    {
        return m_MinSize;
        //return MathsHelper.BiasedRandomDouble(m_MinSize, m_MaxSize, 4);
    }

	///// Getters \\\\\
	public Vector3 GetVelocity()
	{
		return m_Anchor.GetVelocity();
	}
	
	public Colour GetStartColour()
	{
		return m_ParticleStartColour;
	}
	
	public Colour GetFinalColour()
	{
		return m_ParticleFinalColour;
	}
	
	public ModelType GetParticleModel()
	{
		return m_ParticleModel;
	}
	
	public double GetEmissionForce()
	{
		return m_EmissionForce;
	}
	
	public double GetLifeSpan()
	{
		return m_ParticleLifeSpan;
	}
	
	///// Setters \\\\\
	public void SetEmissionForce(double Force)
	{
		m_EmissionForce = Force;
	}
	
	public void SetStartColour(Colour colour)
	{
		m_ParticleStartColour.SetColour(colour);
	}
	
	public void SetFinalColour(Colour colour)
	{
		m_ParticleFinalColour.SetColour(colour);
	}
}
