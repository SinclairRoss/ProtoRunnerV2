package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Flee extends NavigationState
{
	public Flee(AIController Controller)
	{
		super(Controller);
	}
	
	@Override
	public double CalculateUtility() 
	{

		return 0;
	}
	
	@Override
	public void StartBehaviour() 
	{
	
	}

	@Override
	public Vector3 CalculateDestination() 
	{
		return null;
	}

	@Override
	public void EndBehaviour() 
	{

	}
}
