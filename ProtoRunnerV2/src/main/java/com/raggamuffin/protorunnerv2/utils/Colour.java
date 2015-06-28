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
		Red 	= red;
		Green 	= green;
		Blue 	= blue;
		Alpha 	= alpha;
	}
	
	public Colour(Colour colour)
	{
		Red 	= colour.Red;
		Green 	= colour.Green;
		Blue 	= colour.Blue;
		Alpha 	= colour.Alpha;
	}
	
	public Colour(double[] colour)
	{
		Red 	= colour[0];
		Green 	= colour[1];
		Blue 	= colour[2];
		Alpha 	= colour[3];
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
		Red 	= colour.Red;
		Green 	= colour.Green;
		Blue 	= colour.Blue;
		Alpha 	= colour.Alpha;
	}
	
	public void SetColour(double[] colour)
	{
		Red 	= colour[0];
		Green 	= colour[1];
		Blue 	= colour[2];
		Alpha 	= colour[3];
	}

    // Quick inverse.
	public void SetAsInverse(Colour colour)
	{
        Red 	= 1.0 - colour.Red;
        Green 	= 1.0 - colour.Green;
        Blue 	= 1.0 - colour.Blue;
        Alpha 	= colour.Alpha;
	}
	
	public void Brighten(double amount)
	{
		double DeltaRed 	= 1.0;
		double DeltaGreen 	= 1.0;
		double DeltaBlue 	= 1.0;
		double DeltaAlpha 	= 1.0;
		
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
	
	public float[] ToFloatArray()
	{
		float[] array = new float[4];
		array[0] = (float)Red;
		array[1] = (float)Green;
		array[2] = (float)Blue;
		array[3] = (float)Alpha;
		
		return array;
	}
	
	public int ToHex()
	{
		int Hex = 0x00000000;
		
		Hex += (int)(Alpha * 255) << 24;
		Hex += (int)(Red   * 255) << 16;
		Hex += (int)(Green * 255) << 8;
		Hex += (int)(Blue  * 255);
		
		return Hex;
	}
	
	public void Add(Colour A)
	{
		Red 	+= A.Red;
		Green 	+= A.Green;
		Blue 	+= A.Blue;
		Alpha 	+= A.Alpha;
		
		ClampColour();
	}
	
	public void Add(Vector3 A)
	{
		Red 	+= A.I;
		Green 	+= A.J;
		Blue 	+= A.K;
		
		ClampColour();
	}
	
	public void Add(Vector4 A)
	{
		Red 	+= A.I;
		Green 	+= A.J;
		Blue 	+= A.K;
		Alpha 	+= A.W;
		
		ClampColour();
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








































