// Author:	Sinclair Ross.
// Date:	29/10/2014.

package com.raggamuffin.protorunnerv2.utils;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;

public class Physics 
{
	public static double CalculateKineticEnergy(GameObject Object)
	{
		double Velocity = Object.GetVelocity().GetLength();
		
		return 0.5 * Object.GetMass() * (Velocity * Velocity);
	}
	
	public static Vector3 CalculateMomentum(GameObject Object)
	{
		Vector3 Momentum = new Vector3();
		Momentum.SetVector(Object.GetVelocity());
		Momentum.Scale(Object.GetMass());
		return Momentum;
	}


}
