// WeaponComponent_None
// Represents the absence of weapon components.

package com.raggamuffin.protorunnerv2.weapons;

public class WeaponComponent_None extends WeaponComponent
{
    public WeaponComponent_None()
    {
        super(null);
    }

    @Override
    public void Update(double deltaTime)
    {
        // Do nothing.
    }

    @Override
    public void OnActivation()
    {

    }

    @Override
    public void OnDeactivation()
    {

    }
}
