package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.pubsub.InternalPubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.InternalTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class BraveryFactor_DamageTaken extends BraveryFactor
{
    private double m_PanicRate;
    private double m_RecoveryRate;
    private double m_Bravery;

    public BraveryFactor_DamageTaken(InternalPubSubHub pubSub, AIPersonalityAttributes attributes)
    {
        m_PanicRate = attributes.GetHitPanicRate();
        m_RecoveryRate = attributes.GetPanicRecoveryRate();
        m_Bravery = 0.0;

        pubSub.SubscribeToTopic(InternalTopics.DamageTaken, new DamageTakenSubscriber());
    }

    @Override
    public double GetBraveryModifier(double deltaTime)
    {
        double rate = m_RecoveryRate * deltaTime;
        m_Bravery += -m_Bravery >= 0.0 ? rate : -rate;

        return MathsHelper.Clamp(m_Bravery, -1.0, 1.0);
    }

    private class DamageTakenSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_Bravery -= m_PanicRate;
        }
    }
}
