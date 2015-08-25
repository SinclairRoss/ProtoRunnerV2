package com.raggamuffin.protorunnerv2.tutorial;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.vehicles.Runner;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.weapons.WeaponSlot;

public class TutorialEvent_WeaponLock extends TutorialEvent
{
    private boolean m_On;
    private WeaponSlot m_LockedSlot;

    public TutorialEvent_WeaponLock(GameLogic game)
    {
        super(game);

        m_On = false;
        m_LockedSlot = WeaponSlot.Left;

        game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerSwitchedWeapon, new PlayerSwitchedWeaponSubscriber());
    }

    @Override
    public void On()
    {
        m_On = true;
    }

    @Override
    public void Off()
    {
        m_On = false;
    }

    @Override
    public void Update()
    {

    }

    public void SetLock(WeaponSlot slot)
    {
        On();
        m_LockedSlot = slot;
    }

    private class PlayerSwitchedWeaponSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            if(!m_On)
                return;

            Runner player = m_Game.GetVehicleManager().GetPlayer();

            if(player == null)
                return;

            if(player.GetWeaponSlot() == m_LockedSlot)
                return;

            player.SelectWeaponBySlot(m_LockedSlot);
        }
    }
}
