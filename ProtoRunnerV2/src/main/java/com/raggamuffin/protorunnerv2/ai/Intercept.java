// Author:	Sinclair Ross.
// Date:	19/10/2014.

package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Intercept extends NavigationState
{
	public Intercept(AIController Controller)
	{
		super(Controller);
	}
	
	@Override
	public double CalculateUtility() 
	{
		return 1;
	}

	@Override
	public void StartBehaviour() 
	{

	}
	
	@Override
	public Vector3 CalculateDestination() 
	{
		Vehicle Target = m_SituationalAwareness.GetTarget();
		m_Destination.SetVector(0.0);
		
		if(Target != null)		
		{
			m_Destination.SetVector(Target.GetPosition());
		}
		
		return m_Destination;
	}

	@Override
	public void EndBehaviour() 
	{

	}
}
