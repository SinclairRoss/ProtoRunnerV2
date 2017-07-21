package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES20;
import android.util.Log;

import com.raggamuffin.protorunnerv2.ObjectEffect.ObjectEffect_HealthBar;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector2;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLModel_UIRadial extends GLModel
{
    private final FloatBuffer m_VertexBuffer;
    private final FloatBuffer m_ProgressBuffer;

    private int m_NumVertices;

    private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_PositionHandle;
    private int m_ForwardHandle;
    private int m_UpHandle;
    private int m_RightHandle;
    private int m_ScaleHandle;
    private int m_ColourHandle;
    private int m_ProgressMarkHandle;
    private int m_ProgressHandle;

    private int m_VertexHandle;

    private Vector3 m_Eye;
    private Vector3 m_ToEye;

    public GLModel_UIRadial()
    {
        int numVertices = 7;
        float[] vertices = new float[numVertices * 3];
        float[] progress = new float[numVertices];

        float theta = 0.0f;
        final float deltaTheta = ((float)Math.PI * 2.0f) / (numVertices - 1);

        float progressMark = 0.0f;
        final float deltaProgress = 1.0f / (numVertices - 1);

        for(int i = 0; i < numVertices; ++i)
        {
            int index = i*3;
            vertices[index] = (float)Math.sin(theta);
            vertices[index+1] = (float)Math.cos(theta);
            vertices[index+2] = 0.0f;

            progress[i] = progressMark;

            theta += deltaTheta;
            progressMark += deltaProgress;
        }

        ByteBuffer vb = ByteBuffer.allocateDirect(vertices.length * 4);
        vb.order(ByteOrder.nativeOrder());
        m_VertexBuffer = vb.asFloatBuffer();
        m_VertexBuffer.put(vertices);
        m_VertexBuffer.position(0);

        ByteBuffer pb = ByteBuffer.allocateDirect(numVertices * 4);
        pb.order(ByteOrder.nativeOrder());
        m_ProgressBuffer = pb.asFloatBuffer();
        m_ProgressBuffer.put(progress);
        m_ProgressBuffer.position(0);

        m_NumVertices = vertices.length / 3;

        InitShaders();

        m_Eye = new Vector3();
        m_ToEye = new Vector3();
    }

    @Override
    public void InitialiseModel(float[] projMatrix, Vector3 eye)
    {
        GLES20.glUseProgram(m_Program);

        GLES20.glEnableVertexAttribArray(m_VertexHandle);
        GLES20.glVertexAttribPointer(m_VertexHandle, 3, GLES20.GL_FLOAT, false, 12, m_VertexBuffer);

        GLES20.glEnableVertexAttribArray(m_ProgressMarkHandle);
        GLES20.glVertexAttribPointer(m_ProgressMarkHandle, 1, GLES20.GL_FLOAT, false, 4, m_ProgressBuffer);

        GLES20.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);

        if(eye != null)
        {
            m_Eye.SetVector(eye);
        }
    }

    // For use irl.
    public void Draw(Vector3 pos, Vector3 scale, Vector3 fwd, Vector3 right, Vector3 up, Colour colour, double lineWidth, double progress)
    {
        GLES20.glUniform3f(m_PositionHandle, (float)pos.X, (float)pos.Y, (float)pos.Z);

        GLES20.glUniform3f(m_ForwardHandle, (float) fwd.X, (float) fwd.Y, (float) fwd.Z);
        GLES20.glUniform3f(m_RightHandle, (float) right.X, (float) right.Y, (float) right.Z);
        GLES20.glUniform3f(m_UpHandle, (float) up.X, (float) up.Y, (float) up.Z);

        GLES20.glUniform3f(m_ScaleHandle, (float)scale.X, (float)scale.Y, (float)scale.Z);
        GLES20.glUniform4f(m_ColourHandle, (float) colour.Red, (float) colour.Green, (float)colour.Blue, (float)colour.Alpha);
        GLES20.glUniform1f(m_ProgressHandle, (float)progress);

        m_ToEye.SetVectorAsDifference(m_Eye, pos);
        float distanceToEye = (float)m_ToEye.GetLength();

        GLES20.glLineWidth((float)(lineWidth * MathsHelper.FastInverseSqrt(distanceToEye)));
        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, m_NumVertices);
    }

    // For use in UI
    public void Draw(Vector2 pos, Vector2 scale, Colour colour, double lineWidth, double progress)
    {
        GLES20.glUniform3f(m_PositionHandle, (float)pos.X, (float)pos.Y, 0);

        GLES20.glUniform3f(m_RightHandle, 1, 0, 0);
        GLES20.glUniform3f(m_UpHandle, 0, 1, 0);
        GLES20.glUniform3f(m_ForwardHandle, 0, 0, 1);

        GLES20.glUniform3f(m_ScaleHandle, (float)scale.X, (float)scale.Y, 0);
        GLES20.glUniform4f(m_ColourHandle, (float) colour.Red, (float) colour.Green, (float)colour.Blue, (float)colour.Alpha);
        GLES20.glUniform1f(m_ProgressHandle, (float)progress);

        GLES20.glLineWidth((float)lineWidth);
        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, m_NumVertices);
    }

    @Override
    public void Draw(GameObject obj)
    {
        ObjectEffect_HealthBar healthBar = (ObjectEffect_HealthBar) obj;
        Draw(obj.GetPosition(), obj.GetScale(), obj.GetForward(), obj.GetRight(), obj.GetUp(), obj.GetColour(), healthBar.GetLineWidth(), healthBar.Progress());
    }

    public void CleanModel()
    {
        GLES20.glDisableVertexAttribArray(m_VertexHandle);
        GLES20.glDisableVertexAttribArray(m_ProgressMarkHandle);
    }

    protected void InitShaders()
    {
        // prepare shaders and OpenGL program
        int vertexShaderHandler = loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_UIRADIAL);
        int fragmentShaderHandler = loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_UIRADIAL);

        m_Program = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle      = GLES20.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_PositionHandle        = GLES20.glGetUniformLocation(m_Program, "u_Position");
        m_ScaleHandle           = GLES20.glGetUniformLocation(m_Program, "u_Scale");
        m_ForwardHandle    = GLES20.glGetUniformLocation(m_Program, "u_Forward");
        m_UpHandle         = GLES20.glGetUniformLocation(m_Program, "u_Up");
        m_RightHandle      = GLES20.glGetUniformLocation(m_Program, "u_Right");

        m_ProgressHandle = GLES20.glGetUniformLocation(m_Program, "u_Progress");
        m_ColourHandle = GLES20.glGetUniformLocation(m_Program, "u_Color");

        m_VertexHandle = GLES20.glGetAttribLocation(m_Program, "a_Vertices");
        m_ProgressMarkHandle = GLES20.glGetAttribLocation(m_Program, "a_ProgressMark");
    }
}
