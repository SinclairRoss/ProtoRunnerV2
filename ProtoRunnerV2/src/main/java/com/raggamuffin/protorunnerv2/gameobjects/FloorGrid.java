package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colour;

public class FloorGrid extends GameObject
{
	private final double DEPTH = -1.0;
	
	public FloorGrid(Colour col)
	{
		super(null, null);
		
		m_Model = ModelType.FloorPanel;
		m_Colour = col;
	}
	
	@Override
	public void Update(double DeltaTime)
	{
		m_Position.J = DEPTH;
	}

	@Override
	public boolean IsValid() 
	{
		// Upon Anchor invalidation this class will be removed.
		return true;
	}

    @Override
    public void CleanUp()
    {

    }
}
