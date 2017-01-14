package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES20;
import android.util.Log;

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class GLParticle
{
    private FloatBuffer m_VertexBuffer;
    private FloatBuffer m_SizeBuffer;
    private FloatBuffer m_ColourBuffer;

    private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_EyePosHandle;
    private int m_PositionHandle;
    private int m_ColourHandle;

    private ArrayList<Vector3> m_Points;
    private ArrayList<Colour> m_Colours;

    public GLParticle()
    {
        m_Program = 0;

        m_ProjMatrixHandle = 0;
        m_EyePosHandle = 0;
        m_PositionHandle = 0;
        m_ColourHandle = 0;

        m_Points = new ArrayList<>();
        m_Colours = new ArrayList<>();

        InitShaders();
    }

    public void InitialiseModel(float[] projMatrix, Vector3 eye)
    {
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glUseProgram(m_Program);

        GLES20.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);
        GLES20.glUniform4f(m_EyePosHandle, (float) eye.I, (float) eye.J, (float) eye.K, 1.0f);
    }

    public void AddPoint(Vector3 point, Colour colour)
    {
        m_Points.add(point);
        m_Colours.add(colour);
    }

    public void Draw()
    {
        int numPoints = m_Points.size();
        float[] vertices = new float[numPoints * 3];
        float[] size = new float[numPoints];
        float[] colours = new float[numPoints * 4];

        for(int i = 0; i < numPoints; i++)
        {
            Vector3 pos     = m_Points.get(i);
            vertices[i*3]   = (float)pos.I;
            vertices[i*3+1] = (float)pos.J;
            vertices[i*3+2] = (float)pos.K;

            Colour colour   = m_Colours.get(i);
            colours[i*4]   = (float)colour.Red;
            colours[i*4+1] = (float)colour.Green;
            colours[i*4+2] = (float)colour.Blue;
            colours[i*4+3] = (float)colour.Alpha;
        }

        ByteBuffer vb = ByteBuffer.allocateDirect(numPoints * 12);
        vb.order(ByteOrder.nativeOrder());
        m_VertexBuffer = vb.asFloatBuffer();
        m_VertexBuffer.put(vertices);
        m_VertexBuffer.position(0);

        ByteBuffer sb = ByteBuffer.allocateDirect(numPoints * 4);
        sb.order(ByteOrder.nativeOrder());
        m_SizeBuffer = sb.asFloatBuffer();
        m_SizeBuffer.put(size);
        m_SizeBuffer.position(0);

        ByteBuffer cb = ByteBuffer.allocateDirect(numPoints * 16);
        cb.order(ByteOrder.nativeOrder());
        m_ColourBuffer = cb.asFloatBuffer();
        m_ColourBuffer.put(colours);
        m_ColourBuffer.position(0);

        GLES20.glEnableVertexAttribArray(m_PositionHandle);
        GLES20.glVertexAttribPointer(m_PositionHandle, 3, GLES20.GL_FLOAT, false, 12, m_VertexBuffer);

        GLES20.glEnableVertexAttribArray(m_ColourHandle);
        GLES20.glVertexAttribPointer(m_ColourHandle, 4, GLES20.GL_FLOAT, false, 16, m_ColourBuffer);

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, numPoints);

        m_Points.clear();
        m_Colours.clear();
    }

    public void CleanModel()
    {
        GLES20.glDisableVertexAttribArray(m_PositionHandle);
        GLES20.glDisableVertexAttribArray(m_ColourHandle);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    }

    private void InitShaders()
    {
        int vertexShaderHandler 	= loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_POINT);
        int fragmentShaderHandler 	= loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_POINT);

        m_Program = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle      = GLES20.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_EyePosHandle          = GLES20.glGetUniformLocation(m_Program, "u_EyePos");
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
            Log.e("shader particle", "Shader failed to compile");
        }

        Log.e("shader particle", "Shader A-OK.");

        return shader;
    }
}