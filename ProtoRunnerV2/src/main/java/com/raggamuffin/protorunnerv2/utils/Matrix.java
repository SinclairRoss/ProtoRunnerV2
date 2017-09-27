package com.raggamuffin.protorunnerv2.utils;

// Author: Sinclair Ross
// Date:   12/02/2017

public class Matrix
{
    public float[] Elements;

    public Matrix()
    {
        Elements = new float[16];
        SetIdentity();
    }

    public void SetIdentity()
    {
        Elements[0] = 1.0f;
        Elements[1] = 0.0f;
        Elements[2] = 0.0f;
        Elements[3] = 0.0f;

        Elements[4] = 0.0f;
        Elements[5] = 1.0f;
        Elements[6] = 0.0f;
        Elements[7] = 0.0f;

        Elements[8] = 0.0f;
        Elements[9] = 0.0f;
        Elements[10] = 1.0f;
        Elements[11] = 0.0f;

        Elements[12] = 0.0f;
        Elements[13] = 0.0f;
        Elements[14] = 0.0f;
        Elements[15] = 1.0f;
    }

    // <----- Setters -----> \\
    public void SetPosition(float x, float y, float z)
    {
        Elements[12] = x;
        Elements[13] = y;
        Elements[14] = z;
    }

    public void SetPosition(Vector3 pos)
    {
        Elements[12] = (float)pos.X;
        Elements[13] = (float)pos.Y;
        Elements[14] = (float)pos.Z;
    }

    public void SetUp(float x, float y, float z)
    {
        Elements[4] = x;
        Elements[5] = y;
        Elements[6] = z;
    }

    public void SetUp(Vector3 up)
    {
        Elements[4] = (float)up.X;
        Elements[5] = (float)up.Y;
        Elements[6] = (float)up.Z;
    }

    public void SetForward(float x, float y, float z)
    {
        Elements[8] = x;
        Elements[9] = y;
        Elements[10] = z;
    }

    public void SetForward(Vector3 forward)
    {
        Elements[8] = (float)forward.X;
        Elements[9] = (float)forward.Y;
        Elements[10] = (float)forward.Z;
    }

    public void SetRight(double x, double y, double z)
    {
        Elements[0] = (float)x;
        Elements[1] = (float)y;
        Elements[2] = (float)z;
    }

    public void SetRight(Vector3 right)
    {
        SetRight(right.X, right.Y, right.Z);
    }

    public void Scale(Vector3 scale)
    {
        Scale(scale.X, scale.Y, scale.Z);
    }

    public void Scale(double x, double y, double z)
    {
        Elements[0] *= x;
        Elements[1] *= x;
        Elements[2] *= x;

        Elements[4] *= y;
        Elements[5] *= y;
        Elements[6] *= y;

        Elements[8] *= z;
        Elements[9] *= z;
        Elements[10] *= z;
    }

    // <----- Getters -----> \\
    // Position
    public double GetPosition_x() { return Elements[12]; }
    public double GetPosition_y() { return Elements[13]; }
    public double GetPosition_z() { return Elements[14]; }
    public void GetPosition(Vector3 out) { out.SetVector(Elements[12], Elements[13], Elements[14]); }

    // Up
    public double GetUp_x() { return Elements[4]; }
    public double GetUp_y() { return Elements[5]; }
    public double GetUp_z() { return Elements[6]; }
    public void GetUp(Vector3 out) {out.SetVector(Elements[4], Elements[5], Elements[6]);}

    // Forward
    public double GetForward_x() { return Elements[8]; }
    public double GetForward_y() { return Elements[9]; }
    public double GetForward_z() { return Elements[10]; }
    public void GetForward(Vector3 out) {out.SetVector(Elements[8], Elements[9], Elements[10]);}

    // Right
    public double GetRight_x() { return Elements[0]; }
    public double GetRight_y() { return Elements[1]; }
    public double GetRight_z() { return Elements[2]; }
    public void GetRight(Vector3 out) {out.SetVector(Elements[0], Elements[1], Elements[2]);}

    public void SetMatrixFromUnitQuaternion(Quaternion a)
    {
        Elements[0] = (float)(1 - (2 * (a.Axis.Y * a.Axis.Y)) - (2 * (a.Axis.Z * a.Axis.Z)));
        Elements[1] = (float)((2 * a.Axis.X * a.Axis.Y) - (2 * a.W * a.Axis.Z));
        Elements[2] = (float)((2 * a.Axis.X * a.Axis.Z) + (2 * a.W * a.Axis.Y));
        Elements[3] = 0.0f;

        Elements[4] = (float)((2 * a.Axis.X * a.Axis.Y) + (2 * a.W * a.Axis.Z));
        Elements[5] = (float)(1 - (2 * (a.Axis.X * a.Axis.X)) - (2 * (a.Axis.Z * a.Axis.Z)));
        Elements[6] = (float)((2 * a.Axis.Y * a.Axis.Z) + (2 * a.W * a.Axis.X));
        Elements[7] = 0.0f;

        Elements[8] = (float)((2 * a.Axis.X * a.Axis.Z) - (2 * a.W * a.Axis.Y));
        Elements[9] = (float)((2 * a.Axis.Y * a.Axis.Z) - (2 * a.W * a.Axis.X));
        Elements[10] = (float)(1 - (2 * (a.Axis.X * a.Axis.X)) - (2 * (a.Axis.Y * a.Axis.Y)));
        Elements[11] = 0.0f;

        Elements[12] = 0.0f;
        Elements[13] = 0.0f;
        Elements[14] = 0.0f;
        Elements[15] = 1.0f;
    }
}
