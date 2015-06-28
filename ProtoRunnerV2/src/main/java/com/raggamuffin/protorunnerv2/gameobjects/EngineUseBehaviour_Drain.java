package com.raggamuffin.protorunnerv2.gameobjects;

public class EngineUseBehaviour_Drain extends EngineUseBehaviour
{
	private final double ENGINE_DRAIN_MULTIPLIER = 0.00007;
	
	private Vehicle m_Anchor;
	
	public EngineUseBehaviour_Drain(Vehicle anchor)
	{
		m_Anchor = anchor;
	}

	@Override
	public void Update(double deltaTime, double engineOutput, double dodgeOutput) 
	{
		double drainAmount 	 = (engineOutput * ENGINE_DRAIN_MULTIPLIER);
		drainAmount 		*= deltaTime;

		m_Anchor.DrainEnergy(drainAmount);
	}
}
