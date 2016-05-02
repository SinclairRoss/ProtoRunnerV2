package com.raggamuffin.protorunnerv2.utils;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.ui.UIElement;

public final class CollisionDetection 
{
	private static Vector3 DisplacementA = new Vector3();
	private static Vector3 DisplacementB = new Vector3();
	private static Vector3 RelativeMovement = new Vector3();

	private static Vector3 AToB = new Vector3();
	
	// Check for collisions between two bounding spheres.
	public static CollisionReport CheckCollisions(GameObject A, GameObject B)
	{
		DisplacementA.SetVectorDifference(A.GetPreviousPosition(), A.GetPosition());
		DisplacementB.SetVectorDifference(B.GetPreviousPosition(), B.GetPosition());
		RelativeMovement.SetVectorDifference(DisplacementA, DisplacementB);	// Movement relative to object A.

		AToB.SetVectorDifference(A.GetPreviousPosition(), B.GetPreviousPosition());

		double ResultantLength = RelativeMovement.GetLength();

		return RayCastSphere(B.GetPreviousPosition(), RelativeMovement, A.GetPosition(), A.GetBoundingRadius() + B.GetBoundingRadius());
	//	double AToBLength = AToB.GetLength();

		/*
		double BoundingA = A.GetBoundingRadius();
		double BoundingB = B.GetBoundingRadius();
		double SumBoundingRadii = BoundingA + BoundingB;


		// PHASE 1: Did the objects travel far enough to collide?
		if(ResultantLength < (AToBLength- SumBoundingRadii))
			return false;

		RelativeMovement.Normalise();
		double DotProduct = Vector3.DotProduct(RelativeMovement, AToB);

		// PHASE 2: Are the two objects moving towards each other?
		if(DotProduct <= 0)
			return false;

		// PHASE 3: Are A and B close enough to touch at their closest point.
		double ClosestPointSqr = (AToBLength * AToBLength) - (DotProduct * DotProduct);

		if(ClosestPointSqr >= (SumBoundingRadii * SumBoundingRadii))
			return false;

		// Phase 4: At the closest point, are A and B touching.
		double MinDistance = (SumBoundingRadii * SumBoundingRadii) - ClosestPointSqr;

		if(MinDistance < 0)
			return false;

		double Distance = DotProduct - Math.sqrt(MinDistance);

		if(ResultantLength < Distance)
			return false;

		return true;
		*/
	}

    public static boolean SimpleCollisionDetection(GameObject a, GameObject b)
    {
        AToB.SetVectorDifference(a.GetPosition(), b.GetPosition());
        double sumBoundingRadius = (a.GetBoundingRadius() + b.GetBoundingRadius());

        return AToB.GetLengthSqr() < (sumBoundingRadius * sumBoundingRadius);
    }

	public static CollisionReport RayCastSphere(Vector3 rayStart, Vector3 rayEnd, Vector3 spherePos, double sphereRadius)
	{
		//TODO: Account for collisions between frames
        // Calculate discriminant
		double lengthX = rayEnd.I - rayStart.I;
		double lengthY = rayEnd.J - rayStart.J;
		double lengthZ = rayEnd.K - rayStart.K;
        double paraX = -spherePos.I + rayStart.I;
        double paraY = -spherePos.J + rayStart.J;
        double paraZ = -spherePos.K + rayStart.K;

		double a = lengthX*lengthX + lengthY*lengthY + lengthZ*lengthZ;
		double b = (lengthX * paraX)*2 + (lengthY * paraY)*2 + (lengthZ * paraZ)*2;
		double c = paraX*paraX + paraY*paraY + paraZ*paraZ - sphereRadius*sphereRadius;

		double discriminantSqr = (b*b) - (4*a*c);

		CollisionReport report = null;

        if(discriminantSqr > 0)
        {
			double discriminant = Math.sqrt(discriminantSqr);

			double entersAt = (-b - discriminant) /  (2 * a);
			double exitsAt  = (-b + discriminant) / (2 * a);

            if((entersAt >= 0.0 && entersAt <= 1.0) ||  // If the ray enters the sphere.
               (exitsAt >= 0.0 && exitsAt <= 1.0)   ||  // If the ray exits the sphere.
               (entersAt < 0.0 && exitsAt > 1.0))       // if the ray lies completely within sphere.
            {
                Vector3 entryPoint = new Vector3();
                entryPoint.SetVectorDifference(rayStart, rayEnd);
                entryPoint.Scale(entersAt);
                entryPoint.Add(rayStart);

                Vector3 exitPoint = new Vector3();
                exitPoint.SetVectorDifference(rayStart, rayEnd);
                exitPoint.Scale(exitsAt);
                exitPoint.Add(rayStart);

                report = new CollisionReport(true, entersAt, exitsAt, entryPoint, exitPoint);
            }
        }

		return report;
	}

	public static boolean RayTrace(Vector3 Origin, Vector3 NormRay, double Length, GameObject Object)
	{
		AToB.SetVectorDifference(Origin, Object.GetPosition());

		// PHASE 1: Can the ray possibly reach the Object
		if(AToB.GetLengthSqr() > Length * Length)
			return false;

		// PHASE 2: Is the ray in the direction of the object.
		double dotProduct = Vector3.DotProduct(NormRay, AToB);

		if(dotProduct <= 0.0)
			return false;

		// PHASE 3: Is the ray coliding with the Game objects Collision sphere.
		double aToBLengthSqr = AToB.GetLengthSqr();
		double boundingRadius = Object.GetBoundingRadius();
		double distanceSqr = aToBLengthSqr - (dotProduct * dotProduct);

		if(distanceSqr > (boundingRadius * boundingRadius))
			return false;

		return true;
	}
	
	public static boolean UIElementInteraction(final Vector2 Touch, final Vector2 ScreenSize, final UIElement Element)
	{
        if(Element.IsHidden())
            return false;

		// PHASE 1: Convert Touch coords into screen space coords.
		double ratio = ScreenSize.I / ScreenSize.J;

		// Normalised screen space.
		double i = MathsHelper.Normalise(Touch.I, 0, ScreenSize.I);
		double j = MathsHelper.Normalise(Touch.J, 0, ScreenSize.J);
		
		i = MathsHelper.Lerp(i, -ratio, ratio);
		j = MathsHelper.Lerp(j, 1, -1);

		// Compare normalised screen poition agains the UIElement.
		Vector2 ElementPosition = Element.GetPosition();
		Vector2 ElementSize	    = Element.GetSize();
		
		if(i < ElementPosition.I || i > (ElementPosition.I + ElementSize.I))
			return false;
		
		if(j < ElementPosition.J || j > (ElementPosition.J + ElementSize.J))
			return false;

		return true;	
	}
	
	public static boolean RadarDetection(Vector3 radarFragmentPosition, Vector3 vehiclePosition, double fragmentBounds)
	{
		if(vehiclePosition.I < (radarFragmentPosition.I - fragmentBounds))
			return false;
		
		if(vehiclePosition.I > (radarFragmentPosition.I + fragmentBounds))
			return false;
		
		if(vehiclePosition.K < (radarFragmentPosition.K - fragmentBounds))
			return false;
		
		if(vehiclePosition.K > (radarFragmentPosition.K + fragmentBounds))
			return false;
	
		return true;
	}
}






























