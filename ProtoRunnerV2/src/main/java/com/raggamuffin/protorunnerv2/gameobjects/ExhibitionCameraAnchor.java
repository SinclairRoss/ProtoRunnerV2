package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ExhibitionCameraAnchor extends GameObject
{
	public ExhibitionCameraAnchor()
	{
        super(ModelType.Nothing, 1.0);

        SetPosition(0, 40, 0);

        SetForward(0, -1, 0);
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
