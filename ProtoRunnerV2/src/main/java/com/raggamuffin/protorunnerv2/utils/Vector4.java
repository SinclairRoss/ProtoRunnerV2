package com.raggamuffin.protorunnerv2.utils;

public class Vector4 
{
	public double I;
	public double J;
	public double K;
	public double W;
	
	public Vector4()
	{
		I = 0.0;
		J = 0.0;
		K = 0.0;
		W = 0.0;
	}
	
	public Vector4(double i, double j, double k, double w)
	{
		I = i;
		J = j;
		K = k;
		W = w;
	}
	
	public Vector4(double scale)
	{
		I = scale;
		J = scale;
		K = scale;
		W = scale;
	}
	
	public Vector4(Vector4 vector)
	{
		I = vector.I;
		J = vector.J;
		K = vector.K;
		W = vector.W;
	}
	
	public void SetVector(double i, double j, double k, double w)
	{
		I = i;
		J = j;
		K = k;
		W = w;
	}
	
	public void SetVector(double scale)
	{
		I = scale;
		J = scale;
		K = scale;
		W = scale;
	}
	
	public void SetVector(Vector4 vector)
	{
		I = vector.I;
		J = vector.J;
		K = vector.K;
		W = vector.W;
	}
	
	public void SetVector(Colour colour)
	{
		I = colour.Red;
		J = colour.Green;
		K = colour.Blue;
		W = colour.Alpha;
	}
	
	public void SetVectorAsInverse(Vector4 vector)
	{
		I = -vector.I;
		J = -vector.J;
		K = -vector.K;
		W = -vector.W;
	}
	
	public void SetVectorDifference(Vector4 A, Vector4 B)
	{
		I = B.I - A.I;
		J = B.J - A.J;
		K = B.K - A.K;
		W = B.W - A.W;
	}
	
	public void Normalise()
	{
		double Length = GetLength();
		
		if(Length != 0.0)
		{
			I /= Length;
			J /= Length;
			K /= Length;
			W /= Length;
		}
	}
	
	public void Add(final Vector4 vec)
	{
		I += vec.I;
		J += vec.J;
		K += vec.K;
		W += vec.W;
	}
	
	public void Subtract(final Vector4 vec)
	{
		I -= vec.I;
		J -= vec.J;
		K -= vec.K;
		W -= vec.W;
	}
	
	public void Scale(double scale)
	{
		I *= scale;
		J *= scale;
		K *= scale;
		W *= scale;
	}
	
	public double GetLengthSqr()
	{
		return ((I * I) + (J * J) + (K * K) + (W * W));
	}
	
	public double GetLength()
	{
		return Math.sqrt((I * I) + (J * J) + (K * K) + (W * W));
	}
}
