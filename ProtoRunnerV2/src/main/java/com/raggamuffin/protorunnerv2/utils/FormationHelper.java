package com.raggamuffin.protorunnerv2.utils;

import java.util.ArrayList;

// Provides static methods that help create spawning formations for vehicles.
public class FormationHelper
{
    public static ArrayList<Vector3> CreateWedgeFormation(final Vector3 pos, final Vector3 forward, double horizontalSpacing, double verticalSpacing, int count)
    {
        ArrayList<Vector3> formation = new ArrayList<>(count);

        Vector3 right = new Vector3(forward);
        right.SetVectorAsCrossProduct(Vector3.UP, forward);

        double verticalOffsetI = 0.0;
        double verticalOffsetK = 0.0;

        double horizontalOffsetI = 0.0;
        double horizontalOffsetK = 0.0;

        for(int i = 0; i < count; ++i)
        {
            double actualHorizontalOffsetI = horizontalOffsetI;
            double actualHorizontalOffsetK = horizontalOffsetK;

            if((i % 2) == 1)
            {
                verticalOffsetI -= verticalSpacing * forward.X;
                verticalOffsetK -= verticalSpacing * forward.Z;

                horizontalOffsetI -= horizontalSpacing * right.X;
                horizontalOffsetK -= horizontalSpacing * right.Z;

                actualHorizontalOffsetI = -horizontalOffsetI;
                actualHorizontalOffsetK = -horizontalOffsetK;
            }

            Vector3 position = new Vector3(verticalOffsetI, 0.0, verticalOffsetK);
            position.Add(actualHorizontalOffsetI, 0.0, actualHorizontalOffsetK);
            position.Add(pos);

            formation.add(position);
        }

        return formation;
    }

    public static ArrayList<Vector3> CreateLineFormation(final Vector3 pos, final Vector3 forward, double spacing, int count)
    {
        ArrayList<Vector3> formation = new ArrayList<>(count);

        Vector3 right = new Vector3(forward);
        right.SetVectorAsCrossProduct(Vector3.UP, forward);

        double formationWidth = spacing * (count - 1);
        double xOffset = right.X * formationWidth * 0.5;
        double zOffset = right.Z * formationWidth * 0.5;

        for(int i = 0; i < count; ++i)
        {
            Vector3 position = new Vector3(right);

            position.Scale(i * spacing);
            position.Subtract(xOffset, 0, zOffset);
            position.Add(pos);

            formation.add(position);
        }

        return formation;
    }
}
