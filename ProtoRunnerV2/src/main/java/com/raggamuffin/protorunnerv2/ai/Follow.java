package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Follow extends NavigationState
{
	public Follow(AIController Controller) 
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
		Vehicle Leader = m_Controller.GetLeader();
		m_Destination.SetVector(0.0);
		
		if(Leader != null)		
		{
			m_Destination.SetVector(Leader.GetPosition());
		}
		
		return m_Destination;
	}

	@Override
	public void EndBehaviour() 
	{
		
	}
}
