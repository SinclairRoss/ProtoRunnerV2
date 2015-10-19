package com.raggamuffin.protorunnerv2.utils;

public class Matrix44 
{
	private double[] Element;
	
	public Matrix44()
	{
		Element = new double[16];
		
		SetIdentity();
	}
	
	public Matrix44(Matrix44 mat)
	{
		Element = new double[16];

        for(int i = 0; i < 4; i++)
            Element[i] = mat.Element[i];
	}

	public void SetIdentity()
	{
        for(int i = 0; i < 4; i++)
            Element[i] = 0.0;

		Element[0]  = 1.0;
		Element[5]  = 1.0;
		Element[10] = 1.0;
		Element[15] = 1.0;
	}
	
	public void SetTranslation(Vector3 translation)
	{
		SetIdentity();
		
		Element[12] = translation.I;
		Element[13] = translation.J;
		Element[14] = translation.K;
	}
	
	public void SetRotation(Quaternion rotation)
	{
		double I2 = rotation.I * rotation.I;
		double J2 = rotation.J * rotation.J;
		double K2 = rotation.K * rotation.K;
		
		double IJ = rotation.I * rotation.J;
		double IK = rotation.I * rotation.K;
		double JK = rotation.J * rotation.K;
		
		double WI = rotation.W * rotation.I;
		double WJ = rotation.W * rotation.J;
		double WK = rotation.W * rotation.K;
		
		Matrix44 mat = new Matrix44();
		
		mat.Element[0] = 1.0 - 2.0 * (J2 + K2);
		mat.Element[1] = 2.0 * (IJ - WK);
		mat.Element[2] = 2.0 * (IK + WJ);
		mat.Element[3] = 0.0;
		
		mat.Element[4] = 2.0 * (IJ + WK);
		mat.Element[5] = 1.0 - 2.0 * (I2 + K2);
		mat.Element[6] = 2.0 * (JK - WI);
		mat.Element[7] = 0.0;
		
		mat.Element[8] = 2.0 * (IK - WJ);
		mat.Element[9] = 2.0 * (JK + WI);
		mat.Element[10] = 1.0 - 2.0 * (I2 + K2);
		mat.Element[11] = 0.0;

//		mat.Element[12] = 0.0;
//		mat.Element[13] = 0.0;
//		mat.Element[14] = 0.0;
//		mat.Element[15] = 1.0;
	}
	
	public void Multiply(final Matrix44 lhs, final Matrix44 rhs)
	{

	}

    public void Translate(Vector3 translation)
    {
        Element[12] += translation.I;
        Element[13] += translation.J;
        Element[14] += translation.K;
    }

    public void Translate(double i, double j, double k)
    {
        Element[12] += i;
        Element[13] += j;
        Element[14] += k;
    }

    ///// Setters
    public void SetRight(Vector3 right)
    {
        Element[0] = right.I;
        Element[1] = right.J;
        Element[2] = right.K;
    }

    public void SetUp(Vector3 up)
    {
        Element[4] = up.I;
        Element[5] = up.J;
        Element[6] = up.K;
    }

    public void SetForward(Vector3 fwd)
    {
        Element[8] = fwd.I;
        Element[9] = fwd.J;
        Element[10] = fwd.K;
    }

    public void SetPosition(Vector3 pos)
    {
        Element[12] = pos.I;
        Element[13] = pos.J;
        Element[14] = pos.K;
    }

    public void SetPosition(double i, double j, double k)
    {
        Element[12] = i;
        Element[13] = j;
        Element[14] = k;
    }

    ///// Getters
    public Vector3 GetRight()
    {
        return new Vector3(Element[0],Element[1],Element[2]);
    }

    public Vector3 GetLeft()
    {
        return new Vector3(-Element[0],-Element[1],-Element[2]);
    }

    public Vector3 GetUp()
    {
        return new Vector3(Element[4],Element[5],Element[6]);
    }

    public Vector3 GetForward()
    {
        return new Vector3(Element[8],Element[9],Element[10]);
    }

    public Vector3 GetPosition()
    {
        return new Vector3(Element[12],Element[13],Element[14]);
    }
}
