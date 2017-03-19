package com.raggamuffin.protorunnerv2.utils;

import android.graphics.Point;
import android.util.Log;

public class Vector2 
{
	public static final Vector2 RIGHT   = new Vector2(1, 0);
	public static final Vector2 FORWARD = new Vector2(0, 1);

	public double I;
	public double J;

	public Vector2()
	{
		I = 0.0f;
		J = 0.0f;
	}
	
	public Vector2(double i, double j)
	{
		I = i;
		J = j;
	}
	
	public Vector2(double scale)
	{
		I = scale;
		J = scale;
	}
	
	public Vector2(Vector2 vector)
	{
		I = vector.I;
		J = vector.J;
	}
	
	public Vector2(Point size)
	{
		I = size.x;
		J = size.y;
	}

	public void SetVector(double i, double j)
	{
		I = i;
		J = j;
	}
	
	public void SetVector(double scale)
	{
		I = scale;
		J = scale;
	}
	
	public void SetVector(Vector2 vector)
	{
		I = vector.I;
		J = vector.J;
	}
	
	public void SetVector(Point size)
	{
		I = size.x;
		J = size.y;
	}

	public void SetAsRandNorm()
	{
		SetAsRand(1);
	}

	public void SetAsRand(double length)
	{
		I = MathsHelper.RandomDouble(-1,1);
		J = MathsHelper.RandomDouble(-1,1);

		Normalise();
		Scale(length);
	}

    public void Rotate(double theta)
    {
        double x = I;
        double y = J;

        I = (x  * Math.cos(theta)) - (y * Math.sin(theta));
        J = (x  * Math.sin(theta)) + (y * Math.cos(theta));
    }

    public void Scale(double scale)
    {
        I *= scale;
        J *= scale;
    }

    public void Add(double x, double y)
    {
        I += x;
        J += y;
    }

    public void Subtract(double x, double y)
    {
        I -= x;
        J -= y;
    }
	
	public void SetVectorAsInverse(Vector2 vector)
	{
		I = -vector.I;
		J = -vector.J;
	}
	
	public void SetVectorDifference(Vector2 A, Vector2 B)
	{
		I = B.I - A.I;
		J = B.J - A.J;
	}
	
	public static double DotProduct(Vector2 A, Vector2 B)
	{
		return (A.I * B.I) + (A.J * B.J);
	}
	
	public static double RadiansBetween(Vector2 A, Vector2 B)
	{
		return Math.acos(DotProduct(A,B) / (A.GetLength() * B.GetLength()));
	}
	
	public static double Determinant(Vector2 A, Vector2 B)
	{
		return (A.I * B.J) - (B.I * A.J);
	}
	
	public void Normalise()
	{	
		double Length = GetLength();
		if(GetLengthSqr() != 0.0)
		{
			I /= Length;
			J /= Length;
		}
	}

	public double GetLengthSqr()
	{
		return ((I * I) + (J * J));
	}
	
	public double GetLength()
	{
		return Math.sqrt((I * I) + (J * J));
	}
	
	public void Output()
	{
		Log.e("Vector2", "X: " + I);
		Log.e("Vector2", "Y: " + J);
	}
}
