package com.raggamuffin.protorunnerv2.utils;

import android.graphics.Point;
import android.util.Log;

public class Vector2 
{
	public static final Vector2 RIGHT   = new Vector2(1, 0);
	public static final Vector2 FORWARD = new Vector2(0, 1);

	public double X;
	public double Y;

	public Vector2()
	{
		X = 0.0f;
		Y = 0.0f;
	}
	
	public Vector2(double i, double j)
	{
		X = i;
		Y = j;
	}
	
	public Vector2(double scale)
	{
		X = scale;
		Y = scale;
	}
	
	public Vector2(Vector2 vector)
	{
		X = vector.X;
		Y = vector.Y;
	}
	
	public Vector2(Point size)
	{
		X = size.x;
		Y = size.y;
	}

	public void SetVector(double i, double j)
	{
		X = i;
		Y = j;
	}
	
	public void SetVector(double scale)
	{
		X = scale;
		Y = scale;
	}
	
	public void SetVector(Vector2 vector)
	{
		X = vector.X;
		Y = vector.Y;
	}
	
	public void SetVector(Point size)
	{
		X = size.x;
		Y = size.y;
	}

	public void SetAsRandNorm()
	{
		SetAsRand(1);
	}

	public void SetAsRand(double length)
	{
		X = MathsHelper.RandomDouble(-1,1);
		Y = MathsHelper.RandomDouble(-1,1);

		Normalise();
		Scale(length);
	}

    public void Rotate(double theta)
    {
        double x = X;
        double y = Y;

        X = (x  * Math.cos(theta)) - (y * Math.sin(theta));
        Y = (x  * Math.sin(theta)) + (y * Math.cos(theta));
    }

    public void Scale(double scale)
    {
        X *= scale;
        Y *= scale;
    }

    public void Add(double x, double y)
    {
        X += x;
        Y += y;
    }

    public void Subtract(double x, double y)
    {
        X -= x;
        Y -= y;
    }
	
	public void SetVectorAsInverse(Vector2 vector)
	{
		X = -vector.X;
		Y = -vector.Y;
	}
	
	public void SetVectorDifference(Vector2 A, Vector2 B)
	{
		X = B.X - A.X;
		Y = B.Y - A.Y;
	}
	
	public static double DotProduct(Vector2 A, Vector2 B)
	{
		return (A.X * B.X) + (A.Y * B.Y);
	}
	
	public static double RadiansBetween(Vector2 A, Vector2 B)
	{
		return Math.acos(DotProduct(A,B) / (A.GetLength() * B.GetLength()));
	}
	
	public static double Determinant(Vector2 A, Vector2 B)
	{
		return (A.X * B.Y) - (B.X * A.Y);
	}
	
	public void Normalise()
	{	
		double Length = GetLength();
		if(GetLengthSqr() != 0.0)
		{
			X /= Length;
			Y /= Length;
		}
	}

	public double GetLengthSqr()
	{
		return ((X * X) + (Y * Y));
	}
	
	public double GetLength()
	{
		return Math.sqrt((X * X) + (Y * Y));
	}
	
	public void Output()
	{
		Log.e("Vector2", "X: " + X);
		Log.e("Vector2", "Y: " + Y);
	}
}
