package com.raggamuffin.protorunnerv2.ai;

public class BehaviourControl 
{
	public enum BehaviourStates {Independent, Submissive};

	private AIController m_Controller;
	
	private BehaviourState m_BehaviourState;
	private IndependentBehaviour m_IndependentState;
	private SubmissiveBehaviour  m_SubmissiveState;
	
	public BehaviourControl(AIController Controller)
	{
		m_Controller 		= Controller;
		m_IndependentState 	= new IndependentBehaviour(Controller);
		m_SubmissiveState 	= new SubmissiveBehaviour(Controller);
		m_BehaviourState 	= m_IndependentState;
	}
	
	public void Update(double DeltaTime)
	{
		m_BehaviourState.Update(DeltaTime);
	}

	public void SetState(BehaviourStates State)
	{
		switch(State)
		{
			case Independent:
				m_BehaviourState = m_IndependentState;
				break;
				
			case Submissive:
				m_BehaviourState = m_SubmissiveState;
				break;
		}
		
		m_BehaviourState.InitialiseState();
	}
}
