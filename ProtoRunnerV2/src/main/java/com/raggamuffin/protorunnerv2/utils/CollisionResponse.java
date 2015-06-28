// Author:	Sinclair Ross.
// Date:	29/10/2014.

package com.raggamuffin.protorunnerv2.utils;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
// Doesn't work... yet.
public final class CollisionResponse 
{
	private static Vector3 NormalisedAToB = new Vector3();

	private static Vector3 VelocityA = new Vector3();
	private static Vector3 VelocityB = new Vector3();
	private static Vector3 NormalisedRelativeVelocity = new Vector3();

	private static Vector3 ApproachVector = new Vector3();

	public static void Bounce(GameObject A, GameObject B)
	{
		NormalisedAToB.SetVectorDifference(A.GetPosition(), B.GetPosition());
		NormalisedAToB.Normalise();
		
		VelocityA.SetVector(A.GetVelocity());
		VelocityB.SetVector(B.GetVelocity());
		
		NormalisedRelativeVelocity.SetVectorDifference(VelocityA,VelocityB);
		NormalisedRelativeVelocity.Normalise();
		
		ApproachVector.SetVector(NormalisedRelativeVelocity);
		ApproachVector.Multiply(NormalisedAToB);

		A.ApplyForce(ApproachVector, Physics.CalculateKineticEnergy(B));
		ApproachVector.Scale(-1);
		
		B.ApplyForce(ApproachVector, Physics.CalculateKineticEnergy(A));
	}
}
