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
        OK,
        Stable,
        Decreasing
    }

    MultiplierState m_MultiplierState;

    private int m_Multiplier;

    private Timer m_StartDrainTimer;
    private Timer m_DrainRateTimer;

    private Publisher m_MultiplierIncreasedPublisher;
    private Publisher m_MultiplierDecreasedPublisher;

    private boolean m_On;

    public MultiplierController(GameLogic game)
    {
        m_MultiplierState = MultiplierState.OK;

        m_Multiplier = 0;

        m_StartDrainTimer = new Timer(5.0);
        m_DrainRateTimer = new Timer(0.3);

        m_MultiplierIncreasedPublisher = game.GetPubSubHub().CreatePublisher(PublishedTopics.MultiplierIncreased);
        m_MultiplierDecreasedPublisher = game.GetPubSubHub().CreatePublisher(PublishedTopics.MultiplierDecreased);
        game.GetPubSubHub().SubscribeToTopic(PublishedTopics.MultiplierCollected, new MultiplierCollectedSubscriber());

        m_On = true;
    }

    public void Start()
    {
        m_On = true;

        StopMultiplierDrain();
    }

    public void Stop()
    {
        m_On = false;

        m_StartDrainTimer.Stop();
        m_DrainRateTimer.Stop();
    }

    public void Update()
    {
        if(m_On)
        {
            switch (m_MultiplierState)
            {
                case OK:
                {
                    break;
                }
                case Stable:
                {
                    if (m_StartDrainTimer.HasElapsed())
                    {
                        StartMultiplierDrain();
                    }
                    break;
                }
                case Decreasing:
                {
                    if (m_DrainRateTimer.HasElapsed())
                    {
                        m_DrainRateTimer.Start();

                        if(m_Multiplier > 0)
                        {
                            --m_Multiplier;

                            m_MultiplierDecreasedPublisher.Publish();
                        }
                        else
                        {
                            StopMultiplierDrain();
                        }
                    }
                    break;
                }
            }

            Log.e("Fuck Cake", m_MultiplierState.toString());
        }
    }

    private void StopMultiplierDrain()
    {
        m_StartDrainTimer.Stop();
        m_DrainRateTimer.Stop();

        m_MultiplierState = MultiplierState.OK;
    }

    private void StabiliseMultiplierDrain()
    {
        m_StartDrainTimer.Start();
        m_DrainRateTimer.Stop();

        m_MultiplierState = MultiplierState.Stable;
    }

    private void StartMultiplierDrain()
    {
        m_StartDrainTimer.Stop();
        m_DrainRateTimer.Start();

        m_MultiplierState = MultiplierState.Decreasing;
    }

    public int GetMultiplier()
    {
        return m_Multiplier;
    }

    private class MultiplierCollectedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            if(m_On)
            {
                ++m_Multiplier;
                StabiliseMultiplierDrain();

                m_MultiplierIncreasedPublisher.Publish();
            }
        }
    }
}
