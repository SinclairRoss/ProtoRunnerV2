package com.raggamuffin.protorunnerv2.ui;

public abstract class ActivationBehaviour 
{
	protected TextAnimation m_Anchor;
	
	public ActivationBehaviour(TextAnimation Anchor)
	{
		m_Anchor = Anchor;
	}
	
	protected abstract void UpdateTrigger(double DeltaTime);
}
