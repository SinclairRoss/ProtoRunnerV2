package com.raggamuffin.protorunnerv2.ai;

public class BehaviourControl 
{
	public enum BehaviourStates {Independent, Submissive}

	private AIController m_Controller;
	
	private BehaviourState m_BehaviourState;
	private IndependentBehaviour m_IndependentState;
	private SubmissiveBehaviour  m_SubmissiveState;
	
	public BehaviourControl(AIController Controller, AIGoalSet goalSet)
	{
		m_Controller 		= Controller;
		m_IndependentState 	= new IndependentBehaviour(Controller, goalSet);
		m_SubmissiveState 	= new SubmissiveBehaviour(Controller);
		m_BehaviourState 	= m_IndependentState;
	}
	
	public void Update()
	{
		m_BehaviourState.Update();
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
