package com.raggamuffin.protorunnerv2.utils;

public class Orientation 
{
	public float W;
	public float I;
	public float J;
	public float K;
	
	public Orientation(final Vector3 vec, float theta)
	{
		W = theta;
		I = (float) vec.I;// * sinTheta;
		J = (float) vec.J;// * sinTheta;
		K = (float) vec.K;// * sinTheta;
	}
}
