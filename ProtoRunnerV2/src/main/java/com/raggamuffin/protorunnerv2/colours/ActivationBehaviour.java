package com.raggamuffin.protorunnerv2.colours;

public abstract class ActivationBehaviour 
{
	protected ColourBehaviour m_Anchor;
	
	public ActivationBehaviour(ColourBehaviour Anchor)
	{
		m_Anchor = Anchor;
	}
	
	protected abstract void UpdateTrigger(double DeltaTime);
}
