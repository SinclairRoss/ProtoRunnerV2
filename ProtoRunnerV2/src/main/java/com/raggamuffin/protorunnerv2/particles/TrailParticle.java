package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class TrailParticle extends Particle
{
	private ParticleEmitter m_Origin;
	private TrailParticle m_Parent;
	private Vector3 m_EndPoint;
	private Vector3 m_North;

	private Colour m_EndPointColour;
	
	public TrailParticle(ParticleEmitter origin) 
	{
		super();

		m_Origin = origin;
		m_Parent = null;
		m_EndPoint = new Vector3();
		m_North = new Vector3(0,0,1);
		
		m_EndPointColour = new Colour();
	}
	
	@Override
	public void Update(double deltaTime)
	{		
		super.Update(deltaTime);
		
		if(m_Parent == null)
			CalculateLineDimentions();	
		else
			m_EndPointColour.SetColour(m_Parent.GetColour());
	}
	
	@Override
	public void Activate(ParticleEmitter origin)
	{
		super.Activate(origin);
		
		m_Scale.SetVector(0.0);
		m_Origin = origin;
		m_Parent = null;
	}

	@Override
	public boolean IsValid() 
	{
		if(HasParent())
			return m_Parent.IsAlive();
		else 
			return IsAlive();
	}
	
	public boolean IsAlive()
	{
		return !m_DissipationTimer.TimedOut();
	}
	
	public boolean HasParent()
	{
		return m_Parent != null;
	}
	
	public Colour GetEndPointColour()
	{
		return m_EndPointColour;
	}
	
	public TrailParticle GetParent()
	{
		return m_Parent;
	}
	
	public void SetParent(TrailParticle parent)
	{
		m_Parent = parent;
		
		CalculateLineDimentions();
	}

	@Override
	public ParticleType GetParticleType() 
	{
		return ParticleType.Trail;
	}
	
	private void CalculateLineDimentions()
	{
		m_EndPoint.SetVectorDifference(m_Position, m_Origin.GetPosition());
		double length = m_EndPoint.GetLength();
		m_Scale.SetVector(length);
		
		m_Orientation = Vector3.RadiansBetween(m_North, m_EndPoint);
		
		if(Vector3.Determinant(m_North, m_EndPoint) > 0)
			m_Orientation *= -1;
	}
}
