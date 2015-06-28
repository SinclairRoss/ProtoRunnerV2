package com.raggamuffin.protorunnerv2.master;

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class RenderEffectSettings
{
	private double m_GlowIntensityVert;
	private double m_GlowIntensityHoriz;

    private Colour m_SkyBoxColour;
	
	public RenderEffectSettings()
	{
		m_GlowIntensityVert  = 0.0;
		m_GlowIntensityHoriz = 0.0;

        m_SkyBoxColour = null;
	}
	
	public double GetGlowIntensityHoriz()
	{
		return m_GlowIntensityHoriz;
	}
	
	public void SetGlowIntensityHoriz(double Intensity)
	{
		m_GlowIntensityHoriz = Intensity;
	}
	
	public double GetGlowIntensityVert()
	{
		return m_GlowIntensityVert;
	}

	public void SetGlowIntensityVert(double Intensity)
	{
		m_GlowIntensityVert = Intensity;
	}
	
	public void SetGlowIntensity(double Intensity)
	{
		m_GlowIntensityHoriz = Intensity;
		m_GlowIntensityVert = Intensity;
	}

    public void SetSkyboxColour(Colour colour)
    {
        m_SkyBoxColour = colour;
    }

    public Colour GetSkyBoxColour()
    {
        return m_SkyBoxColour;
    }
}
