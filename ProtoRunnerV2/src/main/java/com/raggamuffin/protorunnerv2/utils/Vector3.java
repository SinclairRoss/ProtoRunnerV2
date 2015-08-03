package com.raggamuffin.protorunnerv2.utils;

import android.util.Log;

public final class Vector3 
{
	public double I;
	public double J;
	public double K;
	
	public Vector3()
	{
		I = 0.0;
		J = 0.0;
		K = 0.0;
	}
	
	public Vector3(double i, double j, double k)
	{
		I = i;
		J = j;
		K = k;
	}
	
	public Vector3(double scale)
	{
		I = scale;
		J = scale;
		K = scale;
	}
	
	public Vector3(final Vector3 vector)
	{
		I = vector.I;
		J = vector.J;
		K = vector.K;
	}
	
	public void SetVector(double i, double j, double k)
	{
		I = i;
		J = j;
		K = k;
	}
	
	public void SetVector(double scale)
	{
		I = scale;
		J = scale;
		K = scale;
	}
	
	public void SetVector(final Vector3 vector)
	{
		I = vector.I;
		J = vector.J;
		K = vector.K;
	}
	
	public void SetVectorAsInverse(final Vector3 vector)
	{
		I = -vector.I;
		J = -vector.J;
		K = -vector.K;
	}

    public void SetAsCrossProduct(final Vector3 a, final Vector3 b)
    {
        I = (a.J * b.K) - (b.J * a.K);
        J = (a.I * b.K) - (b.I * a.K);
        K = (a.I * b.J) - (b.I * a.J);
    }
	
	public void SetVectorDifference(final Vector3 A, final Vector3 B)
	{
		I = B.I - A.I;
		J = B.J - A.J;
		K = B.K - A.K;
	}
	
	public void Normalise()
	{
		double Length = GetLength();
		
		if(Math.abs(Length) > 0.000001)
		{
			I /= Length;
			J /= Length;
			K /= Length;
		}
	}
	
	public void SumOf(final Vector3 A, final Vector3 B)
	{
		I = A.I + B.I;
		J = A.J + B.J;
		K = A.K + B.K;
	}
	
	public void Add(final Vector3 vec)
	{
		I += vec.I;
		J += vec.J;
		K += vec.K;
	}
	
	public void Add(double i, double j, double k)
	{
		I += i;
		J += j;
		K += k;
	}
	
	public void Subtract(final Vector3 vec)
	{
		I -= vec.I;
		J -= vec.J;
		K -= vec.K;
	}
	
	public void Multiply(final Vector3 vec)
	{
		I *= vec.I;
		J *= vec.J;
		K *= vec.K;
	}
	
	public void Scale(double scale)
	{
		I *= scale;
		J *= scale;
		K *= scale;
	}

    public void RotateX(double Theta)
    {
        double y = J;
        double z = K;

        J =  (y  * Math.cos(Theta)) + (z * Math.sin(Theta));
        K = -(y  * Math.sin(Theta)) + (z * Math.cos(Theta));
    }
	
	public void RotateY(double Theta)
	{
		double x = I;
		double z = K;
		
		I = (x  * Math.cos(Theta)) - (z * Math.sin(Theta));
		K = (x  * Math.sin(Theta)) + (z * Math.cos(Theta));
	}

    public void RotateZ(double Theta)
    {
        double x = I;
        double y = J;

        I =  (x  * Math.cos(Theta)) + (y * -Math.sin(Theta));
        J =  (x  * Math.sin(Theta)) + (y * Math.cos(Theta));
    }
	
	public static double DotProduct(Vector3 A, Vector3 B)
	{
		return (A.I * B.I) + (A.J * B.J) + (A.K * B.K);
	}
	
	public static double RadiansBetween(Vector3 A, Vector3 B)
	{
		double rad = Math.acos(DotProduct(A,B) / (A.GetLength() * B.GetLength()));
		
		if(rad != rad)
			return 0.0;
		
		return rad;
	}
	
	public static double Determinant(Vector3 A, Vector3 B)
	{
		return ((A.J * B.K) - (A.K * B.J)) - ((A.I * B.K) - (A.K * B.I)) + ((A.I * B.J) - (A.J * B.I));
	}
	
	public double GetLengthSqr()
	{
		return ((I * I) + (J * J) + (K * K));
	}
	
	public double GetLength()
	{
		return  Math.sqrt((I * I) + (J * J) + (K * K));
	}
	
	// Check for Nan errors.
	public boolean IsNan()
	{
		if(I != I)
			return false;
		
		if(J != J)
			return true;
		
		if(K != K)
			return true;
		
		return false;
	}
	
	public void Output(String tag)
	{
		Log.e(tag, "--------------");
		Log.e(tag, "I: " + I);
		Log.e(tag, "J: " + J);
		Log.e(tag, "K: " + K);
	}
}
