package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES20;
import android.util.Log;

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLModel_UIChevron
{
    private final FloatBuffer m_VertexBuffer;

    private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_PositionHandle;
    private int m_ScaleHandle;
    private int m_RotationHandle;
    private int m_ColourHandle;
    private int m_VertexHandle;

    static float VertexCoords[] =
    {
            1.0f, -0.5f, 0.0f,
            0.0f, 0.0f, 0.0f,   // Tip
            -1.0f, -0.5f, 0.0f
    };

    public GLModel_UIChevron()
    {
        ByteBuffer vb = ByteBuffer.allocateDirect(VertexCoords.length * 4);
        vb.order(ByteOrder.nativeOrder());
        m_VertexBuffer = vb.asFloatBuffer();
        m_VertexBuffer.put(VertexCoords);
        m_VertexBuffer.position(0);

        m_Program = 0;
        m_ProjMatrixHandle = 0;
        m_PositionHandle = 0;
        m_RotationHandle = 0;
        m_VertexHandle = 0;

        InitShaders();
    }

    public void InitialiseModel(float[] projMatrix)
    {
        GLES20.glUseProgram(m_Program);

        GLES20.glEnableVertexAttribArray(m_VertexHandle);
        GLES20.glVertexAttribPointer(m_VertexHandle, 3, GLES20.GL_FLOAT, false, 12, m_VertexBuffer);

        GLES20.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);
    }

    public void Draw(Vector2 pos, Vector2 scale, double rotation, Colour colour, double lineWidth)
    {
        GLES20.glUniform2f(m_PositionHandle, (float)pos.X, (float)pos.Y);
        GLES20.glUniform2f(m_ScaleHandle, (float)scale.X, (float)scale.Y);
        GLES20.glUniform4f(m_ColourHandle, (float) colour.Red, (float) colour.Green, (float)colour.Blue, (float)colour.Alpha);
        GLES20.glUniform1f(m_RotationHandle, (float)rotation);

        GLES20.glLineWidth((float)lineWidth);
        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, 3);
    }

    public void CleanModel()
    {
        GLES20.glDisableVertexAttribArray(m_VertexHandle);
    }

    protected void InitShaders()
    {
        // prepare shaders and OpenGL program
        int vertexShaderHandler = loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_UISTANDARD);
        int fragmentShaderHandler = loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_BLOCKCOLOUR);

        m_Program = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle      = GLES20.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_PositionHandle        = GLES20.glGetUniformLocation(m_Program, "u_Position");
        m_ScaleHandle           = GLES20.glGetUniformLocation(m_Program, "u_Scale");
        m_RotationHandle        = GLES20.glGetUniformLocation(m_Program, "u_Rotation");

        m_ColourHandle 			= GLES20.glGetUniformLocation(m_Program, "u_Color");

        m_VertexHandle = GLES20.glGetAttribLocation(m_Program, "a_Vertices");
    }

    protected int loadShader(int type, String shaderCode)
    {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);

        if (compiled[0] == 0)
        {
            Log.e("Shader", "Shader is broken");
        }

        return shader;
    }
}
