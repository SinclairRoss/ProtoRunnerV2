package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Runner;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.weapons.WeaponSlot;

public class ColourManager
{
    private GameLogic m_Game;

    private Colour m_PrimaryColour;
    private Colour m_AccentingColour;
    private Colour m_AccentTintColour;

    private Colour m_NextColour;
    private Colour m_PreviousColour;

    private double m_Counter;
    private double m_CounterMultiplier;

    public ColourManager(GameLogic game)
    {
        m_Game = game;

        m_PrimaryColour = new Colour();
        m_AccentingColour = new Colour();
        m_AccentTintColour = new Colour();

        m_NextColour = new Colour(Colours.RunnerBlue);
        m_PreviousColour = new Colour();

        m_Counter = 0.0;
        m_CounterMultiplier = 1.0;

        m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerSwitchedWeapon, new PlayerSwitchedWeaponSubscriber());
        m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerSpawned, new PlayerSpawnedSubscriber());
    }

    public void Update(double deltaTime)
    {
        if(m_Counter >= 1.0)
            return;

        m_Counter += (deltaTime * m_CounterMultiplier);
        m_Counter = MathsHelper.Clamp(m_Counter, 0, 1);

        m_PrimaryColour.Red     = MathsHelper.Lerp(m_Counter, m_PreviousColour.Red, m_NextColour.Red);
        m_PrimaryColour.Green   = MathsHelper.Lerp(m_Counter, m_PreviousColour.Green, m_NextColour.Green);
        m_PrimaryColour.Blue    = MathsHelper.Lerp(m_Counter, m_PreviousColour.Blue, m_NextColour.Blue);

        UpdateAccentingColours();
    }

    private void UpdateAccentingColours()
    {
        m_AccentingColour.SetAsInverse(m_PrimaryColour);
        m_AccentTintColour.SetColour(m_AccentingColour);
        m_AccentTintColour.Brighten(-0.6);
    }

    public Colour GetPrimaryColour()
    {
        return m_PrimaryColour;
    }

    public Colour GetAccentingColour()
    {
        return m_AccentingColour;
    }

    public Colour GetAccentTintColour()
    {
        return m_AccentTintColour;
    }

    private class PlayerSwitchedWeaponSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            PrimaryColourChanged();
        }
    }

    private class PlayerSpawnedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            PrimaryColourChanged();
        }
    }

    public void SetColour(double[] colour)
    {
        m_Counter = 0;
        m_PreviousColour.SetColour(colour);
        m_NextColour.SetColour(colour);
    }

    private void PrimaryColourChanged()
    {
        Runner player = m_Game.GetVehicleManager().GetPlayer();

        if(player == null)
            return;


        m_Counter = 0;
        m_PreviousColour.SetColour(m_PrimaryColour);
        m_NextColour.SetColour(GetColourByWeaponSlot(player.GetWeaponSlot()));

    }

    public double[] GetColourByWeaponSlot(WeaponSlot slot)
    {
        switch(slot)
        {
            case Up:
                return Colours.Cyan;
            case Down:
                return Colours.Magenta;
            case Left:
                return Colours.RunnerBlue;
            case Right:
                return Colours.EmeraldGreen;
        }

        return null;
    }
}
