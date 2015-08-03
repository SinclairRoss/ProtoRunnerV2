package com.raggamuffin.protorunnerv2.tutorial;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.Vehicles.Runner;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.weapons.WeaponSlot;

public class TutorialCondition_SwitchWeapon extends TutorialCondition
{
    private WeaponSlot m_Slot;
    private boolean m_Active;
    private boolean m_CondtionComplete;

    public TutorialCondition_SwitchWeapon(GameLogic game, String message, WeaponSlot slot, TutorialEffect... effects)
    {
        super(game, message, OptionalElement.None, effects);

        m_Slot = slot;
        m_Active = false;
        m_CondtionComplete = false;

        PubSubHub pubSub = game.GetPubSubHub();
        pubSub.SubscribeToTopic(PublishedTopics.PlayerSwitchedWeapon, new PlayerSwitchedWeaponSubscriber());
    }

    @Override
    public boolean ConditionComplete()
    {
        return m_CondtionComplete;
    }

    @Override
    public double GetProgress()
    {
        return m_CondtionComplete ? 1 : 0;
    }

    @Override
    public void Initialise()
    {
        super.Initialise();
        m_Active = true;
    }

    @Override
    public void Update(double deltaTime)
    {

    }

    @Override
    public void Reset()
    {
        super.Reset();

        m_Active = false;
        m_CondtionComplete = false;
    }

    private class PlayerSwitchedWeaponSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            if(!m_Active)
                return;

            Runner player = m_Game.GetVehicleManager().GetPlayer();

            if(player == null)
                return;

            if(player.GetWeaponSlot() != m_Slot)
                return;

            m_CondtionComplete = true;
        }
    }
}
