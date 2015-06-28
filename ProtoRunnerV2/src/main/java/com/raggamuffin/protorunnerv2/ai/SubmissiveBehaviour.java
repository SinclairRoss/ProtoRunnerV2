package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.ai.NavigationControl.NavigationStates;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public class SubmissiveBehaviour extends BehaviourState
{
	public SubmissiveBehaviour(AIController Controller) 
	{
		super(Controller);
	}

	@Override
	public void Update(double DeltaTime) 
	{
		Vehicle leader = m_Controller.GetLeader();
		
		if(leader == null)
		{
			return;
		}
		
		VehicleInfo leaderState = leader.GetVehicleInfo();
		
		switch(leaderState.GetAfterBurnerState())
		{
			case Disengaged:
				m_Anchor.DisengageAfterBurners();
				break;
			case Engaged:
				m_Anchor.EngageAfterBurners();
				break;
		}
	}

	@Override
	public void InitialiseState() 
	{
		m_NavController.SetNavigationState(NavigationStates.Follow);
	}
}