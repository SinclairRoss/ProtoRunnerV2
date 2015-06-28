package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Strafe extends NavigationState
{
	protected Strafe(AIController Controller) 
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
		if(MathsHelper.RandomBoolean())
		{
			m_Controller.GetAnchor().StrafeLeft();
		}
		else
		{
			m_Controller.GetAnchor().StrafeRight();
		}
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
		m_Controller.GetAnchor().UseForwardEngine();
	}
}