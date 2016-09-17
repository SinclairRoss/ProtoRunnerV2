package com.raggamuffin.protorunnerv2.renderer;

// Author: Sinclair Ross
// Date:   13/08/2016

import android.opengl.GLES20;

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class RenderObject_WireFrame extends RenderObject
{
    private FloatBuffer m_VertexBuffer;
    private FloatBuffer m_BarycentricCoordBuffer;

    private final int m_NumVertices;

    private final Vector3 m_Position;
    private final Vector3 m_Forward;
    private final Vector3 m_Up;
    private final Vector3 m_Right;
    private final Vector3 m_Scale;
    private final double m_Roll;
    private final Colour m_Colour;

    private final int m_ProjMatrixHandle;
    private final int m_VertexHandle;
    private final int m_BarycentricHandle;
    private final int m_PositionHandle;
    private final int m_ForwardHandle;
    private final int m_UpHandle;
    private final int m_RightHandle;
    private final int m_ScaleHandle;
    private final int m_RollHandle;
    private final int m_ColourHandle;

    public RenderObject_WireFrame(float[] vertices, ModelType modelType, Vector3 position, Vector3 forward, Vector3 up, Vector3 right, Vector3 scale, Colour colour)
    {
        super(Shaders.vertexShader_WIREFRAME, Shaders.fragmentShader_WIREFRAME, modelType);

        if(vertices !=null)
        {
            CreateVertexBuffer(vertices);
            CreateBarycentricCoordBuffer(vertices);
        }

        m_NumVertices = vertices.length / 3;

        m_Position = position;
        m_Forward = forward;
        m_Up = up;
        m_Right = right;
        m_Scale = scale;
        m_Roll = 0;
        m_Colour = colour;

        m_ProjMatrixHandle = GLES20.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_VertexHandle = GLES20.glGetAttribLocation(m_Program, "a_Vertices");
        m_BarycentricHandle = GLES20.glGetAttribLocation(m_Program, "a_Barycentric");
        m_PositionHandle = GLES20.glGetUniformLocation(m_Program, "u_Position");
        m_ForwardHandle = GLES20.glGetUniformLocation(m_Program, "u_Forward");
        m_UpHandle = GLES20.glGetUniformLocation(m_Program, "u_Up");
        m_RightHandle = GLES20.glGetUniformLocation(m_Program, "u_Right");
        m_ScaleHandle = GLES20.glGetUniformLocation(m_Program, "u_Scale");
        m_RollHandle = GLES20.glGetUniformLocation(m_Program, "u_Roll");
        m_ColourHandle = GLES20.glGetUniformLocation(m_Program, "u_Color");
    }

    private void CreateVertexBuffer(float[] vertices)
    {
        ByteBuffer vb = ByteBuffer.allocateDirect(vertices.length * 4);
        vb.order(ByteOrder.nativeOrder());
        m_VertexBuffer = vb.asFloatBuffer();
        m_VertexBuffer.put(vertices);
        m_VertexBuffer.position(0);
    }

    private void CreateBarycentricCoordBuffer(float[] vertices)
    {
        float[] coords = new float[vertices.length];
        int numTriangles = m_NumVertices / 3;

        for(int i = 0; i < numTriangles; i++)
        {
            coords[i * 9]     = 1.0f;
            coords[i * 9 + 1] = 0.0f;
            coords[i * 9 + 2] = 0.0f;

            coords[i * 9 + 3] = 0.0f;
            coords[i * 9 + 4] = 1.0f;
            coords[i * 9 + 5] = 0.0f;

            coords[i * 9 + 6] = 0.0f;
            coords[i * 9 + 7] = 0.0f;
            coords[i * 9 + 8] = 1.0f;
        }

        ByteBuffer bcb = ByteBuffer.allocateDirect(coords.length * 4);
        bcb.order(ByteOrder.nativeOrder());
        m_BarycentricCoordBuffer = bcb.asFloatBuffer();
        m_BarycentricCoordBuffer.put(coords);
        m_BarycentricCoordBuffer.position(0);
    }

    @Override
    public void InitialiseModel(float[] projMatrix)
    {
        GLES20.glUseProgram(m_Program);

        GLES20.glEnableVertexAttribArray(m_VertexHandle);
        GLES20.glVertexAttribPointer(m_VertexHandle, 3, GLES20.GL_FLOAT, false, 12, m_VertexBuffer);

        GLES20.glEnableVertexAttribArray(m_BarycentricHandle);
        GLES20.glVertexAttribPointer(m_BarycentricHandle, 3, GLES20.GL_FLOAT, false, 12, m_BarycentricCoordBuffer);

        GLES20.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);
    }

    @Override
    public void Draw()
    {
        GLES20.glUniform4f(m_PositionHandle, (float) m_Position.I, (float) m_Position.J, (float) m_Position.K, 1.0f);
        GLES20.glUniform3f(m_ForwardHandle, (float) m_Forward.I, (float) m_Forward.J, (float) m_Forward.K);
        GLES20.glUniform3f(m_UpHandle, (float) m_Up.I, (float) m_Up.J, (float) m_Up.K);
        GLES20.glUniform3f(m_RightHandle, (float) m_Right.I, (float) m_Right.J, (float) m_Right.K);
        GLES20.glUniform3f(m_ScaleHandle, (float) m_Scale.I, (float) m_Scale.J, (float) m_Scale.K);
        GLES20.glUniform1f(m_RollHandle, (float) m_Roll);
        GLES20.glUniform4f(m_ColourHandle, (float) m_Colour.Red, (float) m_Colour.Green, (float) m_Colour.Blue, (float) m_Colour.Alpha);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, m_NumVertices);
    }

    @Override
    public void CleanModel()
    {
        GLES20.glDisableVertexAttribArray(m_PositionHandle);
        GLES20.glDisableVertexAttribArray(m_BarycentricHandle);
    }
}
