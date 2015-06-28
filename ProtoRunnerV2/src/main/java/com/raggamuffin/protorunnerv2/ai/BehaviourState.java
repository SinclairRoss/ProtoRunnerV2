package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public abstract class BehaviourState
{
	protected AIController m_Controller;
	protected NavigationControl m_NavController;
	protected Vehicle m_Anchor;
	
	public BehaviourState(AIController Controller)
	{
		m_Controller 	= Controller;
		m_NavController = m_Controller.GetNavigationControl();
		
		m_Anchor 		= m_Controller.GetAnchor();
	}
	
	
	public abstract void InitialiseState();
	public abstract void Update(double DeltaTime);
}
