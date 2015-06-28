package com.raggamuffin.protorunnerv2.managers;

import java.util.Iterator;
import java.util.Vector;

import android.util.Log;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.particles.Graviton;
import com.raggamuffin.protorunnerv2.particles.GravitonBehaviourType;
import com.raggamuffin.protorunnerv2.particles.Particle;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter;
import com.raggamuffin.protorunnerv2.particles.ParticleType;
import com.raggamuffin.protorunnerv2.particles.StandardParticle;
import com.raggamuffin.protorunnerv2.particles.TrailParticle;

public class ParticleManager
{
	private Vector<StandardParticle> m_ActiveParticles;
	private Vector<StandardParticle> m_InvalidParticles;
	
	private Vector<TrailParticle> m_ActiveTrailParticles;
	private Vector<TrailParticle> m_InvalidTrailParticles;
	
	private Vector<Graviton> m_Gravitons;
	
	private GameLogic m_Game;
	
	public ParticleManager(GameLogic Game)
	{
		m_Game = Game;
		
		m_ActiveParticles = new Vector<StandardParticle>();
		m_InvalidParticles = new Vector<StandardParticle>();
		
		m_ActiveTrailParticles = new Vector<TrailParticle>();
		m_InvalidTrailParticles = new Vector<TrailParticle>();
		
		m_Gravitons = new Vector<Graviton>();
	}
	
	public void Update(double DeltaTime)
	{		
		for(Iterator<StandardParticle> Iter = m_ActiveParticles.iterator(); Iter.hasNext();)
		{
			StandardParticle temp = Iter.next();
			
			if(temp.IsValid())
			{
				temp.Update(DeltaTime);
			}
			else
			{
				m_InvalidParticles.add(temp);
				m_Game.RemoveObjectFromRenderer(temp);
				Iter.remove();
			}
		}
		
		for(Iterator<TrailParticle> Iter = m_ActiveTrailParticles.iterator(); Iter.hasNext();)
		{
			TrailParticle temp = Iter.next();
			
			if(temp.IsValid())
			{
				temp.Update(DeltaTime);
			}
			else
			{
				m_InvalidTrailParticles.add(temp);
				m_Game.RemoveObjectFromRenderer(temp);
				Iter.remove();
			}
		}
	}

	public StandardParticle CreateParticle(ParticleEmitter origin)
	{
		StandardParticle newParticle = null;
		
		// Check to see if there are any particles ready to be recycled.
		if(m_InvalidParticles.size() > 0)
		{
			newParticle = m_InvalidParticles.elementAt(0);
			m_InvalidParticles.remove(newParticle);	
		}
		else
		{
			newParticle = new StandardParticle(m_Gravitons);
		}

		newParticle.Activate(origin);
		m_ActiveParticles.add(newParticle);
		
		m_Game.AddObjectToRenderer(newParticle);	
		
		return newParticle;
	}
	
	public TrailParticle CreateTrailParticle(ParticleEmitter origin)
	{
		TrailParticle newParticle = null;
		
		// Check to see if there are any particles ready to be recycled.
		if(m_InvalidTrailParticles.size() > 0)
		{
			newParticle = m_InvalidTrailParticles.elementAt(0);		
			m_InvalidTrailParticles.remove(newParticle);	
		}
		else
		{
			newParticle = new TrailParticle(origin);
		}

		newParticle.Activate(origin);
		m_ActiveTrailParticles.add(newParticle);
		
		m_Game.AddObjectToRenderer(newParticle);	
		
		return newParticle;
	}

	public Graviton CreateGraviton(double pull, GravitonBehaviourType type)
	{
		Graviton grav = new Graviton(pull, type);
		m_Gravitons.add(grav);
		return grav;
	}
	
	public void DestroyGraviton(Graviton grav)
	{
		m_Gravitons.remove(grav);
	}
}
