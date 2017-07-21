package com.raggamuffin.protorunnerv2.renderer;

// Author: Sinclair Ross
// Date:   04/07/2017

public class ProceduralShapes
{
    public static float[] Generate_ShieldVertices(int numEdges)
    {
        double theta = 0.0f;
        double delta = (Math.PI * 2) / numEdges;

        float[] ringVertices = new float[numEdges * 3];

        for(int i = 0; i < numEdges; ++i)
        {
            float x = (float)-Math.sin(theta);
            float y = (float)Math.cos(theta);

            ringVertices[(i * 3)] = x;
            ringVertices[(i * 3) + 1] = y;
            ringVertices[(i * 3) + 2] = 0;

            theta += delta;
        }

        int numTriangles = numEdges;
        int numVertices = numTriangles * 3;
        float[] vertices = new float[numVertices * 3];

        for (int i = 0; i < numEdges; ++i)
        {
            int nextIndex = (i + 1 < numEdges) ? ((i + 1) * 3) : 0;

            // Top
            vertices[(i * 9)] = ringVertices[(i * 3)];
            vertices[(i * 9) + 1] = ringVertices[(i * 3) + 1];
            vertices[(i * 9) + 2] = ringVertices[(i * 3) + 2];

            vertices[(i * 9) + 3] = 0;
            vertices[(i * 9) + 4] = 0;
            vertices[(i * 9) + 5] = 0;

            vertices[(i * 9) + 6] = ringVertices[nextIndex];
            vertices[(i * 9) + 7] = ringVertices[nextIndex + 1];
            vertices[(i * 9) + 8] = ringVertices[nextIndex + 2];
        }

        return vertices;
    }
}
