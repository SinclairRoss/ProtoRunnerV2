package com.raggamuffin.protorunnerv2.managers;

// Author: Sinclair Ross
// Date:   17/01/2017

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.Particle;
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
        m_AttractionStrength = 10;

        m_Position = position;
        m_ParticleForceDirection = new Vector3();

        m_Game = game;

        MultiplierCollectedPublisher = m_Game.GetPubSubHub().CreatePublisher(PublishedTopics.MultiplierCollected);
    }

    public void Update()
    {
        ArrayList<Particle> particles = m_Game.GetParticleManager().GetMultiplierParticles();

        int numParticles = particles.size();
        for(int i = 0; i < numParticles; ++i)
        {
            Particle particle = particles.get(i);

            double distanceSqr = Vector3.GetDistanceBetweenSqr(m_Position, particle.GetPosition());
            AttractParticles(particle, distanceSqr);
            CollectParticles(particle, distanceSqr);
        }
    }

    private void AttractParticles(Particle particle, double distanceSqr)
    {
        if(distanceSqr < m_AttractionRange * m_AttractionRange)
        {
            m_ParticleForceDirection.SetAsDifference(particle.GetPosition(), m_Position);
            m_ParticleForceDirection.Normalise();

            particle.ApplyForce(m_ParticleForceDirection, m_AttractionStrength);
        }
    }

    private void CollectParticles(Particle particle, double distanceSqr)
    {
        if(distanceSqr < m_CollectionRange * m_CollectionRange)
        {
            particle.ForceInvalidation();
            MultiplierCollectedPublisher.Publish();
        }
    }
}
