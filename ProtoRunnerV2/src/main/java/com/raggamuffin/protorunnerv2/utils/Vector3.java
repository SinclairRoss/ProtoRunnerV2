package com.raggamuffin.protorunnerv2.utils;

import android.util.Log;

public final class Vector3
{
    public enum Component
    {
        X,
        Y,
        Z
    }

    public static final Vector3 FORWARD = new Vector3(0, 0, 1);
    public static final Vector3 BACK = new Vector3(0, 0, -1);
    public static final Vector3 UP = new Vector3(0, 1, 0);
    public static final Vector3 RIGHT = new Vector3(1, 0, 0);
    public static final Vector3 ZERO = new Vector3(0);
    public static final Vector3 ONE = new Vector3(1);

    private static float[] Array = new float[3];

    public double X;
    public double Y;
    public double Z;

    public Vector3()
    {
        X = 0.0;
        Y = 0.0;
        Z = 0.0;
    }

    public Vector3(double x, double y, double z)
    {
        X = x;
        Y = y;
        Z = z;
    }

    public Vector3(double scale)
    {
        X = scale;
        Y = scale;
        Z = scale;
    }

    public Vector3(final Vector3 vector)
    {
        X = vector.X;
        Y = vector.Y;
        Z = vector.Z;
    }

    public void SetVector(double i, double j, double k)
    {
        X = i;
        Y = j;
        Z = k;
    }

    public void SetVector(double scale)
    {
        X = scale;
        Y = scale;
        Z = scale;
    }

    public void SetVector(final Vector3 vector)
    {
        X = vector.X;
        Y = vector.Y;
        Z = vector.Z;
    }

    public void SetVectorAsInverse(final Vector3 vector)
    {
        X = -vector.X;
        Y = -vector.Y;
        Z = -vector.Z;
    }

    public void ToFloatArray(float[] outArray)
    {
        outArray[0] = (float)X;
        outArray[1] = (float)Y;
        outArray[2] = (float)Z;
    }

    public void SetVectorAsCrossProduct(final Vector3 a, final Vector3 b)
    {
        X = (a.Y * b.Z) - (b.Y * a.Z);
        Y = -((a.X * b.Z) - (b.X * a.Z));
        Z = (a.X * b.Y) - (b.X * a.Y);
    }

    public void SetAsRandNorm()
    {
        SetAsRand(1);
    }

    public void SetAsRand(double length)
    {
        X = MathsHelper.RandomDouble(-1, 1);
        Y = MathsHelper.RandomDouble(-1, 1);
        Z = MathsHelper.RandomDouble(-1, 1);

        Normalise();
        Scale(length);
    }

    public void SetVectorAsDifference(final Vector3 A, final Vector3 B)
    {
        X = B.X - A.X;
        Y = B.Y - A.Y;
        Z = B.Z - A.Z;
    }

    public void Normalise()
    {
        double lengthSqr = GetLengthSqr();

        if(lengthSqr < 0.9999998 || lengthSqr > 1.0000001)
        {
            double length = Math.sqrt(lengthSqr);
            if (length > 0.000001)
            {
                X /= length;
                Y /= length;
                Z /= length;
            }
        }
    }

    public void SumOf(final Vector3 A, final Vector3 B)
    {
        X = A.X + B.X;
        Y = A.Y + B.Y;
        Z = A.Z + B.Z;
    }

    public void Add(final Vector3 vec)
    {
        X += vec.X;
        Y += vec.Y;
        Z += vec.Z;
    }

    public void Add(double i, double j, double k)
    {
        X += i;
        Y += j;
        Z += k;
    }

    public void Subtract(final Vector3 vec)
    {
        X -= vec.X;
        Y -= vec.Y;
        Z -= vec.Z;
    }

    public void Subtract(double x, double y, double z)
    {
        X -= x;
        Y -= y;
        Z -= z;
    }

    public void Multiply(final Vector3 vec)
    {
        X *= vec.X;
        Y *= vec.Y;
        Z *= vec.Z;
    }

    public void Scale(double scale)
    {
        X *= scale;
        Y *= scale;
        Z *= scale;
    }

    public void RotateX(double theta)
    {
        double y = Y;
        double z = Z;

        Y = (y * Math.cos(theta)) + (z * Math.sin(theta));
        Z = -(y * Math.sin(theta)) + (z * Math.cos(theta));
    }

    public void RotateY(double theta)
    {
        double x = X;
        double z = Z;

        X = (x * Math.cos(theta)) - (z * Math.sin(theta));
        Z = (x * Math.sin(theta)) + (z * Math.cos(theta));
    }

    public void RotateZ(double theta)
    {
        double x = X;
        double y = Y;

        X = (x * Math.cos(theta)) + (y * -Math.sin(theta));
        Y = (x * Math.sin(theta)) + (y * Math.cos(theta));
    }

    public void RotateAboutAxis(Vector3 axis, double theta)
    {
        double x = X;
        double y = Y;
        double z = Z;

        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);

