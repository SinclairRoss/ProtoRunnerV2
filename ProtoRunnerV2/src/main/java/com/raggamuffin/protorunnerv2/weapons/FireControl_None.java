package com.raggamuffin.protorunnerv2.weapons;

public class FireControl_None extends FireControl
{
    @Override
    public void Update(double deltaTime)
    {
        // Do nothing.
    }

    @Override
    public boolean ShouldFire()
    {
        return false;
    }

    @Override
    public void NotifyOfFire()
    {}
}
