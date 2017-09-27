package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES31;
import android.util.Log;

import com.raggamuffin.protorunnerv2.RenderObjects.RenderObject;
import com.raggamuffin.protorunnerv2.RenderObjects.RenderObject_Vehicle;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLModel_StandardObject_Instanced extends GLModel
{
    private final static int MAX_INSTANCES = 32;
    private final static int INSTANCEDATA_BINDPOINT = 0;

    private final FloatBuffer m_VertexBuffer;
    private final FloatBuffer m_InstanceDataBuffer_asdf;

    private int m_NumInstances;

    private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_VertexHandle;
    private int m_BarycentricHandle;

    private int m_InstanceDataHandle;

    private int[] m_InstanceDataBuffer;
    private float[] m_Data;

    private int m_InstanceCount;

    public GLModel_StandardObject_Instanced(float[] vertices)
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

        ByteBuffer ib = ByteBuffer.allocateDirect(12 * MAX_INSTANCES);
        vb.order(ByteOrder.nativeOrder());
        m_InstanceDataBuffer_asdf = ib.asFloatBuffer();
        m_InstanceDataBuffer_asdf.position(0);


        m_NumInstances = 0;

        m_Program 			= 0;
        m_ProjMatrixHandle  = 0;

        m_VertexHandle	    = 0;
        m_BarycentricHandle = 0;

        m_Data = new float[3 * MAX_INSTANCES];

        InitShaders();
    }

    @Override
    public void InitialiseModel(float[] projMatrix, Vector3 eye)
    {
        GLES31.glUseProgram(m_Program);

        GLES31.glEnableVertexAttribArray(m_VertexHandle);
        GLES31.glVertexAttribPointer(m_VertexHandle, 3, GLES31.GL_FLOAT, false, 12, m_VertexBuffer);

        GLES31.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);
    }

    public void Draw(RenderObject obj)
    {
        if(obj instanceof RenderObject_Vehicle)
        {
            if(m_InstanceCount < MAX_INSTANCES)
            {
                RenderObject_Vehicle vehicle = (RenderObject_Vehicle) obj;
//                Vector3 pos = vehicle.GetPosition();

                int index = m_InstanceCount * 3;
              //  m_Data[index] = (float)pos.X;
              //  m_Data[++index] = (float)pos.Y;
              //  m_Data[++index] = (float)pos.Z;

                ++m_InstanceCount;
            }
            else
            {
                Log.e("<-- CRITICAL_ERROR -->","To many instances");
            }
        }
        else
        {
            Log.e("<-- CRITICAL_ERROR -->","Not instance of vehicle");
        }
    }

    @Override
    public void CleanModel()
    {
        m_InstanceDataBuffer_asdf.position(0);
        m_InstanceDataBuffer_asdf.put(m_Data);

        GLES31.glBindBuffer(GLES31.GL_UNIFORM_BUFFER, m_InstanceDataBuffer[0]);
        GLES31.glBufferSubData(GLES31.GL_UNIFORM_BUFFER, 0, 12, m_InstanceDataBuffer_asdf);

        GLES31.glDrawArraysInstanced(GLES31.GL_TRIANGLES, 0, MAX_INSTANCES, m_NumInstances );
        GLES31.glDisableVertexAttribArray(m_BarycentricHandle);
    }

    @Override
    protected void InitShaders()
    {
        if(true)
        {
            return;
        }

        // prepare shaders and OpenGL program
        int vertexShaderHandler = loadShader(GLES31.GL_VERTEX_SHADER,Shaders.vertexShader_INSTANCED);
        int fragmentShaderHandler = loadShader(GLES31.GL_FRAGMENT_SHADER,Shaders.fragmentShader_INSTANCED);

        m_Program = GLES31.glCreateProgram();             		// create empty OpenGL Program
        GLES31.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES31.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES31.glLinkProgram(m_Program);                  		 // create OpenGL program executables

        m_ProjMatrixHandle = GLES31.glGetUniformLocation(m_Program, "u_ProjMatrix");

        m_VertexHandle      = GLES31.glGetAttribLocation(m_Program, "a_Vertices");
        m_BarycentricHandle = GLES31.glGetAttribLocation(m_Program, "a_Barycentric");

        m_InstanceDataHandle = GLES31.glGetUniformBlockIndex(m_Program, "InstanceData");
        GLES31.glUniformBlockBinding(m_Program, m_InstanceDataHandle, INSTANCEDATA_BINDPOINT);

        GLES31.glGenBuffers(1, m_InstanceDataBuffer, 0);
        GLES31.glBindBuffer(GLES31.GL_UNIFORM_BUFFER, m_InstanceDataBuffer[0]);

        GLES31.glBufferData(GLES31.GL_UNIFORM_BUFFER, 12 * MAX_INSTANCES, null, GLES31.GL_DYNAMIC_DRAW );
        GLES31.glBindBufferBase(GLES31.GL_UNIFORM_BUFFER, INSTANCEDATA_BINDPOINT, m_InstanceDataBuffer[0]);
    }

   //class InstanceData
   //{
   //    static final int DATA_SIZE = 4 * 3 * 5;

   //    float[] u_Position;
   //    float[] u_Forward;
   //    float[] u_Up;
   //    float[] u_Right;
   //    float[] u_Scale;

   //    public InstanceData()
   //    {
   //        u_Position = new float[3];
   //        u_Forward = new float[3];
   //        u_Up = new float[3];
   //        u_Right = new float[3];
   //        u_Scale = new float[3];
   //    }

   //    public void SetData(Vector3 position, Vector3 forward, Vector3 up, Vector3 right, Vector3 scale)
   //    {
   //        position.ToFloatArray(u_Position);
   //        forward.ToFloatArray(u_Forward);
   //        up.ToFloatArray(u_Up);
   //        right.ToFloatArray(u_Right);
   //        scale.ToFloatArray(u_Scale);
   //    }
   //}
}
