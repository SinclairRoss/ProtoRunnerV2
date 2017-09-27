package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.Particle;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter;
import com.raggamuffin.protorunnerv2.particles.Trail;
import com.raggamuffin.protorunnerv2.particles.TrailEmitter;
import com.raggamuffin.protorunnerv2.particles.TrailNode;

import java.util.ArrayList;

public class ParticleManager
{
    private ArrayList<Particle> m_ActiveParticles;
    private ArrayList<Particle> m_InvalidParticles;

	private ArrayList<TrailNode> m_InvalidTrailParticles;
	
	private GameLogic m_Game;
	
	public ParticleManager(GameLogic Game)
	{
		m_Game = Game;

        m_ActiveParticles = new ArrayList<>();
        m_InvalidParticles = new ArrayList<>();

        m_InvalidTrailParticles = new ArrayList<>();
	}

	public void Update(double deltaTime)
	{
        for (int i = 0; i < m_ActiveParticles.size();)
        {
            Particle particle = m_ActiveParticles.get(i);

            particle.Update(deltaTime);

            if (!particle.IsValid())
            {
                m_ActiveParticles.remove(particle);
                m_InvalidParticles.add(particle);
            }
            else
            {
                ++i;
            }
        }
	}

    public void CreateParticle(ParticleEmitter origin)
    {
        synchronized (m_Game.GetPacket().GetParticleMutex())
        {
            Particle newParticle;

            if(m_InvalidParticles.isEmpty())
            {
                newParticle = new Particle();
            }
            else
            {
                newParticle = m_InvalidParticles.remove(m_InvalidParticles.size() - 1);
            }

            newParticle.Activate(origin.CalculateSpawnPoint(), origin.GetVelocity(), origin.CalculateParticleForward(), origin.GetEmissionForce(), origin.GetInitialColour(), origin.GetFinalColour(), origin.GetLifeSpan());

            m_ActiveParticles.add(newParticle);
        }
    }

    public ArrayList<Particle> GetActiveParticles() { return m_ActiveParticles; }

    public TrailNode CreateTrailPoint(TrailEmitter origin)
    {
        TrailNode newParticle;

        // Check to see if there are any particles ready to be recycled.
        if(m_InvalidTrailParticles.isEmpty())
        {
            newParticle = new TrailNode();
        }
        else
        {
            newParticle = m_InvalidTrailParticles.remove(m_InvalidTrailParticles.size() - 1);
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
}
