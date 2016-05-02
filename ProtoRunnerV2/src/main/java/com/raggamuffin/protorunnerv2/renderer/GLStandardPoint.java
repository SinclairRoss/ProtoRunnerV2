package com.raggamuffin.protorunnerv2.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class GLStandardPoint extends GLModel
{
    public final FloatBuffer vertexBuffer;

    private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_SizeHandle;
    private int m_ColourHandle;
    private int m_PositionHandle;
    private int m_WorldPosHandle;
    private int m_EyePosHandle;

    static float VertexCoords[] =
    {
        0.0f, 0.0f, 0.0f
    };

    public GLStandardPoint()
    {
        // create a byte buffer for the vertex coords.
        ByteBuffer bb = ByteBuffer.allocateDirect(VertexCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(VertexCoords);
        vertexBuffer.position(0);

        m_Program 			= 0;
        m_ProjMatrixHandle  = 0;
        m_SizeHandle		= 0;
        m_ColourHandle		= 0;
        m_PositionHandle	= 0;
        m_WorldPosHandle 	= 0;
        m_EyePosHandle 		= 0;

        InitShaders();
    }

    protected void InitShaders()
    {
        // prepare shaders and OpenGL program
        int vertexShaderHandler 	= loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_POINT);
        int fragmentShaderHandler 	= loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_BLOCKCOLOUR);

        m_Program = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle  = GLES20.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_ColourHandle 		= GLES20.glGetUniformLocation(m_Program, "u_Color");
        m_SizeHandle 		= GLES20.glGetUniformLocation(m_Program, "u_Size");
        m_EyePosHandle 		= GLES20.glGetUniformLocation(m_Program, "u_EyePos");
        m_WorldPosHandle 	= GLES20.glGetUniformLocation(m_Program, "u_WorldPos");

        m_PositionHandle 	= GLES20.glGetAttribLocation(m_Program, "a_Position");
    }

    @Override
    public void InitialiseModel(float[] projMatrix, Vector3 eye)
    {
        GLES20.glUseProgram(m_Program);

        GLES20.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);
        GLES20.glUniform4f(m_EyePosHandle, (float) eye.I, (float) eye.J, (float) eye.K, 1.0f);

        GLES20.glEnableVertexAttribArray(m_PositionHandle);
        GLES20.glVertexAttribPointer(m_PositionHandle, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer);
    }

    @Override
    public void Draw(GameObject obj)
    {
        Vector3 pos = obj.GetPosition();

        GLES20.glUniform4f(m_WorldPosHandle, (float) pos.I, (float) pos.J, (float) pos.K, 1.0f);

        GLES20.glUniform1f(m_SizeHandle, (float)obj.GetScale().I);
        Colour colour = obj.GetColour();
        GLES20.glUniform4f(m_ColourHandle, (float)colour.Red, (float)colour.Green, (float)colour.Blue, (float)colour.Alpha);

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
    }

    @Override
    public void CleanModel()
    {
        GLES20.glDisableVertexAttribArray(m_PositionHandle);
    }
}