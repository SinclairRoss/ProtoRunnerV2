package com.raggamuffin.protorunnerv2.utils;

// Author: Sinclair Ross
// Date:   12/02/2017

public class Matrix
{
    private double[] m_Elements;

    public Matrix()
    {
        m_Elements = new double[16];
        SetIdentity();
    }

    public void SetIdentity()
    {
        m_Elements[0] = 1.0;
        m_Elements[1] = 0.0;
        m_Elements[2] = 0.0;
        m_Elements[3] = 0.0;

        m_Elements[4] = 0.0;
        m_Elements[5] = 1.0;
        m_Elements[6] = 0.0;
        m_Elements[7] = 0.0;

        m_Elements[8] = 0.0;
        m_Elements[9] = 0.0;
        m_Elements[10] = 1.0;
        m_Elements[11] = 0.0;

        m_Elements[12] = 0.0;
        m_Elements[13] = 0.0;
        m_Elements[14] = 0.0;
        m_Elements[15] = 1.0;
    }

    // <----- Setters -----> \\
    public void SetPosition(double x, double y, double z)
    {
        m_Elements[12] = x;
        m_Elements[13] = y;
        m_Elements[14] = z;
    }

    public void SetUp(double x, double y, double z)
    {
        m_Elements[4] = x;
        m_Elements[5] = y;
        m_Elements[6] = z;
    }

    public void SetForward(double x, double y, double z)
    {
        m_Elements[8] = x;
        m_Elements[9] = y;
        m_Elements[10] = z;
    }

    public void SetRight(double x, double y, double z)
    {
        m_Elements[0] = x;
        m_Elements[1] = y;
        m_Elements[2] = z;
    }

    // <----- Getters -----> \\
    // Position
    public double GetPosition_x() { return m_Elements[12]; }
    public double GetPosition_y() { return m_Elements[13]; }
    public double GetPosition_z() { return m_Elements[14]; }
    public void GetPosition(Vector3 out) { out.SetVector(m_Elements[12], m_Elements[13], m_Elements[14]); }

    // Up
    public double GetUp_x() { return m_Elements[4]; }
    public double GetUp_y() { return m_Elements[5]; }
    public double GetUp_z() { return m_Elements[6]; }
    public void GetUp(Vector3 out) {out.SetVector(m_Elements[4], m_Elements[5], m_Elements[6]);}

    // Forward
    public double GetForward_x() { return m_Elements[8]; }
    public double GetForward_y() { return m_Elements[9]; }
    public double GetForward_z() { return m_Elements[10]; }
    public void GetForward(Vector3 out) {out.SetVector(m_Elements[8], m_Elements[9], m_Elements[10]);}

    // Right
    public double GetRight_x() { return m_Elements[0]; }
    public double GetRight_y() { return m_Elements[1]; }
    public double GetRight_z() { return m_Elements[2]; }
    public void GetRight(Vector3 out) {out.SetVector(m_Elements[0], m_Elements[1], m_Elements[2]);}

    public void SetMatrixFromUnitQuaternion(Quaternion a)
    {
        m_Elements[0] = 1 - (2 * (a.Axis.Y * a.Axis.Y)) - (2 * (a.Axis.Z * a.Axis.Z));
        m_Elements[1] = ((2 * a.Axis.X * a.Axis.Y) - (2 * a.W * a.Axis.Z));
        m_Elements[2] = ((2 * a.Axis.X * a.Axis.Z) + (2 * a.W * a.Axis.Y));
        m_Elements[3] = 0.0;

        m_Elements[4] = ((2 * a.Axis.X * a.Axis.Y) + (2 * a.W * a.Axis.Z));
        m_Elements[5] = 1 - (2 * (a.Axis.X * a.Axis.X)) - (2 * (a.Axis.Z * a.Axis.Z));
        m_Elements[6] = ((2 * a.Axis.Y * a.Axis.Z) + (2 * a.W * a.Axis.X));
        m_Elements[7] = 0.0;

        m_Elements[8] = ((2 * a.Axis.X * a.Axis.Z) - (2 * a.W * a.Axis.Y));
        m_Elements[9] = ((2 * a.Axis.Y * a.Axis.Z) - (2 * a.W * a.Axis.X));
        m_Elements[10] = 1 - (2 * (a.Axis.X * a.Axis.X)) - (2 * (a.Axis.Y * a.Axis.Y));
        m_Elements[11] = 0.0;

        m_Elements[12] = 0.0;
        m_Elements[13] = 0.0;
        m_Elements[14] = 0.0;
        m_Elements[15] = 1.0;
    }
}
