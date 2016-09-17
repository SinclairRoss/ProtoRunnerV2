package com.raggamuffin.protorunnerv2.renderer;

// Author: Sinclair Ross
// Date:   14/08/2016

public class RenderObject_None extends RenderObject
{
    public RenderObject_None()
    {
        super("", "", ModelType.Nothing);
    }

    @Override
    public void InitialiseModel(float[] projMatrix)
    {}

    @Override
    public void Draw()
    {}

    @Override
    public void CleanModel()
    {}
}
