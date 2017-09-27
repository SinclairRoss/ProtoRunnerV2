package com.raggamuffin.protorunnerv2.renderer;

// Author: Sinclair Ross
// Date:   21/06/2016

import android.opengl.GLES31;

import com.raggamuffin.protorunnerv2.RenderObjects.RenderObject;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.Tentacle;
import com.raggamuffin.protorunnerv2.particles.RopeNode;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class GLModel_Rope extends GLModel
{
    private FloatBuffer m_VertexBuffer;
    private FloatBuffer m_LengthBuffer;
    private FloatBuffer m_AlphaBuffer;

    private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_PositionHandle;
    private int m_NormalisedLengthHandle;
    private int m_AlphaHandle;
    private int m_HotColourHandle;
    private int m_ColdColourHandle;
    private int m_ColourBloomPointHandle;

    private final int m_RopeResolution;
    private int m_NodeIndex;
    private float[] m_Vertices;
    private float[] m_Lengths;
    private float[] m_AlphaVals;

    private Vector3 m_EyePos;
    private Vector3 m_RopeEndPoint;

    public GLModel_Rope()
    {
        m_Program = 0;

        m_ProjMatrixHandle = 0;
        m_PositionHandle = 0;
        m_NormalisedLengthHandle = 0;
        m_AlphaHandle = 0;
        m_HotColourHandle = 0;
        m_ColdColourHandle = 0;
        m_ColourBloomPointHandle = 0;

        m_RopeResolution = Tentacle.ROPE_RESOLUTION;
        m_NodeIndex = 0;

        ByteBuffer byteBuffer_Vertex = ByteBuffer.allocateDirect(m_RopeResolution * 12);
        byteBuffer_Vertex.order(ByteOrder.nativeOrder());
        m_VertexBuffer = byteBuffer_Vertex.asFloatBuffer();

        ByteBuffer byteBuffer_Length = ByteBuffer.allocateDirect(m_RopeResolution * 4);
        byteBuffer_Length.order(ByteOrder.nativeOrder());
        m_LengthBuffer = byteBuffer_Length.asFloatBuffer();

        ByteBuffer byteBuffer_Alpha = ByteBuffer.allocateDirect(m_RopeResolution * 4);
        byteBuffer_Alpha.order(ByteOrder.nativeOrder());
        m_AlphaBuffer = byteBuffer_Alpha.asFloatBuffer();

        m_Vertices = new float[m_RopeResolution * 3];
        m_Lengths = new float[m_RopeResolution];
        m_AlphaVals = new float[m_RopeResolution];

        m_EyePos = new Vector3();
        m_RopeEndPoint = new Vector3();

        InitShaders();
    }

    @Override
    public void InitialiseModel(float[] projMatrix, Vector3 eye)
    {
        GLES31.glUseProgram(m_Program);

        GLES31.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);

        m_EyePos.SetVector(eye);
    }

    @Override
    public void Draw(RenderObject obj)
    {}

    public void AddPoint(RopeNode node)
    {
        m_Vertices[m_NodeIndex*3] = (float)node.GetPosition().X;
        m_Vertices[m_NodeIndex*3+1] = (float)node.GetPosition().Y;
        m_Vertices[m_NodeIndex*3+2] = (float)node.GetPosition().Z;

        m_Lengths[m_NodeIndex] = (float)node.GetNormalisedLength();
        m_AlphaVals[m_NodeIndex] = (float)node.GetAlpha();

        ++m_NodeIndex;
    }

    public void Draw(Colour coldColour, Colour hotColour, double bloomPoint)
    {
        double x = m_Vertices[m_Vertices.length - 3];
        double y = m_Vertices[m_Vertices.length - 2];
        double z = m_Vertices[m_Vertices.length - 1];

        m_RopeEndPoint.SetVector(x, y, z);
        float dist = (float)Vector3.GetDistanceBetween(m_EyePos, m_RopeEndPoint);

        GLES31.glLineWidth((float) (40 * MathsHelper.FastInverseSqrt(dist)));

       // GLES31.glUniform4f(m_ColdColourHandle, (float) coldColour.Red, (float) coldColour.Green, (float) coldColour.Blue, (float) coldColour.Alpha);
        //GLES31.glUniform4f(m_HotColourHandle, (float) hotColour.Red, (float) hotColour.Green, (float) hotColour.Blue, (float) hotColour.Alpha);
        GLES31.glUniform1f(m_ColourBloomPointHandle, (float)bloomPoint);

        m_VertexBuffer.put(m_Vertices);
        m_VertexBuffer.position(0);

        GLES31.glEnableVertexAttribArray(m_PositionHandle);
        GLES31.glVertexAttribPointer(m_PositionHandle, 3, GLES31.GL_FLOAT, false, 12, m_VertexBuffer);

        m_LengthBuffer.put(m_Lengths);
        m_LengthBuffer.position(0);

        GLES31.glEnableVertexAttribArray(m_NormalisedLengthHandle);
        GLES31.glVertexAttribPointer(m_NormalisedLengthHandle, 1, GLES31.GL_FLOAT, false, 4, m_LengthBuffer);

        m_AlphaBuffer.put(m_AlphaVals);
        m_AlphaBuffer.position(0);

        GLES31.glEnableVertexAttribArray(m_AlphaHandle);
        GLES31.glVertexAttribPointer(m_AlphaHandle, 1, GLES31.GL_FLOAT, false, 4, m_AlphaBuffer);

        GLES31.glDrawArrays(GLES31.GL_LINE_STRIP, 0, Tentacle.ROPE_RESOLUTION);
        m_NodeIndex = 0;
    }

    @Override
    public void CleanModel()
    {
        GLES31.glDisableVertexAttribArray(m_PositionHandle);
    }

    @Override
    protected void InitShaders()
    {
        int vertexShaderHandler 	= loadShader(GLES31.GL_VERTEX_SHADER,Shaders.vertexShader_ROPE);
        int fragmentShaderHandler 	= loadShader(GLES31.GL_FRAGMENT_SHADER,Shaders.fragmentShader_ROPE);

        m_Program = GLES31.glCreateProgram();             	     // create empty OpenGL Program
        GLES31.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES31.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES31.glLinkProgram(m_Program);                  		 // create OpenGL program executables

        m_ProjMatrixHandle = GLES31.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_HotColourHandle = GLES31.glGetUniformLocation(m_Program, "u_HotColor");
        m_ColdColourHandle = GLES31.glGetUniformLocation(m_Program, "u_ColdColor");
        m_ColourBloomPointHandle = GLES31.glGetUniformLocation(m_Program, "u_ColorBloomPoint");

        m_PositionHandle = GLES31.glGetAttribLocation(m_Program, "a_Position");
        m_NormalisedLengthHandle = GLES31.glGetAttribLocation(m_Program, "a_NormalisedLength");
        m_AlphaHandle = GLES31.glGetAttribLocation(m_Program, "a_Alpha");
    }
}