// Author: 	Sinclair Ross.
// Date:	18/10/2014
// Notes:	The purpose of this class is to calculate the target destination of the agent it is attached to.
//			By writing subclasses that calculate the destination differently we can easily create multiple
//			behaviours for the agent. e.g. Seek, Flee, Wander, Regroup.
//			Be aware that this class doesn't dictate how the agent gets to its destination, Only where the desination is.


package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class NavigationState 
{	
	protected AIController m_Controller;
	protected SituationalAwareness m_SituationalAwareness;
	protected Vector3 m_Destination;
	
	protected NavigationState(AIController Controller)
	{
		m_Controller 			= Controller;
		m_SituationalAwareness 	= m_Controller.GetSituationalAwareness();
		m_Destination 			= new Vector3();
	}

	public abstract double CalculateUtility();
	public abstract void StartBehaviour();
	public abstract Vector3 CalculateDestination();
	public abstract void EndBehaviour();
}
