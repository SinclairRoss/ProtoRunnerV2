package com.raggamuffin.protorunnerv2.colours;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class ColourBehaviour_LerpTo extends ColourBehaviour
{
	private double m_Intensity;

	public ColourBehaviour_LerpTo(GameObject Anchor, ActivationMode Mode) 
	{
		super(Anchor, Mode);

		m_Intensity 	= 0.0;
	}

	@Override
	protected void UpdateBehaviour(double deltaTime)
	{
		m_DeltaColour.SetVector(0.0);

        Colour base = m_Anchor.GetBaseColour();
		Colour alt = m_Anchor.GetAltColour();

		m_DeltaColour.I = MathsHelper.Lerp(m_Intensity, base.Red, 	alt.Red) 	- base.Red;
		m_DeltaColour.J = MathsHelper.Lerp(m_Intensity, base.Green, alt.Green) 	- base.Green;
		m_DeltaColour.K = MathsHelper.Lerp(m_Intensity, base.Blue, 	alt.Blue) 	- base.Blue;
	}

	public void SetIntensity(double intensity)
	{
		m_Intensity = MathsHelper.Clamp(intensity, 0.0, 1.0);
	}
}
