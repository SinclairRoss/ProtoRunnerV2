package com.raggamuffin.protorunnerv2.managers;

import com.raggamuffin.protorunnerv2.colours.ColourPair;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle_Runner;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.weapons.WeaponSlot;

public class ColourManager
{
    private GameLogic m_Game;

    private ColourPair m_ActiveColourPair;
    private ColourPair m_PreviousColourPair;
    private ColourPair m_Left;
    private ColourPair m_Right;
    private ColourPair m_Up;

    private Colour m_PrimaryColour;
    private Colour m_SecondaryColour;
    private Colour m_AccentTintColour;

    private double m_Counter;
    private double m_CounterMultiplier;
    private boolean m_ColoursUpdating;

    public ColourManager(GameLogic game)
    {
        m_Game = game;

        m_Left = new ColourPair(Colours.RunnerBlue, Colours.Pink70);
        m_Right = new ColourPair(Colours.White, Colours.Crimson);
        m_Up = new ColourPair(Colours.Azure, Colours.fireBrick);
        m_PreviousColourPair = new ColourPair();
        m_ActiveColourPair = m_Left;

        m_PrimaryColour = new Colour(m_Left.GetPrimary());
        m_SecondaryColour = new Colour(m_Left.GetSecondary());
        m_AccentTintColour = new Colour();
        UpdateAccentingColours();

        m_Counter = 0.0;
        m_CounterMultiplier = 1.0;
        m_ColoursUpdating = false;

        m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerSwitchedWeapon, new PlayerSwitchedWeaponSubscriber());
        m_Game.GetPubSubHub().SubscribeToTopic(PublishedTopics.PlayerSpawned, new PlayerSpawnedSubscriber());
    }

    public void Update(double deltaTime)
    {
        if(m_ColoursUpdating)
        {
            m_Counter += (deltaTime * m_CounterMultiplier);

            if(m_Counter >= 1.0)
            {
                m_Counter = 1.0;
                m_ColoursUpdating = false;
            }

            LerpColour(m_PrimaryColour, m_PreviousColourPair.GetPrimary(), m_ActiveColourPair.GetPrimary(), m_Counter);
            LerpColour(m_SecondaryColour, m_PreviousColourPair.GetSecondary(), m_ActiveColourPair.GetSecondary(), m_Counter);

            UpdateAccentingColours();
        }
    }

    private void LerpColour(Colour colour, Colour previous, Colour next, double lerpAmount)
    {
        colour.Red = MathsHelper.Lerp(lerpAmount, previous.Red, next.Red);
        colour.Green = MathsHelper.Lerp(lerpAmount, previous.Green, next.Green);
        colour.Blue = MathsHelper.Lerp(lerpAmount, previous.Blue, next.Blue);
    }

    private void UpdateAccentingColours()
    {
        m_AccentTintColour.SetColour(m_SecondaryColour);
        m_AccentTintColour.Brighten(-0.6);
    }

    public Colour GetPrimaryColour()
    {
        return m_PrimaryColour;
    }

    public Colour GetSecondaryColour()
    {
        return m_SecondaryColour;
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

    private void PrimaryColourChanged()
    {
        Vehicle_Runner player = m_Game.GetVehicleManager().GetPlayer();

        if(player != null)
        {
            m_PreviousColourPair.SetPrimaryColour(m_PrimaryColour);
            m_PreviousColourPair.SetSecondaryColour(m_SecondaryColour);

            m_ActiveColourPair = GetColourPairByWeaponSlot(player.GetWeaponSlot());

            ResetColourUpdateCounter();
        }
    }

    private ColourPair GetColourPairByWeaponSlot(WeaponSlot slot)
    {
        switch(slot)
        {
            case Up:
                return m_Up;
            case Down:
                return m_PreviousColourPair;
            case Left:
                return m_Left;
            case Right:
                return m_Right;
        }

        return null;
    }

    private void ResetColourUpdateCounter()
    {
        m_Counter = 0.0;
        m_ColoursUpdating = true;
    }
}
