package com.raggamuffin.protorunnerv2.gamelogic;

// Author: Sinclair Ross
// Date:   17/01/2017

import android.util.Log;

import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.utils.Timer;

public class MultiplierController
{
    private enum MultiplierState
    {
        Okay,
        Draining
    }

    MultiplierState m_MultiplierState;

    private Timer m_StartDrainTimer;

    private double m_Multiplier;
    private double m_DrainRate;

    private Publisher m_MultiplierIncreasedPublisher;
    private Publisher m_MultiplierDecreasedPublisher;

    private boolean m_On;

    public MultiplierController(GameLogic game)
    {
        m_MultiplierState = MultiplierState.Okay;

        m_Multiplier = 0;

        m_StartDrainTimer = new Timer(3.0);
        m_DrainRate = 10.0;

        m_MultiplierIncreasedPublisher = game.GetPubSubHub().CreatePublisher(PublishedTopics.MultiplierIncreased);
        m_MultiplierDecreasedPublisher = game.GetPubSubHub().CreatePublisher(PublishedTopics.MultiplierDecreased);
        game.GetPubSubHub().SubscribeToTopic(PublishedTopics.MultiplierCollected, new MultiplierCollectedSubscriber());

        m_On = true;
    }

    public void Start()
    {
        m_On = true;
        m_Multiplier = 0;

        StartState_Okay();
    }

    public void Stop()
    {
        m_On = false;

        m_StartDrainTimer.Stop();
    }

    public void Update(double deltaTime)
    {
        if(m_On)
        {
            switch (m_MultiplierState)
            {
                case Okay:
                {
                    if (m_StartDrainTimer.HasElapsed())
                    {
                        StartState_Draining();
                    }
                    break;
                }
                case Draining:
                {
                    m_Multiplier -= m_DrainRate * deltaTime;
                    if(m_Multiplier < 0)
                    {
                        m_Multiplier = 0;
                    }

                    break;
                }
            }
        }
    }

    private void StartState_Okay()
    {
        m_StartDrainTimer.Start();
        m_MultiplierState = MultiplierState.Okay;
    }

    private void StartState_Draining()
    {
        m_MultiplierDecreasedPublisher.Publish();
        m_MultiplierState = MultiplierState.Draining;
    }

    public int GetMultiplier()
    {
        return (int)m_Multiplier;
    }

    private class MultiplierCollectedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            if(m_On)
            {
                ++m_Multiplier;
                StartState_Okay();

                m_MultiplierIncreasedPublisher.Publish();
            }
        }
    }
}
