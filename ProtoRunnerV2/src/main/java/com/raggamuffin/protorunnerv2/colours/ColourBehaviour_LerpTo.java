package com.raggamuffin.protorunnerv2.colours;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class ColourBehaviour_LerpTo extends ColourBehaviour
{
	private Colour m_BaseColour;
	private Colour m_AltColour;
	private double m_Intensity;

	public ColourBehaviour_LerpTo(GameObject Anchor, ActivationMode Mode) 
	{
		super(Anchor, Mode);
		
		m_BaseColour 	= Anchor.GetBaseColour();
		m_AltColour 	= new Colour();
		m_Intensity 	= 0.0;
	}

	@Override
	protected void UpdateBehaviour(double DeltaTime) 
	{
		m_DeltaColour.SetVector(0.0);
		
		m_DeltaColour.I = MathsHelper.Lerp(m_Intensity, m_BaseColour.Red, 	m_AltColour.Red) 	- m_BaseColour.Red;
		m_DeltaColour.J = MathsHelper.Lerp(m_Intensity, m_BaseColour.Green, m_AltColour.Green) 	- m_BaseColour.Green;
		m_DeltaColour.K = MathsHelper.Lerp(m_Intensity, m_BaseColour.Blue, 	m_AltColour.Blue) 	- m_BaseColour.Blue;		
	}

    public void SetAltColourByReference(Colour alt)
    {
        m_AltColour = alt;
    }

	public void SetAltColour(Colour alt)
	{
		m_AltColour.SetColour(alt);
	}
	
	public void SetAltColour(double[] alt)
	{
		m_AltColour.SetColour(alt);
	}
	
	public void SetIntensity(double intensity)
	{
		m_Intensity = MathsHelper.Clamp(intensity, 0, 1);
		
	}
}
