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
        for(WeaponBarrel barrel : barrels)
        {
            LaserPointer pointer = new LaserPointer(game, m_Anchor, barrel);
            m_LaserPointers.add(pointer);
        }
    }

    @Override
    public void Destroy()
    {}

    @Override
    public void Update(double deltaTime)
    {}

    @Override
    public void OnActivation()
    {
        for(LaserPointer pointer : m_LaserPointers)
        {
            pointer.On();
        }
    }

    @Override
    public void OnDeactivation()
    {
        for(LaserPointer pointer : m_LaserPointers)
        {
            pointer.Off();
        }
    }
}
