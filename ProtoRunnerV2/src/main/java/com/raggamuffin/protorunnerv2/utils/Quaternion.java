package com.raggamuffin.protorunnerv2.utils;

// Author: Sinclair Ross
// Date:   23/04/2017

import android.util.Log;

public class Quaternion
{
    public Vector3 Axis;
    public double W;

    public Quaternion()
    {
        Axis = new Vector3();
        W = 1;
    }

    public Quaternion(Vector3 axis, double rotation)
    {
        Axis = new Vector3();
        SetQuaternion(axis, rotation);
    }

    public Quaternion(Quaternion a)
    {
        Axis = new Vector3();
        SetQuaternion(a);
    }

    public void Reset()
    {
        Axis.Scale(0);
        W = 1;
    }

    public void SetQuaternion(Vector3 axis, double rotation)
    {
        SetQuaternion(axis.X, axis.Y, axis.Z, rotation);
    }

    public void SetQuaternion(double x, double y, double z, double rotation)
    {
        double halfRotation = rotation / 2;

        Axis.X = x * Math.sin(halfRotation);
        Axis.Y = y * Math.sin(halfRotation);
        Axis.Z = z * Math.sin(halfRotation);
        W = -Math.cos(halfRotation);

        Normalise();
    }

    public void SetRotation(double rotation)
    {
        double halfRotation = rotation / 2;
        W = -Math.cos(halfRotation);
    }

    public void SetQuaternion(Quaternion a)
    {
        Axis.X = a.Axis.X;
        Axis.Y = a.Axis.Y;
        Axis.Z = a.Axis.Z;
        W = a.W;
    }

    public void SetQuaternionAsProductOf(Quaternion a, Quaternion b)
    {
        double x1 = b.Axis.X;
        double y1 = b.Axis.Y;
        double z1 = b.Axis.Z;
        double w1 = b.W;

        double x2 = a.Axis.X;
        double y2 = a.Axis.Y;
        double z2 = a.Axis.Z;
        double w2 = a.W;

        Axis.X = (w1 * x2) + (x1 * w2) + (y1 * z2) - (z1 * y2);
        Axis.Y = (w1 * y2) - (x1 * z2) + (y1 * w2) + (z1 * x2);
        Axis.Z = (w1 * z2) + (x1 * y2) - (y1 * x2) + (z1 * w2);
        W = (w1 * w2) - (x1 * x2) - (y1 * y2) - (z1 * z2);
    }

    public void Multiply(Quaternion a)
    {
        SetQuaternionAsProductOf(a, this);
    }

    public void Normalise()
    {
        double lengthSqr = CalculateLengthSqr();

        if((lengthSqr <= 0.9999999 || lengthSqr >= 1.0000001) && lengthSqr > 0)
        {
            double length = Math.sqrt(lengthSqr);

            Axis.X /= length;
            Axis.Y /= length;
            Axis.Z /= length;
            W /= length;
        }
    }

    private static Quaternion dest = new Quaternion();
    public void Slerp(Quaternion origin, Quaternion destination, double t)
    {
        dest.SetQuaternion(destination);
        double flCosOmega = (origin.W * destination.W) + Vector3.DotProduct(destination.Axis, origin.Axis);

        if(flCosOmega < 0)
        {
            dest.W = -dest.W;
            dest.Axis.Scale(-1);
            flCosOmega = -flCosOmega;
        }

        double k0;
        double k1;

        if(flCosOmega > 0.9999)
        {
            k0 = 1.0 - t;
            k1 = t;
        }
        else
        {
            double flSinOmega = Math.sqrt(1.0 - (flCosOmega*flCosOmega));
            double flOmega = Math.atan2(flSinOmega, flCosOmega);
            double flOneOverSinOmega = 1/flSinOmega;

            k0 = Math.sin((1.0 - t) * flOmega) * flOneOverSinOmega;
            k1 = Math.sin(t * flOmega) * flOneOverSinOmega;
        }

        W = (origin.W * k0) + (dest.W * k1);
        Axis.X = (origin.Axis.X * k0) + (dest.Axis.X * k1);
        Axis.Y = (origin.Axis.Y * k0) + (dest.Axis.Y * k1);
        Axis.Z = (origin.Axis.Z * k0) + (dest.Axis.Z * k1);
    }

    public double CalculateLengthSqr() { return (W*W) + Axis.GetLengthSqr(); }
    public double CalculateLength() { return Math.sqrt(CalculateLengthSqr()); }

    public boolean IsNan()
    {
        return (Axis.IsNan() || W != W);
    }

    public void Output(String tag)
    {
        Log.e(tag, "--------------");
        Log.e(tag, "X: " + Axis.X);
        Log.e(tag, "Y: " + Axis.Y);
        Log.e(tag, "Z: " + Axis.Z);
        Log.e(tag, "W: " + W);
    }
}