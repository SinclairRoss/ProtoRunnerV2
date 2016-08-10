package com.raggamuffin.protorunnerv2.managers;

import java.util.ArrayList;
import java.util.Iterator;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.Graviton;
import com.raggamuffin.protorunnerv2.particles.GravitonBehaviourType;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter;
import com.raggamuffin.protorunnerv2.particles.StandardParticle;
import com.raggamuffin.protorunnerv2.particles.TrailEmitter;
import com.raggamuffin.protorunnerv2.particles.TrailNode;

public class ParticleManager
{
	private ArrayList<StandardParticle> m_ActiveParticles;
	private ArrayList<StandardParticle> m_InvalidParticles;

	private ArrayList<TrailNode> m_ActiveTrailParticles;
	private ArrayList<TrailNode> m_InvalidTrailParticles;

	
	private ArrayList<Graviton> m_Gravitons;
	
	private GameLogic m_Game;
	
	public ParticleManager(GameLogic Game)
	{
		m_Game = Game;
		
		m_ActiveParticles = new ArrayList<>();
		m_InvalidParticles = new ArrayList<>();

        m_ActiveTrailParticles = new ArrayList<>();
        m_InvalidTrailParticles = new ArrayList<>();

		m_Gravitons = new ArrayList<>();
	}
	
	public void Update(double deltaTime)
	{		
		for(Iterator<StandardParticle> iter = m_ActiveParticles.iterator(); iter.hasNext();)
		{
			StandardParticle temp = iter.next();
			
			if(temp.IsValid())
			{
				temp.Update(deltaTime);
			}
			else
			{
				m_InvalidParticles.add(temp);
				m_Game.RemoveParticleFromRenderer(temp);
				iter.remove();
			}
		}

        for(Iterator<TrailNode> iter = m_ActiveTrailParticles.iterator(); iter.hasNext();)
        {
            TrailNode temp = iter.next();

            if(temp.IsValid())
            {
                temp.Update(deltaTime);
            }
            else
            {
                temp.CleanUp();
                m_InvalidTrailParticles.add(temp);
                m_Game.RemoveTrailFromRenderer(temp);
				iter.remove();
            }
        }
	}

	public StandardParticle CreateParticle(ParticleEmitter origin)
	{
		StandardParticle newParticle;
		
		// Check to see if there are any particles ready to be recycled.
		if(m_InvalidParticles.size() > 0)
		{
			newParticle = m_InvalidParticles.get(0);
			m_InvalidParticles.remove(newParticle);	
		}
		else
		{
			newParticle = new StandardParticle(m_Gravitons);
		}

		newParticle.Activate(origin);
		m_ActiveParticles.add(newParticle);
		
		m_Game.AddParticleToRenderer(newParticle);
		
		return newParticle;
	}

    public TrailNode CreateTrailPoint(TrailEmitter origin)
    {
        TrailNode newParticle;

        // Check to see if there are any particles ready to be recycled.
        if(m_InvalidTrailParticles.size() > 0)
        {
            newParticle = m_InvalidTrailParticles.get(0);
            m_InvalidTrailParticles.remove(newParticle);
        }
        else
        {
            newParticle = new TrailNode();
        }

        TrailNode headNode = origin.GetHeadNode();

        if(headNode != null)
        {
            m_Game.RemoveTrailFromRenderer(headNode);
            headNode.SetChild(newParticle);
        }

        newParticle.Activate(origin.GetPosition(), origin.GetLifeSpan(), origin.GetFadeInLength(), headNode, origin.GetHotColour(), origin.GetColdColour());
        m_ActiveTrailParticles.add(newParticle);

        m_Game.AddTrailToRenderer(newParticle);

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
