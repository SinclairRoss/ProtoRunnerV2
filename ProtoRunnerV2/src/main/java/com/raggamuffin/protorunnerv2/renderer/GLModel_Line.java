package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES20;
import android.util.Log;

import com.raggamuffin.protorunnerv2.particles.Trail;
import com.raggamuffin.protorunnerv2.particles.TrailNode;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.concurrent.CopyOnWriteArrayList;

public class GLModel_Line
{
    private int m_MaxNodeCount;
    private int m_NodeCount;

    private FloatBuffer m_VertexBuffer;
    private FloatBuffer m_ColourBuffer;

    private float[] m_Vertices;
    private float[] m_Colours;

    private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_PositionHandle;
    private int m_ColourHandle;

    private Vector3 m_ToEye;

    public GLModel_Line()
    {
        m_Program = 0;

        m_ProjMatrixHandle = 0;
        m_PositionHandle = 0;
        m_ColourHandle = 0;

        m_MaxNodeCount = 1;
        ResizeBuffers(m_MaxNodeCount);

        m_ToEye = new Vector3();

        InitShaders();
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

    public void InitialiseModel(float[] projMatrix, Vector3 eye, Trail trail)
    {
        GLES20.glUseProgram(m_Program);
        GLES20.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);

        AddPoints(trail);

        if(!trail.GetNodes().isEmpty())
        {
            m_ToEye.SetVectorAsDifference(eye, trail.GetNodes().get(0).GetPosition());
        }
    }

    private void AddPoints(Trail trail)
    {
        CopyOnWriteArrayList<TrailNode> trailNodes = trail.GetNodes();

        int numTrailNodes = trailNodes.size();
        if (numTrailNodes > m_MaxNodeCount)
        {
            m_MaxNodeCount = numTrailNodes;
            ResizeBuffers(m_MaxNodeCount);
        }

        int i = 0;
        for(TrailNode node : trailNodes)
        {
            // Performing this check a second time protects against a bug triggered by
            // the size of the particles list changing while resizing the buffers.
            if (i >= m_MaxNodeCount)
            {
                m_MaxNodeCount = i+1;
                ResizeBuffers(m_MaxNodeCount);
            }

            Vector3 position = node.GetPosition();
            Colour colour = node.GetColour();

            try
            {
                m_Vertices[(i * 3)] = (float) position.X;
                m_Vertices[(i * 3) + 1] = (float) position.Y;
                m_Vertices[(i * 3) + 2] = (float) position.Z;

                m_Colours[(i * 4)] = (float) colour.Red;
                m_Colours[(i * 4) + 1] = (float) colour.Green;
                m_Colours[(i * 4) + 2] = (float) colour.Blue;
                m_Colours[(i * 4) + 3] = (float) colour.Alpha;
            }
            catch(IndexOutOfBoundsException e)
            {
                Log.e("particle", "Shits fucked");
            }

            ++i;
        }

        m_NodeCount = i;

        m_VertexBuffer.put(m_Vertices);
        m_VertexBuffer.position(0);

        m_ColourBuffer.put(m_Colours);
        m_ColourBuffer.position(0);

    }

    public void Draw()
    {
        float dist = (float) m_ToEye.GetLength();
        GLES20.glLineWidth((float) (20 * MathsHelper.FastInverseSqrt(dist)));

        GLES20.glEnableVertexAttribArray(m_PositionHandle);
        GLES20.glVertexAttribPointer(m_PositionHandle, 3, GLES20.GL_FLOAT, false, 12, m_VertexBuffer);

        GLES20.glEnableVertexAttribArray(m_ColourHandle);
        GLES20.glVertexAttribPointer(m_ColourHandle, 4, GLES20.GL_FLOAT, false, 16, m_ColourBuffer);

        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, m_NodeCount);
    }

    public void CleanModel()
    {
        GLES20.glDisableVertexAttribArray(m_PositionHandle);
        GLES20.glDisableVertexAttribArray(m_ColourHandle);
    }

    private void InitShaders()
    {
        int vertexShaderHandler 	= loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_TRAIL);
        int fragmentShaderHandler 	= loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_TRAIL);

        m_Program = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle      = GLES20.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_PositionHandle        = GLES20.glGetAttribLocation(m_Program, "a_Position");
        m_ColourHandle 			= GLES20.glGetAttribLocation(m_Program, "a_Color");
    }

    private int loadShader(int type, String shaderCode)
    {
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);

        if (compiled[0] == 0)
        {
            Log.e("shader trail", "Shader failed to compile");
        }

        return shader;
    }
}
