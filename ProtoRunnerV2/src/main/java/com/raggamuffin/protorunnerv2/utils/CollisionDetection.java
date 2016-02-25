package com.raggamuffin.protorunnerv2.utils;

import android.util.Log;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.ui.UIElement;

public final class CollisionDetection 
{
	private static Vector3 MovementA = new Vector3();
	private static Vector3 MovementB = new Vector3();
	private static Vector3 MovementResultant = new Vector3();

	private static Vector3 AToB = new Vector3();
	
	// Check for collisions between two bounding spheres.
	// Note: Work in progress. Calculating the points of intersection not complete.
	public static boolean CheckCollisions(GameObject A, GameObject B)
	{
		MovementA.SetVectorDifference(A.GetPreviousPosition(), A.GetPosition());
		MovementB.SetVectorDifference(B.GetPreviousPosition(), B.GetPosition());	
		MovementResultant.SetVectorDifference(MovementA, MovementB);

		AToB.SetVectorDifference(A.GetPreviousPosition(), B.GetPreviousPosition());

		double ResultantLength = MovementResultant.GetLength();	
		double AToBLength = AToB.GetLength();

		double BoundingA = A.GetBoundingRadius();
		double BoundingB = B.GetBoundingRadius();
		double SumBoundingRadii = BoundingA + BoundingB;

		// PHASE 1: Did the objects travel far enough to collide?
		if(ResultantLength < (AToBLength- SumBoundingRadii))
			return false;

		MovementResultant.Normalise();
		double DotProduct = Vector3.DotProduct(MovementResultant, AToB);
		
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
	}

    public static boolean SimpleCollisionDetection(GameObject a, GameObject b)
    {
        AToB.SetVectorDifference(a.GetPosition(), b.GetPosition());
        double sumBoundingRadius = (a.GetBoundingRadius() + b.GetBoundingRadius());

        return AToB.GetLengthSqr() < (sumBoundingRadius * sumBoundingRadius);
    }

	public static boolean RayTrace(Vector3 Origin, Vector3 NormRay, double Length, GameObject Object)
	{
		AToB.SetVectorDifference(Origin, Object.GetPosition());

		// PHASE 1: Can the ray possibly reach the Object
		if(AToB.GetLengthSqr() > Length * Length)
			return false;
		
		// PHASE 2: Is the ray in the direction of the object.
		double DotProduct = Vector3.DotProduct(NormRay, AToB);
		
		if(DotProduct <= 0.0)
			return false;
				
		// PHASE 3: Is the ray coliding with the Game objects Collision sphere.
		double AToBLengthSqr = AToB.GetLengthSqr();
		double BoundingRadius = Object.GetBoundingRadius();
		double ClosestPointSqr = (AToBLengthSqr) - (DotProduct * DotProduct);
		
		if(ClosestPointSqr > (BoundingRadius * BoundingRadius))
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






























