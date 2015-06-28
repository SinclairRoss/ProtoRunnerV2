package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public class Idle extends Action
{

	protected Idle(Vehicle anchor) 
	{
		super(anchor);
	}

	@Override
	public void Update(double DeltaTime) 
	{
		m_Anchor.SetEngineOutput(0.0);
	}

}
