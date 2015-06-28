package com.raggamuffin.protorunnerv2.utils;

public class Matrix44 
{
	private double[][] Element;	//[column][row]
	
	public Matrix44()
	{
		Element = new double[4][4];
		
		SetIdentity();
	}
	
	public Matrix44(Matrix44 mat)
	{
		Element = new double[4][4];
		
		for(int c = 0; c < 4; c++)
		{
			for(int r = 0; r < 4; r++)
			{
				Element[c][r] = mat.Element[c][r];
			}
		}
	}

	
	public void SetIdentity()
	{
		for(int c = 0; c < 4; c++)
		{
			for(int r = 0; r < 4; r++)
			{
				Element[c][r] = 0.0;
			}
		}

		Element[0][0] = 1.0;
		Element[1][1] = 1.0;
		Element[2][2] = 1.0;
		Element[3][3] = 1.0;
	}
	
	public void SetTranslation(Vector3 translation)
	{
		SetIdentity();
		
		Element[0][3] = translation.I;
		Element[1][3] = translation.J;
		Element[2][3] = translation.K;
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
		
		mat.Element[0][0] = 1.0 - 2.0 * (J2 + K2);
		mat.Element[1][0] = 2.0 * (IJ - WK);
		mat.Element[2][0] = 2.0 * (IK + WJ);
		mat.Element[3][0] = 0.0;
		
		mat.Element[0][1] = 2.0 * (IJ + WK);
		mat.Element[1][1] = 1.0 - 2.0 * (I2 + K2);
		mat.Element[2][1] = 2.0 * (JK - WI);
		mat.Element[3][1] = 0.0;
		
		mat.Element[0][2] = 2.0 * (IK - WJ);
		mat.Element[1][2] = 2.0 * (JK + WI);
		mat.Element[2][2] = 1.0 - 2.0 * (I2 + K2);
		mat.Element[3][2] = 0.0;
		
		mat.Element[0][3] = 0.0;
		mat.Element[1][3] = 0.0;
		mat.Element[2][3] = 0.0;
		mat.Element[3][3] = 1.0;
	}
	
	public void Multiply(final Matrix44 lhs, final Matrix44 rhs)
	{
		for(int c = 0; c < 4; c++)
		{
			for(int r = 0; r < 4; r++)
			{
				Element[c][r] = (lhs.Element[0][r] * rhs.Element[c][0]) + 
							    (lhs.Element[1][r] * rhs.Element[c][1]) + 
							    (lhs.Element[2][r] * rhs.Element[c][2]) + 
							    (lhs.Element[3][r] * rhs.Element[c][3]) ;					
			}
		}
	}
	
	
}
