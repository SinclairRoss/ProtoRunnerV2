package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   18/02/2017

import com.raggamuffin.protorunnerv2.renderer.ModelType;

public class GameObject_Marker extends GameObject
{
    public GameObject_Marker(double[] colour)
    {
        super(ModelType.WeaponDrone, 1);

        SetColour(colour);
    }

    @Override
    public boolean IsValid()
    {
        return true;
    }

    @Override
    public void CleanUp()
    {}
}
