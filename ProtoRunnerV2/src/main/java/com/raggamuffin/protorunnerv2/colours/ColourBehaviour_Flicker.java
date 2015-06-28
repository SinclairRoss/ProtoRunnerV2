// Author: 	Sinclair Ross.
// Date:	30/10/2014.

package com.raggamuffin.protorunnerv2.colours;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class ColourBehaviour_Flicker extends ColourBehaviour
{
	
	private Colour m_BaseColour;
	
	// How long the behaviour lasts for.
	private double m_ActivationTimer;
	private double m_MinActivationTime;
	private double m_MaxActivationTime;
		
	// By how much the colour will be brightened or darkened.
	private double m_FlickerAmount;
	private double m_MinFlickerAmount;
	private double m_MaxFlickerAmount;
	
	// How long until the brightness changes.
	private double m_FlickerTimer;
	private double m_MinFlickerTime;
	private double m_MaxFlickerTime;

	public ColourBehaviour_Flicker(GameObject Anchor, ActivationMode Mode) 
	{
		super(Anchor, Mode);
		
		m_BaseColour = Anchor.GetBaseColour();
		
		m_ActivationTimer 	=  0.0;
		m_MinActivationTime =  1.0;
		m_MaxActivationTime =  1.5;
		
		m_FlickerAmount 	=  0.0;
		m_MinFlickerAmount 	= -0.9;
		m_MaxFlickerAmount 	=  0.2;
		
		m_FlickerTimer 		=  0.0;
		m_MinFlickerTime 	=  0.05;
		m_MaxFlickerTime 	=  0.1;

	}
	
	@Override
	public void UpdateBehaviour(double DeltaTime)
	{
		m_ActivationTimer 	-= DeltaTime;
		m_FlickerTimer 		-= DeltaTime;
	
		if(m_FlickerTimer <= 0.0)
		{
			m_FlickerTimer 	= MathsHelper.RandomDouble(m_MinFlickerTime, m_MaxFlickerTime);
			m_FlickerAmount = MathsHelper.RandomDouble(m_MinFlickerAmount, m_MaxFlickerAmount);
			
			if(m_FlickerAmount > 0.0)
			{
				// Calculate how far a colour is from full intensity.
				m_DeltaColour.I 	= (1.0 - m_BaseColour.Red) 	 * m_FlickerAmount;
				m_DeltaColour.J 	= (1.0 - m_BaseColour.Green) * m_FlickerAmount;
				m_DeltaColour.K 	= (1.0 - m_BaseColour.Blue)  * m_FlickerAmount;
			}
			else
			{
				// Calculate how far a colour is from Minimal intensity.
				m_DeltaColour.I = (m_BaseColour.Red)	 * m_FlickerAmount;
				m_DeltaColour.J = (m_BaseColour.Green)	 * m_FlickerAmount;
				m_DeltaColour.K	= (m_BaseColour.Blue)	 * m_FlickerAmount;
			}
		}
		
		if(m_ActivationTimer <= 0.0)
		{
			m_DeltaColour.SetVector(0.0);
			m_Trigger = false;
		}
	}
	
	@Override
	public void TriggerBehaviour()
	{
		super.TriggerBehaviour();
		m_ActivationTimer 	= MathsHelper.RandomDouble(m_MinActivationTime,m_MaxActivationTime);
	}
}
