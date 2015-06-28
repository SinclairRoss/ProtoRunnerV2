package com.raggamuffin.protorunnerv2.ui;

public class ContinuousActivation extends ActivationBehaviour
{
	public ContinuousActivation(TextAnimation Anchor) 
	{
		super(Anchor);
	}

	@Override
	protected void UpdateTrigger(double DeltaTime) 
	{
		m_Anchor.TriggerBehaviour();
	}

}
