package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;

public class Graviton extends GameObject
{
	GameObject m_Anchor;
	private double m_Pull;
	private GravitonBehaviour m_Behaviour;
	
	public Graviton(double pull, GravitonBehaviourType behaviour) 
	{
		super(null, null);

		m_Pull = pull;
		m_Behaviour = GetBehaviourFromType(behaviour);
	}
	
	@Override
	public void Update(double deltaTime)
	{
		
	}

	@Override
	public boolean IsValid() 
	{
		return m_Anchor.IsValid();
	}

	public double GetPull()
	{
		return m_Pull;
	}
	
	public GravitonBehaviour GetGravitonBehaviour()
	{
		return m_Behaviour;
	}
	
	private GravitonBehaviour GetBehaviourFromType(GravitonBehaviourType type)
	{
		switch(type)
		{
			case Attract:
				return new GravitonBehaviour_Attract(this);
				
			case Repel:
				return new GravitonBehaviour_Repel(this);
			
			default:
				return null;
		}
	}
}