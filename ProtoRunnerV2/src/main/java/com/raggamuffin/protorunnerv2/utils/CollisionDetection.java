package com.raggamuffin.protorunnerv2.utils;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.ui.UITouchArea;

public final class CollisionDetection 
{
	private static Vector3 DisplacementA = new Vector3();
	private static Vector3 DisplacementB = new Vector3();
	private static Vector3 RelativeMovement = new Vector3();

	// Check for collisions between two bounding spheres.
	public static CollisionReport CheckCollisions(GameObject A, GameObject B)
	{
		DisplacementA.SetVectorAsDifference(A.GetPreviousPosition(), A.GetPosition());
		DisplacementB.SetVectorAsDifference(B.GetPreviousPosition(), B.GetPosition());
		RelativeMovement.SetVectorAsDifference(DisplacementA, DisplacementB);	// Movement relative to object A.
		RelativeMovement.Add(B.GetPreviousPosition());

		return RayCastSphere(B.GetPreviousPosition(), RelativeMovement, A.GetPosition(), A.GetBoundingRadius() + B.GetBoundingRadius());
	}

	public static CollisionReport RayCastSphere(Vector3 rayStart, Vector3 rayEnd, Vector3 spherePos, double sphereRadius)
	{
        // Calculate discriminant
		double rayX = rayEnd.X - rayStart.X;
		double rayY = rayEnd.Y - rayStart.Y;
		double rayZ = rayEnd.Z - rayStart.Z;
        double paraX = -spherePos.X + rayStart.X;
        double paraY = -spherePos.Y + rayStart.Y;
        double paraZ = -spherePos.Z + rayStart.Z;

		double a = rayX*rayX + rayY*rayY + rayZ*rayZ;
		double b = (rayX * paraX)*2 + (rayY * paraY)*2 + (rayZ * paraZ)*2;
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
                //Vector3 entryPoint = new Vector3();
                //entryPoint.SetVectorAsDifference(rayStart, rayEnd);
                //entryPoint.Scale(entersAt);
                //entryPoint.Add(rayStart);
//
                //Vector3 exitPoint = new Vector3();
                //exitPoint.SetVectorAsDifference(rayStart, rayEnd);
                //exitPoint.Scale(exitsAt);
                //exitPoint.Add(rayStart);

                report = new CollisionReport(entersAt, exitsAt);
            }
        }

		return report;
	}
	
	public static boolean UIElementInteraction(final Vector2 touch, final UITouchArea touchArea)
	{
		boolean hitDetected = false;

        if (touch.X >= touchArea.Left() && touch.X <= touchArea.Right())
        {
            if(touch.Y >= touchArea.Bottom() && touch.Y <= touchArea.Top())
            {
                hitDetected = true;
            }
        }

		return hitDetected;
	}
	
	public static boolean RadarDetection(Vector3 radarFragmentPosition, Vector3 vehiclePosition, double fragmentBounds)
    {
        boolean collision = (vehiclePosition.X >= (radarFragmentPosition.X - fragmentBounds)
                || vehiclePosition.X > (radarFragmentPosition.X + fragmentBounds)
                || vehiclePosition.Z < (radarFragmentPosition.Z - fragmentBounds)) && vehiclePosition.Z <= (radarFragmentPosition.Z + fragmentBounds);

        return collision;
    }
}






























