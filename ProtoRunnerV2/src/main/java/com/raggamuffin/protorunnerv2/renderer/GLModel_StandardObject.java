package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES20;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLModel_StandardObject  extends GLModel
{
    private final FloatBuffer m_VertexBuffer;
    private final FloatBuffer m_BarycentricCoordBuffer;

    private int m_NumVertices;

    private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_PositionHandle;
    private int m_ForwardHandle;
    private int m_RollHandle;
    private int m_ScaleHandle;
    private int m_ColourHandle;
    private int m_VertexHandle;
    private int m_BarycentricHandle;

    public GLModel_StandardObject(float[] vertices)
    {
        ByteBuffer vb = ByteBuffer.allocateDirect(vertices.length * 4);
        vb.order(ByteOrder.nativeOrder());
        m_VertexBuffer = vb.asFloatBuffer();
        m_VertexBuffer.put(vertices);
        m_VertexBuffer.position(0);

        float[] coords = new float[vertices.length];

        int numTriangles = vertices.length / 9;
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

        m_NumVertices = vertices.length / 3;

        m_Program 			= 0;
        m_ProjMatrixHandle  = 0;
        m_PositionHandle    = 0;

        m_ColourHandle		= 0;
        m_VertexHandle	    = 0;
        m_BarycentricHandle = 0;

        InitShaders();
    }

    @Override
    public void InitialiseModel(float[] projMatrix, Vector3 eye)
    {
        GLES20.glUseProgram(m_Program);

        GLES20.glEnableVertexAttribArray(m_VertexHandle);
        GLES20.glVertexAttribPointer(m_VertexHandle, 3, GLES20.GL_FLOAT, false, 12, m_VertexBuffer);

        GLES20.glEnableVertexAttribArray(m_BarycentricHandle);
        GLES20.glVertexAttribPointer(m_BarycentricHandle, 3, GLES20.GL_FLOAT, false, 12, m_BarycentricCoordBuffer);

        GLES20.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);
    }

    @Override
    public void Draw(GameObject obj)
    {
        Vector3 pos = obj.GetPosition();
        GLES20.glUniform4f(m_PositionHandle, (float) pos.I, (float) pos.J, (float) pos.K, 1.0f);

        Vector3 fwd = obj.GetForward();
        GLES20.glUniform3f(m_ForwardHandle, (float) fwd.I, (float) fwd.J, (float) fwd.K);

        GLES20.glUniform1f(m_RollHandle, (float) obj.GetRoll());

        Vector3 scale = obj.GetScale();
        GLES20.glUniform3f(m_ScaleHandle, (float)scale.I, (float)scale.J, (float)scale.K);

        Colour colour = obj.GetColour();
        GLES20.glUniform4f(m_ColourHandle, (float) colour.Red, (float) colour.Green, (float) colour.Blue, (float) colour.Alpha);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, m_NumVertices);
    }

    @Override
    public void CleanModel()
    {
        GLES20.glDisableVertexAttribArray(m_PositionHandle);
        GLES20.glDisableVertexAttribArray(m_BarycentricHandle);
    }

    @Override
    protected void InitShaders()
    {
        // prepare shaders and OpenGL program
        int vertexShaderHandler = loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_BARYCENTRIC);
        int fragmentShaderHandler = loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_BARYCENTRIC);

        m_Program = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle = GLES20.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_PositionHandle   = GLES20.glGetUniformLocation(m_Program, "u_Position");
        m_ForwardHandle    = GLES20.glGetUniformLocation(m_Program, "u_Forward");
        m_RollHandle       = GLES20.glGetUniformLocation(m_Program, "u_Roll");
        m_ScaleHandle      = GLES20.glGetUniformLocation(m_Program, "u_Scale");
        m_ColourHandle 	   = GLES20.glGetUniformLocation(m_Program, "u_Color");

        m_VertexHandle      = GLES20.glGetAttribLocation(m_Program, "a_Vertices");
        m_BarycentricHandle = GLES20.glGetAttribLocation(m_Program, "a_Barycentric");
    }
}
