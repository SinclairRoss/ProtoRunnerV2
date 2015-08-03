package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.Vehicles.Vehicle;

public abstract class PostFireAction
{
	protected Vehicle m_Anchor;
	
	public PostFireAction(Vehicle anchor)
	{
		m_Anchor = anchor;
	}
	
	public abstract void Update();
}
