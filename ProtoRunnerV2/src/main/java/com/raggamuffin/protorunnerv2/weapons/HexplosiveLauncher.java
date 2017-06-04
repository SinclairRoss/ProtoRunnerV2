package com.raggamuffin.protorunnerv2.weapons;

// Author: Sinclair Ross
// Date:   18/04/2017

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject_Stick;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.GameObjectManager;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.ArrayList;

public class HexplosiveLauncher
{
    public enum HexDirection
    {
        North,
        NorthEast,
        SouthEast,
        South,
        SouthWest,
        NorthWest
    }

    private final int NUM_HEXPLOSIVES = 6;
    private final double SPACING = 0.5;
    private final double DELTA_Z = HexPlosive.RADIUS * 0.86603;
    private final double DELTA_X = HexPlosive.RADIUS * 1.5;

    private GameLogic m_Game;
    private GameObjectManager m_GameObjectManager;

    private Vehicle m_Anchor;
    private Timer m_CooldownTimer;

    private ArrayList<Vector3> m_TargetPositions;

    private Timer m_FireTimer;
    private boolean m_Firing;
    private int m_FireIndex;

    public HexplosiveLauncher(Vehicle anchor, GameLogic game)
    {
        m_Game = game;
        m_GameObjectManager = game.GetGameObjectManager();

        m_Anchor = anchor;
        m_CooldownTimer = new Timer(10);
        m_CooldownTimer.StartElapsed();

        m_TargetPositions = new ArrayList<>(NUM_HEXPLOSIVES);

        for(int i = 0; i < NUM_HEXPLOSIVES; ++i)
        {
            m_TargetPositions.add(new Vector3());
        }

        m_FireTimer = new Timer(0.1);
        m_FireTimer.StartElapsed();
        m_Firing = false;
        m_FireIndex = 0;
    }

    public void Update()
    {
        if(m_Firing)
        {
            if(m_FireTimer.HasElapsed())
            {
                Vector3 targetPos = m_TargetPositions.get(m_FireIndex);

                GameObject_Stick stick = new GameObject_Stick(m_Anchor.GetPosition(), targetPos, m_Anchor.GetColour());
                m_GameObjectManager.AddObject(stick);
                CreateHexplosion(targetPos);

                ++m_FireIndex;
                m_FireTimer.Start();

                if(m_FireIndex >= NUM_HEXPLOSIVES)
                {
                    m_Firing = false;
                }
            }
        }
    }

    public void Fire(Vector3 initialPos)
    {
        if(m_CooldownTimer.HasElapsed() && !m_Firing)
        {
            m_TargetPositions.get(0).SetVector(initialPos);
            m_TargetPositions.get(0).Y = -1;

            for (int i = 1; i < NUM_HEXPLOSIVES; ++i)
            {
                Vector3 targetPos = m_TargetPositions.get(i);
                targetPos.SetVector(m_TargetPositions.get(i - 1));
                UpdateVectorPosition(targetPos, HexDirection.South);
            }

            m_Firing = true;
            m_CooldownTimer.Start();

            m_FireTimer.Start();
            m_FireIndex = 0;
        }
    }

    private int PickNextDirection(HexDirection forwardDirection)
    {
        int directionOrdinal = forwardDirection.ordinal();

        switch (MathsHelper.RandomInt(3))
        {
            case 0: // Left
                return directionOrdinal > 0 ? directionOrdinal - 1 : 5;
            case 1: // Center
                return directionOrdinal;
            case 2: // Right
                return directionOrdinal < HexDirection.values().length ? directionOrdinal + 1 : 0;
        }

        return 0;
    }

    private void UpdateVectorPosition(Vector3 targetPos, HexDirection forwardDirection)
    {
        int index = PickNextDirection(forwardDirection);

        switch (HexDirection.values()[index])
        {
            case North:
            {
                targetPos.Z += (DELTA_Z * 2) + SPACING;
                break;
            }
            case NorthEast:
            {
                targetPos.X += (DELTA_X) + SPACING;
                targetPos.Z += (DELTA_Z) + SPACING;
                break;
            }
            case SouthEast:
            {
                targetPos.X += (DELTA_X) + SPACING;
                targetPos.Z -= (DELTA_Z) + SPACING;
                break;
            }
            case South:
            {
                targetPos.Z -= (DELTA_Z * 2) + SPACING;
                break;
            }
            case SouthWest:
            {
                targetPos.X -= (DELTA_X) + SPACING;
                targetPos.Z -= (DELTA_Z) + SPACING;
                break;
            }
            case NorthWest:
            {
                targetPos.X -= (DELTA_X) + SPACING;
                targetPos.Z += (DELTA_Z) + SPACING;
                break;
            }
        }

        targetPos.Y = -1;
    }

    public void CreateHexplosion(Vector3 targetPos)
    {
        HexPlosive hex = new HexPlosive(m_Game);
        hex.SetPosition(targetPos);
        m_GameObjectManager.AddObject(hex);
    }
}
