package com.raggamuffin.protorunnerv2.colours;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Vector4;

public abstract class ColourBehaviour 
{
	public enum ActivationMode {Continuous, Triggered, Random}

	private ActivationBehaviour m_ActivationBehaviour;
	
	protected GameObject m_Anchor;
	protected boolean m_Trigger;
	protected Vector4 m_DeltaColour;

	public ColourBehaviour(GameObject Anchor, ActivationMode Mode)
	{
		m_Anchor = Anchor;
		m_Trigger = false;
		m_DeltaColour = new Vector4();
		
		SetActivationMode(Mode);
	}
	
	public void Update(double DeltaTime)
	{
		m_ActivationBehaviour.UpdateTrigger(DeltaTime);
		
		if(m_Trigger)
		{
			UpdateBehaviour(DeltaTime);
		}
	}
	
	protected abstract void UpdateBehaviour(double DeltaTime);

	private void SetActivationMode(ActivationMode Mode)
	{
		switch(Mode)
		{
			case Continuous:
				m_ActivationBehaviour = new ContinuousActivation(this);
				break;
				
			case Random:
				m_ActivationBehaviour = new RandomActivation(this);
				break;
				
			case Triggered:
				m_ActivationBehaviour = new TriggeredActivation(this);
				break;
				
			default:
				new RuntimeException("ColourBehaviour.java - SetActivationMode(ActivationMode Mode) : Unhandled switch case.");
				break;
		}
	}
	
	public Vector4 GetDeltaColour()
	{
		return m_DeltaColour;
	}
	
	public void TriggerBehaviour()
	{
		m_Trigger = true;
	}
}
 