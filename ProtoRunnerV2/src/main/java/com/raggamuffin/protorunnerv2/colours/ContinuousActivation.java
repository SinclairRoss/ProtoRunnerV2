// Author:	Sinclair Ross.
// Date:	24/10/2014.
// Notes:	Continuously activates the colour behaviour by detecting if the behaviour is no 
//			longer triggered and simply triggers it again.

package com.raggamuffin.protorunnerv2.colours;

public class ContinuousActivation extends ActivationBehaviour
{

	public ContinuousActivation(ColourBehaviour Anchor) 
	{
		super(Anchor);
	}

	@Override
	protected void UpdateTrigger(double DeltaTime) 
	{
		m_Anchor.TriggerBehaviour();
	}
}
