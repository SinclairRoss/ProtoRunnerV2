package com.raggamuffin.protorunnerv2.ui;

public class TriggeredActivation extends ActivationBehaviour
{
	public TriggeredActivation(TextAnimation Anchor)
	{
		super(Anchor);
	}

	@Override
	protected void UpdateTrigger(double DeltaTime) 
	{
		// Do nothing. This behaviour is triggered manually by directly accessing the colour behaviour.
	}
}
