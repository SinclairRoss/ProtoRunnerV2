package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES20;

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class GLRunnerTest extends GLModel
{
    private FloatBuffer m_VertexBuffer;
    private IntBuffer m_IDBuffer;

    private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_VertexPosHandle;
    private int m_WorldPosHandle;
    private int m_InstanceIDHandle;

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

    private int m_NumInstances;

    private ArrayList<Float> m_VertexPositionCoords;
    private ArrayList<Float> m_WorldPositionCoords;
    private ArrayList<Integer> m_InstanceIDs;
    private ArrayList<Float> m_WorldPosCoords;

    private int m_TotalVertexCount;

    public GLRunnerTest()
    {
        m_VertexPositionCoords = new ArrayList<Float>();
        m_WorldPositionCoords = new ArrayList<Float>();
        m_InstanceIDs = new ArrayList<Integer>();
        m_WorldPosCoords = new ArrayList<Float>();
        m_TotalVertexCount = 0;

        m_Program 			= 0;
        m_ProjMatrixHandle  = 0;
        m_VertexPosHandle   = 0;
        m_InstanceIDHandle  = 0;

        InitShaders();
    }

    public void AddToBuffer(Vector3 pos, Colour colour, Vector3 scale, Vector3 forward, float roll, float yaw)
    {
        m_TotalVertexCount += vertexCount;

        for(float coord : VertexCoords)
        {
            m_VertexPositionCoords.add(coord);
            m_InstanceIDs.add(m_NumInstances);
        }

        m_WorldPosCoords.add((float) pos.I);
        m_WorldPosCoords.add((float) pos.J);
        m_WorldPosCoords.add((float) pos.K);

        m_NumInstances ++;
    }

    @Override
    public void Draw(float[] projMatrix)
    {
        GLES20.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);

        int size = m_WorldPosCoords.size();

        float[] worldPos = new float[3];
        Arrays.fill(worldPos, 0.0f);

        for(int i = 0; i < size; i++)
        {
            worldPos[i] = m_WorldPosCoords.get(i);
        }

        GLES20.glUniform1fv(m_WorldPosHandle, size, worldPos, 0);

        SetBuffer(m_VertexPosHandle, m_VertexPositionCoords, 3, 0);
        SetBuffer(m_InstanceIDHandle, m_InstanceIDs, 1);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, m_TotalVertexCount);
    }

    private void SetBuffer(final int handle, final ArrayList<Float> arrayList, final int stride, final int bufferIndex)
    {
        int size = arrayList.size();
        float[] array = new float[size];

        for(int a = 0; a < size; a++)
            array[a] = arrayList.get(a);

        ByteBuffer bb = ByteBuffer.allocateDirect(size * 4);
        bb.order(ByteOrder.nativeOrder());

        m_VertexBuffer = bb.asFloatBuffer();
        m_VertexBuffer.put(array);
        m_VertexBuffer.position(0);

        GLES20.glEnableVertexAttribArray(handle);
        GLES20.glVertexAttribPointer(handle, stride, GLES20.GL_FLOAT, false, stride * 4, m_VertexBuffer);
    }

    private void SetBuffer(final int handle, final ArrayList<Integer> arrayList, final int stride)
    {
        int size = arrayList.size();
        int[] array = new int[size];

        for(int a = 0; a < size; a++)
            array[a] = arrayList.get(a);

        ByteBuffer bb = ByteBuffer.allocateDirect(size * 4);
        bb.order(ByteOrder.nativeOrder());

        m_IDBuffer = bb.asIntBuffer();
        m_IDBuffer.put(array);
        m_IDBuffer.position(0);

        GLES20.glEnableVertexAttribArray(handle);
        GLES20.glVertexAttribPointer(handle, stride, GLES20.GL_INT, false, stride * 4, m_IDBuffer);
    }

    private void InitShaders()
    {
        // prepare shaders and OpenGL program
        int vertexShaderHandler 	= loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_INSTANCING_TEST);
        int fragmentShaderHandler 	= loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_INSTANCING_TEST);

        m_Program = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle  = GLES20.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_WorldPosHandle    = GLES20.glGetUniformLocation(m_Program, "u_WorldPos");

        m_VertexPosHandle   = GLES20.glGetAttribLocation(m_Program, "a_Position");
        m_InstanceIDHandle  = GLES20.glGetAttribLocation(m_Program, "a_InstanceID");
    }

    @Override
    public void InitialiseModel()
    {
        GLES20.glUseProgram(m_Program);
    }

    @Override
    public void CleanModel()
    {
        GLES20.glDisableVertexAttribArray(m_VertexPosHandle);
        GLES20.glDisableVertexAttribArray(m_WorldPosHandle);

        m_WorldPositionCoords.clear();
        m_WorldPosCoords.clear();
        m_InstanceIDs.clear();
        m_TotalVertexCount = 0;
        m_NumInstances = 0;

        m_IDBuffer.clear();
        m_VertexBuffer.clear();

    }

    @Override
    public int GetVertexCount()
    {
        return vertexCount;
    }
}
