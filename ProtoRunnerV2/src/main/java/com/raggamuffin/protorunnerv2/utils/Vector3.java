package com.raggamuffin.protorunnerv2.utils;

import android.util.Log;

public final class Vector3 
{
    public static final Vector3 RIGHT   = new Vector3(1,0,0);
    public static final Vector3 UP      = new Vector3(0,1,0);
    public static final Vector3 DOWN    = new Vector3(0,-1,0);
    public static final Vector3 FORWARD = new Vector3(0,0,1);

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

        Normalise();
    }

    public void SetAsRandNorm()
    {
        SetAsRand(1);
    }

	public void SetAsRand(double length)
	{
		I = MathsHelper.RandomDouble(-1,1);
		J = MathsHelper.RandomDouble(-1,1);
		K = MathsHelper.RandomDouble(-1,1);

		Normalise();
        Scale(length);
	}

	public void SetVectorDifference(final Vector3 A, final Vector3 B)
	{
		I = B.I - A.I;
		J = B.J - A.J;
		K = B.K - A.K;
	}
	
	public void Normalise()
	{
		double length = GetLength();
		
		if(length > 0.000001)
		{
			I /= length;
			J /= length;
			K /= length;
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

    public void Subtract(double x, double y, double z)
    {
        I -= x;
        J -= y;
        K -= z;
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

    public void RotateX(double theta)
    {
        double y = J;
        double z = K;

        J =  (y  * Math.cos(theta)) + (z * Math.sin(theta));
        K = -(y  * Math.sin(theta)) + (z * Math.cos(theta));
    }
	
	public void RotateY(double theta)
	{
		double x = I;
		double z = K;
		
		I = (x  * Math.cos(theta)) - (z * Math.sin(theta));
		K = (x  * Math.sin(theta)) + (z * Math.cos(theta));
	}

    public void RotateZ(double theta)
    {
        double x = I;
        double y = J;

        I =  (x  * Math.cos(theta)) + (y * -Math.sin(theta));
        J =  (x  * Math.sin(theta)) + (y * Math.cos(theta));
    }
	
	public static double DotProduct(Vector3 A, Vector3 B)
	{
		return (A.I * B.I) + (A.J * B.J) + (A.K * B.K);
	}
	
	public static double RadiansBetween(Vector3 A, Vector3 B)
	{
		double rad = Math.acos(DotProduct(A,B) / (A.GetLength() * B.GetLength()));

        return rad == rad ? rad : 0.0;
	}

    public static double DistanceBetween(Vector3 a, Vector3 b)
    {
        double i = b.I - a.I;
        double j = b.J - a.J;
        double k = b.K - a.K;

        return Math.sqrt(i*i + j*j + k*k);
    }

    public static double DistanceBetweenSqr(Vector3 a, Vector3 b)
    {
        double i = b.I - a.I;
        double j = b.J - a.J;
        double k = b.K - a.K;

        return i*i + j*j + k*k;
    }

    public double Pitch()
    {
        double val = Math.atan(J / K);

        if(K < 0.0)
            val += (Math.PI - val) * 2;

        return val == val ? val : 0.0;
    }

    public double Yaw()
    {
        double val = Math.PI * 2 - Math.atan(I / K);

        if(K < 0.0)
            val += Math.PI;

        return val == val ? val : 0.0;
    }
	
	public static double Determinant(Vector3 A, Vector3 B)
	{
		return ((A.J * B.K) - (A.K * B.J)) - ((A.I * B.K) - (A.K * B.I)) + ((A.I * B.J) - (A.J * B.I));
	}

    public void Lerp(Vector3 start, Vector3 end, double amount)
    {
        I = MathsHelper.Lerp(amount, start.I, end.I);
        J = MathsHelper.Lerp(amount, start.J, end.J);
        K = MathsHelper.Lerp(amount, start.K, end.K);
    }
	
	public double GetLengthSqr()
	{
		return ((I * I) + (J * J) + (K * K));
	}
	
	public double GetLength()
	{
		return Math.sqrt((I * I) + (J * J) + (K * K));
	}

	public boolean IsNan()
	{
		if(I != I)
			return true;
		
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
