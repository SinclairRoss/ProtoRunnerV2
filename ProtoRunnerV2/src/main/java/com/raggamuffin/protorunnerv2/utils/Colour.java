package com.raggamuffin.protorunnerv2.utils;

import android.util.Log;

public class Colour 
{
	public double Red;
	public double Green;
	public double Blue;
	public double Alpha;
	
	public Colour()
	{
		Red 	= 1.0;
		Green 	= 1.0;
		Blue 	= 1.0;
		Alpha 	= 1.0;
	}
	
	public Colour(double red, double green, double blue, double alpha)
	{
		SetColour(red, green, blue, alpha);
	}
	
	public Colour(Colour colour)
	{
		SetColour(colour.Red, colour.Green, colour.Blue, colour.Alpha);
	}
	
	public Colour(double[] colour)
	{
		SetColour(colour[0], colour[1], colour[2], 1.0);
	}

	public Colour(double[] colour, double alpha)
	{
		SetColour(colour[0], colour[1], colour[2], alpha);
	}
	
	public void SetColour(double red, double green, double blue, double alpha)
	{
		Red 	= red;
		Green 	= green;
		Blue 	= blue;
		Alpha 	= alpha;
	}
	
	public void SetColour(Colour colour)
	{
		SetColour(colour.Red, colour.Green, colour.Blue, colour.Alpha);
	}
	
	public void SetColour(double[] colour)
	{
		SetColour(colour[0], colour[1], colour[2], 1.0);
	}
	
	public void Brighten(double amount)
	{
		double DeltaRed;
		double DeltaGreen;
		double DeltaBlue;
		double DeltaAlpha;
		
		if(amount > 0.0)
		{
			// Calculate how far a colour is from full intensity.
			DeltaRed 	= 1.0 - Red;
			DeltaGreen 	= 1.0 - Green;
			DeltaBlue 	= 1.0 - Blue;
			DeltaAlpha 	= 1.0 - Alpha;
		}
		else
		{
			// Calculate how far a colour is from Minimal intensity.
			DeltaRed 	= Red;
			DeltaGreen 	= Green;
			DeltaBlue 	= Blue;
			DeltaAlpha 	= Alpha;
		}
		
		// Calculate how much each colour should be intensified by.
		Red 	+= DeltaRed   * amount;
		Green 	+= DeltaGreen * amount;
		Blue 	+= DeltaBlue  * amount;
		Alpha 	+= DeltaAlpha * amount;

		ClampColour();
	}
	
	public void Add(Colour a)
	{
        Add(a.Red, a.Green, a.Blue, a.Alpha);
	}
	
	public void Add(Vector3 a)
	{
		Add(a.X, a.Y, a.Z, 0);
	}
	
	public void Add(Vector4 a)
	{
		Add(a.I, a.J, a.K, a.W);
	}
	
	public void Add(double red, double green, double blue, double alpha)
	{
		Red 	+= red;
		Green 	+= green;
		Blue 	+= blue;
		Alpha 	+= alpha;
		
		ClampColour();
	}
	
	public void ClampColour()
	{
		Red 	= MathsHelper.Clamp(Red, 0, 1);
		Green 	= MathsHelper.Clamp(Green, 0, 1);
		Blue 	= MathsHelper.Clamp(Blue, 0, 1);
		Alpha 	= MathsHelper.Clamp(Alpha, 0, 1);
	}
	
	public void Output(String tag)
	{
		Log.e(tag, "--------------");
		Log.e(tag, "Red: " 		+ Double.toString(Red));
		Log.e(tag, "Green: " 	+ Double.toString(Green));
		Log.e(tag, "Blue: " 	+ Double.toString(Blue));
		Log.e(tag, "Alpha: " 	+ Double.toString(Alpha));
	}
}








































