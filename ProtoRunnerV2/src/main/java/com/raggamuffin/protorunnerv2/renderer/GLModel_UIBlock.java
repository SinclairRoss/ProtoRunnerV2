package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES20;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector2;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLModel_UIBlock extends GLModel
{
    public final FloatBuffer vertexBuffer;

    private int m_Program;

    private int m_ProjMatrixHandle;

    private int m_PositionHandle;
    private int m_ScaleHandle;
    private int m_ColourHandle;
    private int m_RotationHandle;

    private int m_VertexHandle;

    private float[] m_Offset;

    static final int COORDS_PER_VERTEX = 3;
    static float VertexCoords[] =
    {
        -0.5f,	  0.5f,	 0.0f, // A
        -0.5f,	 -0.5f,	 0.0f, // B
        0.5f,	 -0.5f,	 0.0f, // C

        -0.5f,	  0.5f,	 0.0f, // A
        0.5f,	 -0.5f,	 0.0f, // C
        0.5f,	  0.5f,	 0.0f // D
    };

    private final int vertexCount = VertexCoords.length / COORDS_PER_VERTEX;

    public GLModel_UIBlock()
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(VertexCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(VertexCoords);
        vertexBuffer.position(0);

        m_Offset = new float[2];
        m_Offset[0] = 0.0f;
        m_Offset[1] = 0.0f;

        m_Program = 0;
        m_ProjMatrixHandle = 0;
        m_PositionHandle = 0;
        m_ScaleHandle = 0;
        m_ColourHandle = 0;
        m_RotationHandle = 0;
        m_VertexHandle = 0;

        InitShaders();
    }

    public void draw(Vector2 pos, Vector2 scale, Colour colour, double rotation)
    {
        GLES20.glUniform2f(m_PositionHandle, (float)pos.X, (float)pos.Y);
        GLES20.glUniform2f(m_ScaleHandle, (float)scale.X, (float)scale.Y);
        GLES20.glUniform4f(m_ColourHandle, (float) colour.Red, (float) colour.Green, (float)colour.Blue, (float)colour.Alpha);
        GLES20.glUniform1f(m_RotationHandle, (float)rotation);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
    }

    public void InitShaders()
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

    @Override
    public void InitialiseModel(float[] projMatrix, Vector3 eye)
    {
        GLES20.glUseProgram(m_Program);

        GLES20.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);

        GLES20.glEnableVertexAttribArray(m_VertexHandle);
        GLES20.glVertexAttribPointer(m_VertexHandle, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer);
    }

    @Override
    public void Draw(GameObject obj)
    {}

    @Override
    public void CleanModel()
    {
        GLES20.glDisableVertexAttribArray(m_VertexHandle);
    }
}