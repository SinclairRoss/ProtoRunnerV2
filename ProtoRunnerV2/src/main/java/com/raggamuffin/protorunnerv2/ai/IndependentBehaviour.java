package com.raggamuffin.protorunnerv2.ai;

public class IndependentBehaviour extends BehaviourState
{
	public IndependentBehaviour(AIController Controller)
	{
		super(Controller);
	}
	
	@Override
	public void Update(double DeltaTime) 
	{
		m_NavController.SetHighestUtilityState();
	}

	@Override
	public void InitialiseState() 
	{

	}
}
