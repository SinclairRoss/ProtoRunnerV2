// Cos(Theta / 2) + Sin(Theta / 2)
// W				I J K

package com.raggamuffin.protorunnerv2.utils;

public class Quaternion 
{
	public static final double TOLERANCE = 0.00001;
	public static final double PIOVER180 = Math.PI / 180;
	
	public double W;
	public double I;
	public double J;
	public double K;
	
	private double m_Magnitude;
	
	public Quaternion()
	{
		W = 1;
		I = 0;
		J = 0;
		K = 0;
		
		m_Magnitude = 1.0;
	}
	

	public Quaternion(double theta, double i, double j, double k)
	{
		double halfTheta = theta * 0.5;
		
		W = Math.cos(halfTheta);
		I = i * Math.sin(halfTheta);
		J = j * Math.sin(halfTheta);
		K = k * Math.sin(halfTheta);
		
		Normalise();
	}
	
	public void Set(double theta, double i, double j, double k)
	{
		double halfTheta = theta * 0.5;
		
		W = Math.cos(halfTheta);
		I = i * Math.sin(halfTheta);
		J = j * Math.sin(halfTheta);
		K = k * Math.sin(halfTheta);
		
		Normalise();
	}
	
	public Quaternion(Quaternion quat)
	{
		W = quat.W;
		I = quat.I;
		J = quat.J;
		K = quat.K;
		
		Normalise();
	}
	
	public void Set(Quaternion quat)
	{
		W = quat.W;
		I = quat.I;
		J = quat.J;
		K = quat.K;
		
		Normalise();
	}
	
	public Quaternion(final Vector3 vector, double theta)
	{
		theta = Math.toRadians(theta);
		theta *= 0.5;
	
		double sinTheta = Math.sin(theta);
	
		W = Math.cos(theta);
		I = vector.I * sinTheta;
		J = vector.J * sinTheta;
		K = vector.K * sinTheta;
		
		Normalise();
		
	}
	
	public void Set(final Vector3 vector, double theta)
	{
		theta = Math.toRadians(theta);
		theta *= 0.5;
	
		double sinTheta = Math.sin(theta);
	
		W = Math.cos(theta);
		I = vector.I * sinTheta;
		J = vector.J * sinTheta;
		K = vector.K * sinTheta;
		
		Normalise();
	}
	
	public Quaternion(double pitch, double Jaw, double roll)
	{
		double p = pitch * PIOVER180 * 0.5;
		double J = Jaw   * PIOVER180 * 0.5;
		double r = roll  * PIOVER180 * 0.5;
		
		double sinp = Math.sin(p);
		double sinJ = Math.sin(J);
		double sinr = Math.sin(r);
		double cosp = Math.cos(p);
		double cosJ = Math.cos(J);
		double cosr = Math.cos(r);
		
		W = cosr * cosp * cosJ + sinr * sinp * sinJ;
		I = sinr * cosp * cosJ - cosr * sinp * sinJ;
		J = cosr * sinp * cosJ + sinr * cosp * sinJ;
		K = cosr * cosp * sinJ - sinr * sinp * cosJ;
		
		Normalise();
	}
	
	public void Set(double pitch, double Yaw, double roll)
	{
		double p = pitch * PIOVER180 * 0.5;
		double J = Yaw   * PIOVER180 * 0.5;
		double r = roll  * PIOVER180 * 0.5;
		
		double sinp = Math.sin(p);
		double sinJ = Math.sin(J);
		double sinr = Math.sin(r);
		double cosp = Math.cos(p);
		double cosJ = Math.cos(J);
		double cosr = Math.cos(r);
		
		W = cosr * cosp * cosJ + sinr * sinp * sinJ;
		I = sinr * cosp * cosJ - cosr * sinp * sinJ;
		J = cosr * sinp * cosJ + sinr * cosp * sinJ;
		K = cosr * cosp * sinJ - sinr * sinp * cosJ;
		
		Normalise();
	}
	
	public void Normalise()
	{
		CalculateMagnitude();
		
		I /= m_Magnitude;
		J /= m_Magnitude;
		K /= m_Magnitude;
		W /= m_Magnitude;
	}

	// combined rotation = second * first.
	public void Multiply(final Quaternion lhs, final Quaternion rhs)
	{	
		I = (lhs.W * rhs.I) + (lhs.I * rhs.W) + (lhs.J * rhs.K) - (lhs.K * rhs.J);
		J = (lhs.W * rhs.J) + (lhs.J * rhs.W) + (lhs.K * rhs.I) - (lhs.I * rhs.K);
		K = (lhs.W * rhs.K) + (lhs.K * rhs.W) + (lhs.I * rhs.J) - (lhs.J * rhs.I);		
		W = (lhs.W * rhs.W) - (lhs.I * rhs.I) - (lhs.J * rhs.J) - (lhs.K * rhs.K);	
	}
	
	public float[] ToMatrix()
	{
		float[] matrix = new float[16];
		
		float I2 = (float) (I * I);
		float J2 = (float) (J * J);
		float K2 = (float) (K * K);
		float IJ = (float) (I * J);
		float IK = (float) (I * K);
		float JK = (float) (J * K);
		float WI = (float) (W * I);
		float WJ = (float) (W * J);
		float WK = (float) (W * K);
	 
		matrix[0]  = 1.0f - 2.0f * (J2 + K2);
		matrix[1]  = 2.0f * (IJ - WK);
		matrix[2]  = 2.0f * (IK + WJ);
		matrix[3]  = 0.0f;
				
		matrix[4]  = 2.0f * (IJ + WK);
		matrix[5]  = 1.0f - 2.0f * (I2 + K2);
		matrix[6]  = 2.0f * (JK - WI);
		matrix[7]  = 0.0f;
				
		matrix[8]  = 2.0f * (IK - WJ);
		matrix[9]  = 2.0f * (JK + WI);
		matrix[10] = 1.0f - 2.0f * (I2 + J2);
		matrix[11] = 0.0f;
					
		matrix[12] = 0.0f;
		matrix[13] = 0.0f;
		matrix[14] = 0.0f;
		matrix[15] = 1.0f;
		
		return matrix;
	}
	
	private void CalculateMagnitude()
	{
		m_Magnitude = Math.sqrt(W*W + I*I + J*J + K*K);
	}
	
	public double GetMagnitudeSqr()
	{
		return (W*W + I*I + J*J + K*K);
	}
	
	public double GetMagnitude()
	{
		CalculateMagnitude();
		return m_Magnitude;
	}
	
	public Quaternion GetConjugate()
	{
		Quaternion conjugate = new Quaternion();
		
		conjugate.W = -this.W;
		conjugate.I = -this.I;
		conjugate.J = -this.J;
		conjugate.K = -this.K;

		
		return conjugate;
	}
}






































