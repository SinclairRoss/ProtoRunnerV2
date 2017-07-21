package com.raggamuffin.protorunnerv2.managers;

import android.util.Log;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.Particle;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter;
import com.raggamuffin.protorunnerv2.particles.ParticleType;
import com.raggamuffin.protorunnerv2.particles.Particle_Multiplier;
import com.raggamuffin.protorunnerv2.particles.Particle_Standard;
import com.raggamuffin.protorunnerv2.particles.Trail;
import com.raggamuffin.protorunnerv2.particles.TrailEmitter;
import com.raggamuffin.protorunnerv2.particles.TrailNode;

import java.util.ArrayList;

public class ParticleManager
{
    private ArrayList<Particle> m_ActiveParticles;
    private ArrayList<Particle> m_InvalidParticles;
    private ArrayList<Particle> m_NewParticles;
    private ArrayList<Particle> m_OldParticles;

    private ArrayList<Particle> m_ActiveParticles_Multiplier;
    private ArrayList<Particle> m_InvalidParticles_Multiplier;
    private ArrayList<Particle> m_NewParticles_Multiplier;
    private ArrayList<Particle> m_OldParticles_Multiplier;

	private ArrayList<TrailNode> m_InvalidTrailParticles;
	
	private GameLogic m_Game;
	
	public ParticleManager(GameLogic Game)
	{
		m_Game = Game;

        m_ActiveParticles = new ArrayList<>();
        m_InvalidParticles = new ArrayList<>();
        m_NewParticles = new ArrayList<>();
        m_OldParticles = new ArrayList<>();

        m_ActiveParticles_Multiplier = new ArrayList<>();
        m_InvalidParticles_Multiplier = new ArrayList<>();
        m_NewParticles_Multiplier = new ArrayList<>();
        m_OldParticles_Multiplier = new ArrayList<>();

        m_InvalidTrailParticles = new ArrayList<>();
	}
	
	public void Update(double deltaTime)
	{
        if(m_NewParticles.size() > 0)
        {
            m_ActiveParticles.addAll(m_NewParticles);
            m_Game.AddParticleToRenderer(m_NewParticles, ParticleType.Standard);
            m_NewParticles.clear();
        }

        int numParticles = m_ActiveParticles.size();
        for(int i = 0; i < numParticles; ++i)
        {
            Particle particle = m_ActiveParticles.get(i);

            if(particle.IsValid())
            {
                particle.Update(deltaTime);
            }
            else
            {
                particle.OnInvalidation();

                m_InvalidParticles.add(particle);
                m_OldParticles.add(particle);
                m_ActiveParticles.remove(i);

                --i;
                --numParticles;
            }
        }

        if(m_OldParticles.size() > 0)
        {
            m_Game.RemoveParticleFromRenderer(m_OldParticles, ParticleType.Standard);
            m_OldParticles.clear();
        }

        if(m_NewParticles_Multiplier.size() > 0)
        {
            m_ActiveParticles_Multiplier.addAll(m_NewParticles_Multiplier);
            m_Game.AddParticleToRenderer(m_NewParticles_Multiplier, ParticleType.Multiplier);
            m_NewParticles_Multiplier.clear();
        }

        numParticles = m_ActiveParticles_Multiplier.size();
        for(int i = 0; i < m_ActiveParticles_Multiplier.size(); ++i)
        {
            Particle particle = m_ActiveParticles_Multiplier.get(i);

            if(particle.IsValid())
            {
                particle.Update(deltaTime);
            }
            else
            {
                particle.OnInvalidation();

                m_InvalidParticles_Multiplier.add(particle);
                m_OldParticles_Multiplier.add(particle);
                m_ActiveParticles_Multiplier.remove(i);

                --i;
                --numParticles;
            }
        }

        if(m_OldParticles_Multiplier.size() > 0)
        {
            m_Game.RemoveParticleFromRenderer(m_OldParticles_Multiplier, ParticleType.Multiplier);
            m_OldParticles_Multiplier.clear();
        }
	}

    public Particle CreateParticle(ParticleEmitter origin)
    {
        Particle newParticle;

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

        if(newParticle == null)
        {
            newParticle = new Particle_Standard();
        }

        newParticle.Activate(origin.CalculateSpawnPoint(), origin.GetVelocity(), origin.CalculateParticleForward(), origin.GetEmissionForce(), origin.GetInitialColour(), origin.GetFinalColour(), origin.GetLifeSpan());
        m_NewParticles.add(newParticle);

        return newParticle;
    }

    public Particle CreateParticleMultiplier(ParticleEmitter origin)
    {
        Particle newParticle;

        // Check to see if there are any particles ready to be recycled.
        if (m_InvalidParticles_Multiplier.size() > 0)
        {
            int finalIndex = m_InvalidParticles_Multiplier.size() - 1;
            newParticle = m_InvalidParticles_Multiplier.get(finalIndex);
            m_InvalidParticles_Multiplier.remove(newParticle);
        }
        else
        {
            newParticle = new Particle_Multiplier(m_Game);
        }

        newParticle.Activate(origin.CalculateSpawnPoint(), origin.GetVelocity(), origin.CalculateParticleForward(), origin.GetEmissionForce(), origin.GetInitialColour(), origin.GetFinalColour(), origin.GetLifeSpan());
        m_NewParticles_Multiplier.add(newParticle);

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

        Trail originTrail = origin.GetTrail();

        TrailNode headNode = originTrail.GetHeadNode();

        if(headNode != null)
        {
            headNode.SetChild(newParticle);
        }

        newParticle.Activate(origin.GetPosition(), origin.GetLifeSpan(), origin.GetFadeInLength(), headNode, origin.GetHotColour(), origin.GetColdColour());

        return newParticle;
    }

    public void RecycleTrailNode(TrailNode node)
    {
        m_InvalidTrailParticles.add(node);
    }

    public ArrayList<Particle> GetMultiplierParticles()
    {
        return m_ActiveParticles_Multiplier;
    }
}
