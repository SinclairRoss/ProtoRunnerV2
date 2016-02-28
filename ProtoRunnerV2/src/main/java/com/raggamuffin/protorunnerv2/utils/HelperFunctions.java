package com.raggamuffin.protorunnerv2.utils;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;

import java.util.ArrayList;

public class HelperFunctions
{
    private static Vector3 s_ScratchVector = new Vector3();

    public static GameObject FindClosestObjectToPoint(ArrayList<GameObject> objects, Vector3 point)
    {
        GameObject closestObject = null;
        double closestDistanceSqr = Double.MAX_VALUE;

        for(GameObject object : objects)
        {
            s_ScratchVector.SetVectorDifference(object.GetPosition(), point);

            double distanceSqr = s_ScratchVector.GetLengthSqr();

            if(distanceSqr < closestDistanceSqr)
            {
                closestObject = object;
                closestDistanceSqr = distanceSqr;
            }
        }

        return closestObject;
    }
}
