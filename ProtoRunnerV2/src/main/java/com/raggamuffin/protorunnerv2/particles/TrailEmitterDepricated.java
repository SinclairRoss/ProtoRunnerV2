package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class TrailEmitterDepricated extends ParticleEmitter
{
	private TrailParticle m_LastParticle;
	private Vector3 m_Offset;
	
	public TrailEmitterDepricated(GameObject anchor, ParticleManager pManager)
	{
		super(anchor, pManager, new EmissionBehaviour_Timed(0.1), 1.0);
		
		m_ParticleForward.SetVector(0,1,0);
		
		m_LastParticle = null;
		
		m_ParticleModel = ModelType.TrailDepricated;
		
		m_Offset = new Vector3();
		m_Offset.Scale(-1.0);

        m_ParticleStartColour = anchor.GetColour();
        m_ParticleFinalColour = anchor.GetColour();
	}
	
	@Override
	public void Emit(int count)
	{
		m_Offset.SetVector(m_Anchor.GetForward());
		m_Offset.Scale(-1.5);
		m_Position.Add(m_Offset);
		
		if(count == 0)
			return; 
		
		TrailParticle newParticle = m_ParticleManager.CreateTrailParticle(this);
		
		if(m_LastParticle != null)
			m_LastParticle.SetParent(newParticle);
		
		m_LastParticle = newParticle;
	}
	
	@Override
	protected Vector3 CalculateParticleForward() 
	{
		return m_ParticleForward;
	}
	
	public Vector3 GetVelocity()
	{
		return m_Velocity;
	}
}
