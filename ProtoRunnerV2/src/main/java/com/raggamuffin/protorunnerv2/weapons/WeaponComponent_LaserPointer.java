// WeaponComponent_LaserPointer
// A component that creates laser pointers indicating where a weapon is aiming.

package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;

import java.util.ArrayList;

public class WeaponComponent_LaserPointer extends WeaponComponent
{
    private ArrayList<LaserPointer> m_LaserPointers;
    private GameLogic m_Game;

    public WeaponComponent_LaserPointer(GameLogic game, Weapon anchor)
    {
        super(anchor);

        m_Game = game;

        m_LaserPointers = new ArrayList<>();

        ArrayList<WeaponBarrel> barrels = m_Anchor.GetWeaponBarrels();

        int numBarrels = barrels.size();
        for(int i = 0; i < numBarrels; ++i)
        {
            WeaponBarrel barrel = barrels.get(i);

            LaserPointer pointer = new LaserPointer(m_Anchor, barrel);
            pointer.Off();
            m_LaserPointers.add(pointer);
            m_Game.AddObjectToRenderer(pointer);
        }
    }

    @Override
    public void Destroy()
    {
        int numPointers = m_LaserPointers.size();
        for(int i = 0; i < numPointers; ++i)
        {
            LaserPointer pointer = m_LaserPointers.get(i);
            m_Game.RemoveObjectFromRenderer(pointer);
        }
    }

    @Override
    public void Update(double deltaTime)
    {
        int numPointers = m_LaserPointers.size();
        for(int i = 0; i < numPointers; ++i)
        {
            LaserPointer pointer = m_LaserPointers.get(i);
            pointer.Update(deltaTime);
        }
    }

    @Override
    public void OnActivation()
    {
        int numPointers = m_LaserPointers.size();
        for(int i = 0; i < numPointers; ++i)
        {
            LaserPointer pointer = m_LaserPointers.get(i);
            pointer.On();
        }
    }

    @Override
    public void OnDeactivation()
    {
        int numPointers = m_LaserPointers.size();
        for(int i = 0; i < numPointers; ++i)
        {
            LaserPointer pointer = m_LaserPointers.get(i);
            pointer.Off();
        }
    }
}