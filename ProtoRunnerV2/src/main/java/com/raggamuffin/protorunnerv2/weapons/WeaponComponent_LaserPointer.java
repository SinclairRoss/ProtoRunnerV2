// WeaponComponent_LaserPointer
// A component that creates laser pointers indicating where a weapon is aiming.

package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;

import java.util.ArrayList;

public class WeaponComponent_LaserPointer extends WeaponComponent
{
    private ArrayList<LaserPointer> m_LaserPointers;

    public WeaponComponent_LaserPointer(GameLogic game, Weapon anchor)
    {
        super(anchor);

        m_LaserPointers = new ArrayList<>();

        ArrayList<WeaponBarrel> barrels = m_Anchor.GetWeaponBarrels();
        for(WeaponBarrel barrel : barrels)
        {
            LaserPointer pointer = new LaserPointer(m_Anchor, barrel);
            m_LaserPointers.add(pointer);

            game.AddObjectToRenderer(pointer);
        }
    }

    @Override
    public void Update(double deltaTime)
    {
        for(LaserPointer pointer : m_LaserPointers)
        {
            pointer.Update(deltaTime);
        }
    }

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
