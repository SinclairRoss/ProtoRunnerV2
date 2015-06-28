package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.utils.Spring1;

public class ProjectileBehaviour_PanicSwitch extends ProjectileBehaviour
{
    private enum PanicSwitchState
    {
        InitalExpansion,
        Contraction,
        FinalExpansion
    }

    private final double INITIAL_EXPANSION_THRESHOLD = 5.0;
    private final double CONTRACTION_THRESHOLD = 2.0;
    private final double FINAL_EXPANSION_THRESHOLD = 100.0;

    private PanicSwitchState m_State;
    private Weapon m_FiringWeapon;
    private double m_Scale;
    private Spring1 m_Spring;

    private PubSubHub m_PubSub;
    private Publisher m_PanicSwitchFiredPublisher;
    private Publisher m_PanicSwitchDepletedPublisher;

    public ProjectileBehaviour_PanicSwitch(Projectile anchor, PubSubHub pubSub)
    {
        super(anchor);

        m_State = PanicSwitchState.InitalExpansion;

        m_FiringWeapon = anchor.GetFiringWeapon();

        m_Scale = 0.0;
        m_Spring = new Spring1(1.0, 0.16 ,1);

        m_Anchor.SetBoundingRadius(m_Scale);
        m_Anchor.SetScale(m_Scale);

        m_PubSub = pubSub;
        m_PanicSwitchFiredPublisher     = m_PubSub.CreatePublisher(PublishedTopics.PanicSwitchFired);
        m_PanicSwitchDepletedPublisher  = m_PubSub.CreatePublisher(PublishedTopics.PanicSwitchDepleted);

        m_PanicSwitchFiredPublisher.Publish();
    }

    public void Update(double deltaTime)
    {
        switch(m_State)
        {
            case InitalExpansion:
            {
                m_Spring.SetRelaxedPosition(INITIAL_EXPANSION_THRESHOLD);

                if (m_Scale >= INITIAL_EXPANSION_THRESHOLD)
                    m_State = PanicSwitchState.Contraction;

                break;
            }
            case Contraction:
            {
                m_Spring.SetRelaxedPosition(CONTRACTION_THRESHOLD);

                if (m_Scale <= CONTRACTION_THRESHOLD)
                    m_State = PanicSwitchState.FinalExpansion;

                break;
            }
            case FinalExpansion:
            {
                m_Spring.SetRelaxedPosition(FINAL_EXPANSION_THRESHOLD);

                if (m_Scale >= FINAL_EXPANSION_THRESHOLD)
                    m_Anchor.ForceInvalidation();

                break;
            }
        }

        m_Anchor.SetVelocity(m_FiringWeapon.GetVelocity());

        m_Spring.Update(deltaTime);

        m_Scale = m_Spring.GetPosition();
        m_Anchor.SetBoundingRadius(m_Scale);
        m_Anchor.SetScale(m_Scale);
    }

    @Override
    public void CollisionResponce()
    {
        // Do nothing.
    }

    @Override
    public void CleanUp()
    {
        m_Anchor.SetBoundingRadius(0.0);
        m_PanicSwitchDepletedPublisher.Publish();
    }

    @Override
    public boolean UseSimpleCollisionDetection()
    {
        return true;
    }
}