        X = x * (cosTheta + (axis.X * axis.X * (1 - cosTheta))) +
            y * (axis.Y * axis.X * (1 - cosTheta) + axis.Z * sinTheta) +
            z * (axis.Z * axis.X * (1 - cosTheta) - axis.Y * sinTheta);

        Y = x * (axis.X * axis.Y * (1 - cosTheta) - axis.Z * sinTheta) +
            y * (cosTheta + axis.Y * axis.Y * (1 - cosTheta)) +
            z * (axis.Z * axis.Y * (1 - cosTheta) + axis.X * sinTheta);

        Z = x * (axis.X * axis.Z * (1 - cosTheta) + axis.Y * sinTheta) +
            y * (axis.Y * axis.Z * (1 - cosTheta) - axis.X * sinTheta) +
            z * (cosTheta + (axis.Z * axis.Z) * (1 - cosTheta));
    }

    public void Multiply(Quaternion a)
    {
        double crossX = (a.Axis.Y * Z) - (Y * a.Axis.Z);
        double crossY = -((a.Axis.X * Z) - (X * a.Axis.Z));
        double crossZ = (a.Axis.X * Y) - (X * a.Axis.Y);

        X += (crossX * 2 * a.W) + (((a.Axis.Y * crossZ) - (crossY * a.Axis.Z)) * 2);
        Y += (crossY * 2 * a.W) + ((-((a.Axis.X * crossZ) - (crossX * a.Axis.Z))) * 2);
        Z += (crossZ * 2 * a.W) + (((a.Axis.X * crossY) - (crossX * a.Axis.Y)) * 2);
    }

    public void Rotate(Quaternion a)
    {
        double dot = DotProduct(a.Axis, this);
        double s = (a.W * a.W - dot);

        double crossX = (a.Axis.Y * Z) - (Y * a.Axis.Z);
        double crossY = -(a.Axis.X * Z) - (X * a.Axis.Z);
        double crossZ = (a.Axis.X * Y) - (X * a.Axis.Y);

        X = (2 * dot * a.Axis.X) + (s * X) + (2 * a.W * crossX);
        Y = (2 * dot * a.Axis.Y) + (s * Y) + (2 * a.W * crossY);
        Z = (2 * dot * a.Axis.Z) + (s * Z) + (2 * a.W * crossZ);

    }

	public static double DotProduct(Vector3 A, Vector3 B)
	{
		return (A.X * B.X) + (A.Y * B.Y) + (A.Z * B.Z);
	}
	
	public static double RadiansBetween(Vector3 A, Vector3 B)
	{
		double rad = Math.acos(DotProduct(A,B) / (A.GetLength() * B.GetLength()));
        return rad == rad ? rad : 0.0;
	}

    public static double DistanceBetween(Vector3 a, Vector3 b)
    {
        return Math.sqrt(DistanceBetweenSqr(a, b));
    }

    public static double DistanceBetweenSqr(Vector3 a, Vector3 b)
    {
        double i = b.X - a.X;
        double j = b.Y - a.Y;
        double k = b.Z - a.Z;

        return i*i + j*j + k*k;
    }

	public double Yaw()
	{
        return RadiansBetween(Vector3.FORWARD, this);
	}

	public static double Determinant(Vector3 A, Vector3 B)
	{
		return ((A.Y * B.Z) - (A.Z * B.Y)) - ((A.X * B.Z) - (A.Z * B.X)) + ((A.X * B.Y) - (A.Y * B.X));
	}

    public void Lerp(Vector3 start, Vector3 end, double amount)
    {
        X = MathsHelper.Lerp(amount, start.X, end.X);
        Y = MathsHelper.Lerp(amount, start.Y, end.Y);
        Z = MathsHelper.Lerp(amount, start.Z, end.Z);
    }
	
	public double GetLengthSqr()
	{
		return ((X * X) + (Y * Y) + (Z * Z));
	}
	
	public double GetLength()
	{
		return Math.sqrt(GetLengthSqr());
	}

	public static double GetDistanceBetweenSqr(Vector3 a, Vector3 b)
	{
		double i = b.X - a.X;
		double j = b.Y - a.Y;
		double k = b.Z - a.Z;

		return ((i * i) + (j * j) + (k * k));
	}

	public static double GetDistanceBetween(Vector3 a, Vector3 b)
	{
		return Math.sqrt(GetDistanceBetweenSqr(a,b));
	}

	public boolean IsNan()
	{
		return X != X || Y != Y || Z != Z;
	}

	public Component GetMajorComponent()
    {
        double x = Math.abs(X);
        double y = Math.abs(Y);
        double z = Math.abs(Z);

        if(x > y && x > z)
            return Component.X;

        if(z > x && z > y)
            return Component.Z;

        if(y > x && y > z)
            return Component.Y;

        return null;
    }

    public float[] AsArray()
    {
        Array[0] = (float)X;
        Array[1] = (float)Y;
        Array[2] = (float)Z;

        return Array;
    }
	
	public void Output(String tag)
	{
		Log.e(tag, "--------------");
		Log.e(tag, "X: " + X);
		Log.e(tag, "Y: " + Y);
		Log.e(tag, "Z: " + Z);
	}
}
