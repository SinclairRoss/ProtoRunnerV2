// Author:	Sinclair Ross.
// Date:	24/10/2014.
// Notes:	This class does nothing. The colour behaviour this is attached to is 
//			triggered manually with the TriggerBehaviour function.
		
package com.raggamuffin.protorunnerv2.colours;

public class TriggeredActivation extends ActivationBehaviour
{

	public TriggeredActivation(ColourBehaviour Anchor) 
	{
		super(Anchor);
	}

	@Override
	protected void UpdateTrigger(double DeltaTime) 
	{
		// Do nothing. This behaviour is triggered manually by directly accessing the colour behaviour.
	}
	
}
