package com.raggamuffin.protorunnerv2.colours;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class ColourBehaviour_Pulse extends ColourBehaviour
{
	private double m_Counter;
	private double m_Intensity;
	private double m_Rate;
	
	public ColourBehaviour_Pulse(GameObject Anchor, ActivationMode Mode) 
	{
		super(Anchor, Mode);
		
		m_Counter 	= MathsHelper.RandomDouble(0,  Math.PI * 2);
		m_Intensity = 0.25;
		m_Rate = 3.0;
	}

	@Override
	public void UpdateBehaviour(double DeltaTime) 
	{
		m_DeltaColour.SetVector(0.0);
		
		m_Counter += DeltaTime * m_Rate;
		
		// If the behaviour completes on cycle.
		// stop the behaviour.
		if(m_Counter >= Math.PI * 2)
		{
			m_Counter = 0.0;
			m_Trigger = false;
		}
	
		m_DeltaColour.SetVector(Math.sin(m_Counter) * m_Intensity);
		m_DeltaColour.W = 1.0;
	}
}
