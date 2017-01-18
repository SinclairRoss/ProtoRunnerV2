package com.raggamuffin.protorunnerv2.managers;

// Author: Sinclair Ross
// Date:   17/01/2017

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.Particle_Multiplier;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.ArrayList;

public class MultiplierHoover
{
    private final double m_AttractionRange;
    private final double m_CollectionRange;
    private final double m_AttractionStrength;

    private final Vector3 m_Position;
    private Vector3 m_ParticleForceDirection;
    private final GameLogic m_Game;

    private Publisher MultiplierCollectedPublisher;

    public MultiplierHoover(Vector3 position, GameLogic game)
    {
        m_AttractionRange = 10.0;
        m_CollectionRange = 3.0;
        m_AttractionStrength = 20;

        m_Position = position;
        m_ParticleForceDirection = new Vector3();

        m_Game = game;

        MultiplierCollectedPublisher = m_Game.GetPubSubHub().CreatePublisher(PublishedTopics.MultiplierCollected);
    }

    public void Update()
    {
        ArrayList<Particle_Multiplier> particles = m_Game.GetParticleManager().GetMultiplierParticles();

        for(Particle_Multiplier particle : particles)
        {
            double distanceSqr = Vector3.GetDistanceBetweenSqr(m_Position, particle.GetPosition());
            AttractParticles(particle, distanceSqr);
            CollectParticles(particle, distanceSqr);
        }
    }

    private void AttractParticles(Particle_Multiplier particle, double distanceSqr)
    {
        if(distanceSqr < m_AttractionRange * m_AttractionRange)
        {
            m_ParticleForceDirection.SetVectorDifference(particle.GetPosition(), m_Position);
            m_ParticleForceDirection.Normalise();

            particle.ApplyForce(m_ParticleForceDirection, m_AttractionStrength);
        }
    }

    private void CollectParticles(Particle_Multiplier particle, double distanceSqr)
    {
        if(distanceSqr < m_CollectionRange * m_CollectionRange)
        {
            particle.Collected();
            MultiplierCollectedPublisher.Publish();
        }
    }
}
