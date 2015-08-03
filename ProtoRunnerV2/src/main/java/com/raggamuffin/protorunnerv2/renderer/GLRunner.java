package com.raggamuffin.protorunnerv2.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class GLRunner extends GLModel
{
    public final FloatBuffer vertexBuffer;
    public final FloatBuffer barycentricCoordBuffer;

    private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_WorldPosHandle;
    private int m_YawHandle;
    private int m_RollHandle;
    private int m_ColourHandle;
    private int m_PositionHandle;
    private int m_BarycentricHandle;

    static final int COORDS_PER_VERTEX = 3;
    static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;	// 4 Bytes to a float.

    static float VertexCoords[] =
    {
        // Rear - Top Left.
        -1.0f,  0.0f, -1.0f, // A.
        0.0f,  0.75f, -1.5f, // B.
        0.0f,  0.0f, -0.5f, // E.

        // Rear - Top Right.
        0.0f,  0.75f, -1.5f, // B.
        1.0f,  0.0f, -1.0f, // C.
        0.0f,  0.0f, -0.5f, // E.

        // Rear - Bottom Right.
        1.0f,  0.0f, -1.0f, // C.
        0.0f, -0.75f, -1.2f, // D.
        0.0f,  0.0f, -0.5f, // E.

        // Rear - Bottom Left.
        0.0f, -0.75f, -1.2f, // D.
        -1.0f,  0.0f, -1.0f, // A.
        0.0f,  0.0f, -0.5f, // E.

        // Front - Top Left.
        -1.0f,  0.0f, -1.0f, // A.
        0.0f,  0.0f,  1.5f, // F.
        0.0f,  0.75f, -1.5f, // B.

        // Front - Top Right.
        1.0f,  0.0f, -1.0f, // C.
        0.0f,  0.75f, -1.5f, // B.
        0.0f,  0.0f,  1.5f, // F.

        // Front - Top Left.
        -1.0f,  0.0f, -1.0f, // A.
        0.0f, -0.75f, -1.2f, // D.
        0.0f,  0.0f,  1.5f, // F.

        // Front - Top Right.
        1.0f,  0.0f, -1.0f, // C.
        0.0f,  0.0f,  1.5f, // F.
        0.0f, -0.75f, -1.2f, // D.
    };

    private final int vertexCount = VertexCoords.length / COORDS_PER_VERTEX;

    static final int BARYCENTRICCOORDS_PER_VERTEX = 3;
    static final int BARYCENTRICCOORD_STRIDE = BARYCENTRICCOORDS_PER_VERTEX * 4;	// 4 Bytes to a float.

    static float BarycentricCoords[] =
    {
        // Rear - Top Left.
        1.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 1.0f,

        // Rear - Top Right.
        1.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 1.0f,

        // Rear - Bottom Right.
        1.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 1.0f,

        // Rear - Bottom Left.
        1.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 1.0f,

        // Front - Top Left.
        1.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 1.0f,

        // Front - Top Right.
        1.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 1.0f,

        // Front - Top Left.
        1.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 1.0f,

        // Front - Top Right.
        1.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 1.0f
    };

    public GLRunner()
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(VertexCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(VertexCoords);
        vertexBuffer.position(0);

        ByteBuffer bcb = ByteBuffer.allocateDirect(BarycentricCoords.length * 4);
        bcb.order(ByteOrder.nativeOrder());
        barycentricCoordBuffer = bcb.asFloatBuffer();
        barycentricCoordBuffer.put(BarycentricCoords);
        barycentricCoordBuffer.position(0);

        m_Program 			= 0;
        m_ProjMatrixHandle  = 0;
        m_WorldPosHandle    = 0;
        m_YawHandle         = 0;
        m_RollHandle        = 0;
        m_ColourHandle		= 0;
        m_PositionHandle	= 0;
        m_BarycentricHandle = 0;

        InitShaders();
    }

    public void draw(Vector3 pos, Vector3 scale, Colour colour, float roll, float yaw, float[] projMatrix)
    {
        GLES20.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);
        GLES20.glUniform4f(m_WorldPosHandle, (float) pos.I, (float) pos.J, (float) pos.K, 1.0f);
        GLES20.glUniform1f(m_YawHandle, yaw);
        GLES20.glUniform1f(m_RollHandle, roll);
        GLES20.glUniform4f(m_ColourHandle, (float) colour.Red, (float) colour.Green, (float) colour.Blue, (float) colour.Alpha);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
    }

    protected void InitShaders()
    {
        // prepare shaders and OpenGL program
        int vertexShaderHandler 	= loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_BARYCENTRIC);
        int fragmentShaderHandler 	= loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_BARYCENTRIC);

        m_Program = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle = GLES20.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_WorldPosHandle   = GLES20.glGetUniformLocation(m_Program, "u_WorldPos");
        m_YawHandle        = GLES20.glGetUniformLocation(m_Program, "u_Yaw");
        m_RollHandle       = GLES20.glGetUniformLocation(m_Program, "u_Roll");
        m_ColourHandle 	   = GLES20.glGetUniformLocation(m_Program, "u_Color");

        m_PositionHandle    = GLES20.glGetAttribLocation(m_Program, "a_Position");
        m_BarycentricHandle = GLES20.glGetAttribLocation(m_Program, "a_Barycentric");
    }

    @Override
    public void InitialiseModel(float[] yeah)
    {
        GLES20.glUseProgram(m_Program);

        GLES20.glEnableVertexAttribArray(m_PositionHandle);
        GLES20.glVertexAttribPointer(m_PositionHandle, GLRunner.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, GLRunner.VERTEX_STRIDE, vertexBuffer);

        GLES20.glEnableVertexAttribArray(m_BarycentricHandle);
        GLES20.glVertexAttribPointer(m_BarycentricHandle, GLRunner.BARYCENTRICCOORDS_PER_VERTEX, GLES20.GL_FLOAT, false, GLRunner.BARYCENTRICCOORD_STRIDE, barycentricCoordBuffer);
    }

    @Override
    public void CleanModel()
    {
        GLES20.glDisableVertexAttribArray(m_PositionHandle);
        GLES20.glDisableVertexAttribArray(m_BarycentricHandle);
    }

    @Override
    public void Draw(GameObject obj)
    {

    }
}
