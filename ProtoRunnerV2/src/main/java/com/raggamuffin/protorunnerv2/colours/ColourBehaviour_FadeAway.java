package com.raggamuffin.protorunnerv2.colours;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class ColourBehaviour_FadeAway extends ColourBehaviour
{
	// Increase Blue.
	// Decrease Red.
	// Leave Green Alone.

	private double m_Counter;
	private double m_CounterMultiplier;

	public ColourBehaviour_FadeAway(GameObject Anchor, ActivationMode Mode, double TimeFrame) 
	{
		super(Anchor, Mode);

		m_Counter = 0.0;
		m_CounterMultiplier = 1 / TimeFrame;
	}

	@Override
	public void UpdateBehaviour(double DeltaTime) 
	{
		m_DeltaColour.SetVector(0.0);
		
		m_Counter += (DeltaTime * m_CounterMultiplier);
		
		// If the behaviour completes on cycle.
		// stop the behaviour.
		if(m_Counter >= 1.0)
		{
			m_Counter = 1.0;
			m_Trigger = false;
		}

		double Amount = MathsHelper.Lerp(m_Counter, 0.0, 1.0);
		
		m_DeltaColour.I 	= -Amount;
		m_DeltaColour.K 	=  Amount;
		m_DeltaColour.W		= -Amount;
		
	}
	
	@Override
	public void TriggerBehaviour()
	{
		super.TriggerBehaviour();
		m_Counter = 0.0;
	}
}
