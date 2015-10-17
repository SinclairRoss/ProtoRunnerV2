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
	
	public void SetGlowIntensityHoriz(double intensity)
	{
		m_GlowIntensityHoriz = intensity;
	}
	
	public double GetGlowIntensityVert()
	{
		return m_GlowIntensityVert;
	}

	public void SetGlowIntensityVert(double intensity)
	{
		m_GlowIntensityVert = intensity;
	}
	
	public void SetGlowIntensity(double intensity)
	{
		m_GlowIntensityHoriz = intensity;
		m_GlowIntensityVert = intensity;
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
