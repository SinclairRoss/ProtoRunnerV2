package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES20;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLModel_PlasmaPulse extends GLModel
{
    private FloatBuffer m_VertexBuffer;

    private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_VertexHandle;
    private int m_PositionHandle;
    private int m_ForwardHandle;
    private int m_ScaleHandle;
    private int m_ColourHandle;

    private int m_NumVertices;

    public GLModel_PlasmaPulse()
    {
        m_Program = 0;

        m_ProjMatrixHandle = 0;
        m_ColourHandle = 0;

        float[] vertices =
        {
//                 0.0f,  0.5f,  0.0f, // A
//                 0.0f, -0.5f,  0.0f, // B
//                 0.5f,  0.0f,  0.0f, // C
//                -0.5f,  0.0f,  0.0f, // D
//                 0.0f,  0.0f,  0.5f, // E
//                 0.0f,  0.0f, -1.0f, // F

                0.0f,  0.0f,  0.5f, // E
                0.0f,  0.5f,  0.0f, // A
                -0.5f,  0.0f,  0.0f, // D

                0.0f,  0.0f,  0.5f, // E
                0.5f,  0.0f,  0.0f, // C
                0.0f,  0.5f,  0.0f, // A
        };

        m_NumVertices = vertices.length / 3;

        ByteBuffer vb = ByteBuffer.allocateDirect(vertices.length * 4);
        vb.order(ByteOrder.nativeOrder());
        m_VertexBuffer = vb.asFloatBuffer();
        m_VertexBuffer.put(vertices);
        m_VertexBuffer.position(0);

        InitShaders();
    }

    @Override
    public void InitialiseModel(float[] projMatrix, Vector3 eye)
    {
        GLES20.glUseProgram(m_Program);

        GLES20.glEnableVertexAttribArray(m_VertexHandle);
        GLES20.glVertexAttribPointer(m_VertexHandle, 3, GLES20.GL_FLOAT, false, 12, m_VertexBuffer);

        GLES20.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);
    }

    @Override
    public void Draw(GameObject obj)
    {
        Vector3 pos = obj.GetPosition();
        GLES20.glUniform3f(m_PositionHandle, (float) pos.I, (float) pos.J, (float) pos.K);

        Vector3 fwd = obj.GetForward();
        GLES20.glUniform3f(m_ForwardHandle, (float) fwd.I, (float) fwd.J, (float) fwd.K);

        Vector3 scale = obj.GetScale();
        GLES20.glUniform3f(m_ScaleHandle, (float)scale.I, (float)scale.J, (float)scale.K);

        Colour colour = obj.GetColour();
        GLES20.glUniform4f(m_ColourHandle, (float) colour.Red, (float) colour.Green, (float) colour.Blue, (float) colour.Alpha);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, m_NumVertices);
    }

    @Override
    public void CleanModel()
    {
        GLES20.glDisableVertexAttribArray(m_VertexHandle);
    }

    @Override
    protected void InitShaders()
    {
        // prepare shaders and OpenGL program
        int vertexShaderHandler = loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_LOOKAT_BASIC);
        int fragmentShaderHandler = loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_BLOCKCOLOUR);

        m_Program = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);

        m_ProjMatrixHandle = GLES20.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_PositionHandle   = GLES20.glGetUniformLocation(m_Program, "u_Position");
        m_ForwardHandle    = GLES20.glGetUniformLocation(m_Program, "u_Forward");
        m_ScaleHandle      = GLES20.glGetUniformLocation(m_Program, "u_Scale");
        m_ColourHandle 	   = GLES20.glGetUniformLocation(m_Program, "u_Color");

        m_VertexHandle     = GLES20.glGetAttribLocation(m_Program, "a_Vertices");
    }
}
