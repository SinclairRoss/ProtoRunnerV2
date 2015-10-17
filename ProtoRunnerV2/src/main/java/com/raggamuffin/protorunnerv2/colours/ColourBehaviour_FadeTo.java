// Author:	Sinclair Ross.
// Date:	23/10/2014.
// Notes:	Smoothly transitions from one base colour to another.

package com.raggamuffin.protorunnerv2.colours;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class ColourBehaviour_FadeTo extends ColourBehaviour
{
	private Colour m_BaseColour;
	private Colour m_PreviousColour;
	
	private double m_Counter;
	private double m_CounterMultiplier;
	
	public ColourBehaviour_FadeTo(GameObject anchor, ActivationMode mode, double timeFrame)
	{
		super(anchor, mode);

		m_BaseColour = m_Anchor.GetBaseColour();
		m_PreviousColour = new Colour();
		
		m_Counter = 0.0;
		SetTimeFrame(timeFrame);
	}

	@Override
	public void UpdateBehaviour(double deltaTime) 
	{
		m_DeltaColour.SetVector(0.0);

		m_Counter += (deltaTime * m_CounterMultiplier);

		// If the behaviour completes on cycle.
		// stop the behaviour.
		if(m_Counter >= 1.0)
		{
			m_Counter = 1.0;
			m_Trigger = false;
		}

		m_DeltaColour.I 	= MathsHelper.Lerp(m_Counter, m_PreviousColour.Red,   m_BaseColour.Red) 	- m_PreviousColour.Red;
		m_DeltaColour.J 	= MathsHelper.Lerp(m_Counter, m_PreviousColour.Green, m_BaseColour.Green) 	- m_PreviousColour.Green;
		m_DeltaColour.K 	= MathsHelper.Lerp(m_Counter, m_PreviousColour.Blue,  m_BaseColour.Blue) 	- m_PreviousColour.Blue;
	}

    public void SetPreviousColour(Colour previousColour)
    {
        m_PreviousColour.SetColour(previousColour);
    }

	@Override
	public void TriggerBehaviour()
	{
		super.TriggerBehaviour();
		m_Counter = 0.0;
	}
	
	public void SetTimeFrame(double timeFrame)
	{
		m_CounterMultiplier = 1 / timeFrame;
	}
}
