package com.raggamuffin.protorunnerv2.tutorial;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;

public class TutorialCondition_Reboot extends TutorialCondition
{
    private boolean m_Active;
    private boolean m_ConditionComplete;

    public TutorialCondition_Reboot(GameLogic game, String message, TutorialEffect... effects)
    {
        super(game, message, OptionalElement.None, effects);

        m_Active = false;
        m_ConditionComplete = false;

        m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerSpawned, new PlayerSpawnedSubscriber());
    }


    @Override
    public boolean ConditionComplete()
    {
        return m_ConditionComplete;
    }

    @Override
    public double GetProgress()
    {
        return m_ConditionComplete ? 1 : 0;
    }

    @Override
    public void Update(double deltaTime)
    {

    }

    @Override
    public void Initialise()
    {
        super.Initialise();

        m_Game.GetVehicleManager().GetPlayer().SetHullPoints(0);
        m_Active = true;
    }

    private class PlayerSpawnedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            if(!m_Active)
                return;

            m_ConditionComplete = true;
        }
    }
}
