package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public abstract class Action 
{
	protected Vehicle m_Anchor;
	
	protected Action(Vehicle anchor)
	{
		m_Anchor = anchor;
	}

	public abstract void Update(double DeltaTime);
}
