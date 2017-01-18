package com.raggamuffin.protorunnerv2.managers;

import java.util.ArrayList;
import java.util.Iterator;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter;
import com.raggamuffin.protorunnerv2.particles.Particle_Multiplier;
import com.raggamuffin.protorunnerv2.particles.Particle_Standard;
import com.raggamuffin.protorunnerv2.particles.TrailEmitter;
import com.raggamuffin.protorunnerv2.particles.TrailNode;

public class ParticleManager
{
    private ArrayList<Particle_Standard> m_ActiveParticles;
    private ArrayList<Particle_Standard> m_InvalidParticles;

    private ArrayList<Particle_Multiplier> m_ActiveParticles_Multiplier;
    private ArrayList<Particle_Multiplier> m_InvalidParticles_Mulitplier;

	private ArrayList<TrailNode> m_ActiveTrailParticles;
	private ArrayList<TrailNode> m_InvalidTrailParticles;
	
	private GameLogic m_Game;
	
	public ParticleManager(GameLogic Game)
	{
		m_Game = Game;

        m_ActiveParticles = new ArrayList<>();
        m_InvalidParticles = new ArrayList<>();

        m_ActiveParticles_Multiplier = new ArrayList<>();
        m_InvalidParticles_Mulitplier = new ArrayList<>();

        for(int i = 0; i < 100; ++i)
        {
            m_InvalidParticles.add(new Particle_Standard());
            m_InvalidParticles_Mulitplier.add(new Particle_Multiplier(m_Game));
        }

        m_ActiveTrailParticles = new ArrayList<>();
        m_InvalidTrailParticles = new ArrayList<>();
	}
	
	public void Update(double deltaTime)
	{
        for(Iterator<Particle_Standard> iter = m_ActiveParticles.iterator(); iter.hasNext();)
        {
            Particle_Standard particle = iter.next();

            if(particle.IsValid())
            {
                particle.Update(deltaTime);
            }
            else
            {
                particle.OnInvalidation();

                m_InvalidParticles.add(particle);
                m_Game.RemoveParticleFromRenderer(particle);
                iter.remove();
            }
        }

        for(Iterator<Particle_Multiplier> iter = m_ActiveParticles_Multiplier.iterator(); iter.hasNext();)
        {
            Particle_Multiplier particle = iter.next();

            if(particle.IsValid())
            {
                particle.Update(deltaTime);
            }
            else
            {
                particle.OnInvalidation();

                m_InvalidParticles_Mulitplier.add(particle);
                m_Game.RemoveParticleFromRenderer(particle);
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

    public Particle_Standard CreateParticle(ParticleEmitter origin)
    {
        Particle_Standard newParticle;

        // Check to see if there are any particles ready to be recycled.
        if (m_InvalidParticles.size() > 0)
        {
            int finalIndex = m_InvalidParticles.size() - 1;
            newParticle = m_InvalidParticles.get(finalIndex);
            m_InvalidParticles.remove(newParticle);
        }
        else
        {
            newParticle = new Particle_Standard();
        }

        newParticle.Activate(origin.CalculateSpawnPoint(), origin.GetVelocity(), origin.CalculateParticleForward(), origin.GetEmissionForce(), origin.GetInitialColour(), origin.GetFinalColour(), origin.GetLifeSpan());
        m_ActiveParticles.add(newParticle);

        m_Game.AddParticleToRenderer(newParticle);

        return newParticle;
    }

    public Particle_Multiplier CreateParticleMultiplier(ParticleEmitter origin)
    {
        Particle_Multiplier newParticle;

        // Check to see if there are any particles ready to be recycled.
        if (m_InvalidParticles_Mulitplier.size() > 0)
        {
            int finalIndex = m_InvalidParticles_Mulitplier.size() - 1;
            newParticle = m_InvalidParticles_Mulitplier.get(finalIndex);
            m_InvalidParticles_Mulitplier.remove(newParticle);
        }
        else
        {
            newParticle = new Particle_Multiplier(m_Game);
        }

        newParticle.Activate(origin.CalculateSpawnPoint(), origin.GetVelocity(), origin.CalculateParticleForward(), origin.GetEmissionForce(), origin.GetInitialColour(), origin.GetFinalColour(), origin.GetLifeSpan());
        m_ActiveParticles_Multiplier.add(newParticle);

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

    public ArrayList<Particle_Multiplier> GetMultiplierParticles()
    {
        return m_ActiveParticles_Multiplier;
    }
}
