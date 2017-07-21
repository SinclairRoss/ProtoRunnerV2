package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES20;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLModel_DamageTri extends GLModel
{
    private final FloatBuffer m_VertexBuffer;

    private int m_NumVertices;

    private final float LINE_WIDTH = 60;
    private Vector3 m_EyePos;

    private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_PositionHandle;
    private int m_ForwardHandle;
    private int m_UpHandle;
    private int m_RightHandle;
    private int m_ScaleHandle;
    private int m_ColourHandle;
    private int m_VertexHandle;

    public GLModel_DamageTri()
    {
        int numVertices = 3;
        float[] vertices = new float[numVertices * 3];

        double theta = 0.0;
        double deltaTheta = (Math.PI * 2.0f) / numVertices;

        for(int i = 0; i < numVertices; ++i)
        {
            int index = i*3;
            vertices[index] = -(float)Math.sin(theta);
            vertices[index+1] = -(float)Math.cos(theta);
            vertices[index+2] = 0.0f;

            theta += deltaTheta;
        }

        ByteBuffer vb = ByteBuffer.allocateDirect(vertices.length * 4);
        vb.order(ByteOrder.nativeOrder());
        m_VertexBuffer = vb.asFloatBuffer();
        m_VertexBuffer.put(vertices);
        m_VertexBuffer.position(0);

        m_NumVertices = vertices.length / 3;

        InitShaders();

        m_EyePos = new Vector3();
    }

    @Override
    public void InitialiseModel(float[] projMatrix, Vector3 eye)
    {
        GLES20.glUseProgram(m_Program);

        GLES20.glEnableVertexAttribArray(m_VertexHandle);
        GLES20.glVertexAttribPointer(m_VertexHandle, 3, GLES20.GL_FLOAT, false, 12, m_VertexBuffer);

        GLES20.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);

        m_EyePos.SetVector(eye);
    }

    @Override
    public void Draw(GameObject obj)
    {
        Vector3 pos = obj.GetPosition();
        GLES20.glUniform3f(m_PositionHandle, (float) pos.X, (float) pos.Y, (float) pos.Z);

        Vector3 fwd = obj.GetForward();
        GLES20.glUniform3f(m_ForwardHandle, (float) fwd.X, (float) fwd.Y, (float) fwd.Z);

        Vector3 right = obj.GetRight();
        GLES20.glUniform3f(m_RightHandle, (float) right.X, (float) right.Y, (float) right.Z);

        Vector3 up = obj.GetUp();
        GLES20.glUniform3f(m_UpHandle, (float) up.X, (float) up.Y, (float) up.Z);

        Vector3 scale = obj.GetScale();
        GLES20.glUniform3f(m_ScaleHandle, (float) scale.X, (float) scale.Y, (float) scale.Z);

        Colour colour = obj.GetColour();
        GLES20.glUniform4f(m_ColourHandle, (float) colour.Red, (float) colour.Green, (float) colour.Blue, (float) colour.Alpha);

        float distanceToCamera = (float)Vector3.DistanceBetween(pos, m_EyePos);
        GLES20.glLineWidth(LINE_WIDTH * (float)MathsHelper.FastInverseSqrt(distanceToCamera));

        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, m_NumVertices);
    }

    @Override
    public void CleanModel()
    {
        GLES20.glDisableVertexAttribArray(m_PositionHandle);
    }

    @Override
    protected void InitShaders()
    {
        // prepare shaders and OpenGL program
        int vertexShaderHandler = loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_STANDARD);
        int fragmentShaderHandler = loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_BLOCKCOLOUR);

        m_Program = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);

        m_ProjMatrixHandle = GLES20.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_PositionHandle   = GLES20.glGetUniformLocation(m_Program, "u_Position");
        m_ForwardHandle    = GLES20.glGetUniformLocation(m_Program, "u_Forward");
        m_UpHandle         = GLES20.glGetUniformLocation(m_Program, "u_Up");
        m_RightHandle      = GLES20.glGetUniformLocation(m_Program, "u_Right");
        m_ScaleHandle      = GLES20.glGetUniformLocation(m_Program, "u_Scale");
        m_ColourHandle 	   = GLES20.glGetUniformLocation(m_Program, "u_Color");

        m_VertexHandle     = GLES20.glGetAttribLocation(m_Program, "a_Vertices");
    }
}
