package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES31;
import android.util.Log;

import com.raggamuffin.protorunnerv2.RenderObjects.RenderObject_Trail;
import com.raggamuffin.protorunnerv2.RenderObjects.RenderObject_TrailNode;
import com.raggamuffin.protorunnerv2.particles.TrailNode;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
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

    public void InitialiseModel(float[] projMatrix)
    {
        GLES31.glUseProgram(m_Program);
        GLES31.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);
    }

    public void Draw(RenderObject_Trail trail, Vector3 eye)
    {
        AddPoints(trail, eye);

        float dist = (float) m_ToEye.GetLength();
        GLES31.glLineWidth((float) (20 * MathsHelper.FastInverseSqrt(dist)));

        GLES31.glEnableVertexAttribArray(m_PositionHandle);
        GLES31.glVertexAttribPointer(m_PositionHandle, 3, GLES31.GL_FLOAT, false, 12, m_VertexBuffer);

        GLES31.glEnableVertexAttribArray(m_ColourHandle);
        GLES31.glVertexAttribPointer(m_ColourHandle, 4, GLES31.GL_FLOAT, false, 16, m_ColourBuffer);

        GLES31.glDrawArrays(GLES31.GL_LINE_STRIP, 0, m_NodeCount);
    }

    public void AddPoints(RenderObject_Trail trail, Vector3 eye)
    {
        if(!trail.GetNodes().isEmpty())
        {
            m_ToEye.SetVectorAsDifference(eye, trail.GetNodes().get(0).GetPosition());
        }

        ArrayList<RenderObject_TrailNode> nodes = trail.GetNodes();

        m_NodeCount = trail.GetNodeCount();
        if (m_NodeCount > m_MaxNodeCount)
        {
            m_MaxNodeCount = m_NodeCount;
            ResizeBuffers(m_MaxNodeCount);
        }

        for(int i = 0; i < m_NodeCount; ++i)
        {
            RenderObject_TrailNode node = nodes.get(i);

            Vector3 position = node.GetPosition();
            m_Vertices[(i * 3)] = (float) position.X;
            m_Vertices[(i * 3) + 1] = (float) position.Y;
            m_Vertices[(i * 3) + 2] = (float) position.Z;

          //  Colour colour = node.GetColour();
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

    public void CleanModel()
    {
        GLES31.glDisableVertexAttribArray(m_PositionHandle);
        GLES31.glDisableVertexAttribArray(m_ColourHandle);
    }

    private void InitShaders()
    {
        int vertexShaderHandler 	= loadShader(GLES31.GL_VERTEX_SHADER,Shaders.vertexShader_TRAIL);
        int fragmentShaderHandler 	= loadShader(GLES31.GL_FRAGMENT_SHADER,Shaders.fragmentShader_TRAIL);

        m_Program = GLES31.glCreateProgram();             		// create empty OpenGL Program
        GLES31.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES31.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES31.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle      = GLES31.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_PositionHandle        = GLES31.glGetAttribLocation(m_Program, "a_Position");
        m_ColourHandle 			= GLES31.glGetAttribLocation(m_Program, "a_Color");
    }

    private int loadShader(int type, String shaderCode)
    {
        int shader = GLES31.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES31.glShaderSource(shader, shaderCode);
        GLES31.glCompileShader(shader);

        int[] compiled = new int[1];
        GLES31.glGetShaderiv(shader, GLES31.GL_COMPILE_STATUS, compiled, 0);

        if (compiled[0] == 0)
        {
            Log.e("shader trail", "Shader failed to compile");
        }

        return shader;
    }
}
