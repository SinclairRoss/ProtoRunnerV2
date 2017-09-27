package com.raggamuffin.protorunnerv2.renderer;

// Author: Sinclair Ross
// Date:   15/01/2017

import android.opengl.GLES31;
import android.util.Log;

import com.raggamuffin.protorunnerv2.RenderObjects.RenderObject;
import com.raggamuffin.protorunnerv2.RenderObjects.RenderObject_Particle;
import com.raggamuffin.protorunnerv2.particles.Particle;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class GLParticleGroup
{
    private int m_MaxParticleCount;
    private int m_ParticleCount;

    private FloatBuffer m_VertexBuffer;
    private FloatBuffer m_ColourBuffer;

    private float[] m_Vertices;
    private float[] m_Colours;

    private int m_Program;
    private int m_ProjMatrixHandle;
    private int m_EyePosHandle;
    protected int m_PositionHandle;
    protected int m_ColourHandle;

    public GLParticleGroup(String vertexShader, String fragmentShader)
    {
        m_Program = 0;
        m_ProjMatrixHandle = 0;
        m_EyePosHandle = 0;
        m_PositionHandle = 0;
        m_ColourHandle = 0;

        m_MaxParticleCount = 1000;
        ResizeBuffers(m_MaxParticleCount);

        CreateProgram(vertexShader, fragmentShader);
    }

    public void InitialiseModel(float[] projMatrix, Vector3 eye)
    {
        GLES31.glDisable(GLES31.GL_DEPTH_TEST);
        GLES31.glUseProgram(m_Program);

        GLES31.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);
        GLES31.glUniform4f(m_EyePosHandle, (float) eye.X, (float) eye.Y, (float) eye.Z, 1.0f);
    }

    public void CleanModel()
    {
        GLES31.glDisableVertexAttribArray(m_PositionHandle);
        GLES31.glDisableVertexAttribArray(m_ColourHandle);
        GLES31.glEnable(GLES31.GL_DEPTH_TEST);
    }

    public void AddPoints(ArrayList<RenderObject_Particle> particles, int particleCount)
    {
        m_ParticleCount = particleCount;
        if(m_ParticleCount > m_MaxParticleCount)
        {
            m_MaxParticleCount = m_ParticleCount;
            ResizeBuffers(m_MaxParticleCount);
        }

        for(int i = 0; i < m_ParticleCount; ++i)
        {
            RenderObject_Particle particle = particles.get(i);

            Vector3 position = particle.GetPosition();
            Colour colour = particle.GetColour();

            m_Vertices[(i * 3)] = (float) position.X;
            m_Vertices[(i * 3) + 1] = (float) position.Y;
            m_Vertices[(i * 3) + 2] = (float) position.Z;

          //  m_Colours[(i * 4)] = (float) colour.Red;
          //  m_Colours[(i * 4) + 1] = (float) colour.Green;
          //  m_Colours[(i * 4) + 2] = (float) colour.Blue;
          //  m_Colours[(i * 4) + 3] = (float) colour.Alpha;
        }

        m_VertexBuffer.put(m_Vertices);
        m_VertexBuffer.position(0);

        m_ColourBuffer.put(m_Colours);
        m_ColourBuffer.position(0);
    }

    public void Draw()
    {
        GLES31.glEnableVertexAttribArray(m_PositionHandle);
        GLES31.glVertexAttribPointer(m_PositionHandle, 3, GLES31.GL_FLOAT, false, 12, m_VertexBuffer);

        GLES31.glEnableVertexAttribArray(m_ColourHandle);
        GLES31.glVertexAttribPointer(m_ColourHandle, 4, GLES31.GL_FLOAT, false, 16, m_ColourBuffer);

        GLES31.glDrawArrays(GLES31.GL_POINTS, 0, m_ParticleCount);
    }

    private void ResizeBuffers(int size)
    {
        ByteBuffer byteBuffer_Vertex = ByteBuffer.allocateDirect(size * 12);
        byteBuffer_Vertex.order(ByteOrder.nativeOrder());
        m_VertexBuffer = byteBuffer_Vertex.asFloatBuffer();

        m_Vertices = new float[size * 3];

        ByteBuffer byteBuffer_Colour = ByteBuffer.allocateDirect(size * 16);
        byteBuffer_Colour.order(ByteOrder.nativeOrder());
        m_ColourBuffer = byteBuffer_Colour.asFloatBuffer();

        m_Colours = new float[size * 4];
    }

    protected int loadShader(int type, String shaderCode)
    {
        int shader = GLES31.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES31.glShaderSource(shader, shaderCode);
        GLES31.glCompileShader(shader);

        int[] compiled = new int[1];
        GLES31.glGetShaderiv(shader, GLES31.GL_COMPILE_STATUS, compiled, 0);

        if (compiled[0] == 0)
        {
            Log.e("shader particle", "Shader failed to compile");
        }

        return shader;
    }

    private int CreateProgram(String vertexShader, String fragmentShader)
    {
        int vertexShaderHandler = loadShader(GLES31.GL_VERTEX_SHADER, vertexShader);
        int fragmentShaderHandler = loadShader(GLES31.GL_FRAGMENT_SHADER, fragmentShader);

        m_Program = GLES31.glCreateProgram();             		// create empty OpenGL Program
        GLES31.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES31.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES31.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle = GLES31.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_EyePosHandle = GLES31.glGetUniformLocation(m_Program, "u_EyePos");
        m_PositionHandle = GLES31.glGetAttribLocation(m_Program, "a_Position");
        m_ColourHandle = GLES31.glGetAttribLocation(m_Program, "a_Color");

        return m_Program;
    }
}
