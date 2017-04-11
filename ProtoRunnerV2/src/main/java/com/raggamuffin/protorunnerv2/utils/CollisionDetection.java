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
		DisplacementA.SetAsDifference(A.GetPreviousPosition(), A.GetPosition());
		DisplacementB.SetAsDifference(B.GetPreviousPosition(), B.GetPosition());
		RelativeMovement.SetAsDifference(DisplacementA, DisplacementB);	// Movement relative to object A.
		RelativeMovement.Add(B.GetPreviousPosition());

		return RayCastSphere(B.GetPreviousPosition(), RelativeMovement, A.GetPosition(), A.GetBoundingRadius() + B.GetBoundingRadius());
	}

	public static CollisionReport RayCastSphere(Vector3 rayStart, Vector3 rayEnd, Vector3 spherePos, double sphereRadius)
	{
        // Calculate discriminant
		double lengthX = rayEnd.X - rayStart.X;
		double lengthY = rayEnd.Y - rayStart.Y;
		double lengthZ = rayEnd.Z - rayStart.Z;
        double paraX = -spherePos.X + rayStart.X;
        double paraY = -spherePos.Y + rayStart.Y;
        double paraZ = -spherePos.Z + rayStart.Z;

		double a = lengthX*lengthX + lengthY*lengthY + lengthZ*lengthZ;
		double b = (lengthX * paraX)*2 + (lengthY * paraY)*2 + (lengthZ * paraZ)*2;
		double c = (paraX*paraX) + (paraY*paraY) + (paraZ*paraZ) - (sphereRadius*sphereRadius);

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
                entryPoint.SetAsDifference(rayStart, rayEnd);
                entryPoint.Scale(entersAt);
                entryPoint.Add(rayStart);

                Vector3 exitPoint = new Vector3();
                exitPoint.SetAsDifference(rayStart, rayEnd);
                exitPoint.Scale(exitsAt);
                exitPoint.Add(rayStart);

                report = new CollisionReport(true, entersAt, exitsAt, entryPoint, exitPoint);
            }
        }

		return report;
	}
	
	public static boolean UIElementInteraction(final Vector2 Touch, final Vector2 ScreenSize, final UIElement Element)
	{
		boolean hitDetected = false;

        if(!Element.IsHidden())
		{
			// PHASE 1: Convert Touch coords into screen space coords.
			double ratio = ScreenSize.I / ScreenSize.J;

			// Normalised screen space.
			double i = MathsHelper.Normalise(Touch.I, 0, ScreenSize.I);
			double j = MathsHelper.Normalise(Touch.J, 0, ScreenSize.J);

			i = MathsHelper.Lerp(i, -ratio, ratio);
			j = MathsHelper.Lerp(j, 1, -1);

			// Compare normalised screen position against the UIElement.
			Vector2 ElementPosition = Element.GetPosition();
			Vector2 ElementSize = Element.GetSize();

			if (i >= ElementPosition.I && i <= (ElementPosition.I + ElementSize.I))
			{
				if(j >= ElementPosition.J && j <= (ElementPosition.J + ElementSize.J))
				{
					hitDetected = true;
				}
			}
		}

		return hitDetected;
	}
	
	public static boolean RadarDetection(Vector3 radarFragmentPosition, Vector3 vehiclePosition, double fragmentBounds)
	{
		if(vehiclePosition.X < (radarFragmentPosition.X - fragmentBounds))
			return false;
		
		if(vehiclePosition.X > (radarFragmentPosition.X + fragmentBounds))
			return false;
		
		if(vehiclePosition.Z < (radarFragmentPosition.Z - fragmentBounds))
			return false;

		return vehiclePosition.Z <= (radarFragmentPosition.Z + fragmentBounds);

	}
}






























