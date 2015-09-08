package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES20;
import android.util.Log;

import com.raggamuffin.protorunnerv2.particles.TrailPoint;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class GLModel_Trail
{
    private FloatBuffer m_FloatBuffer;

    private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_PositionHandle;
    private int m_ColourHandle;

    private ArrayList<Vector3> m_TrailPoints;

    private Vector3 m_EyePos;
    private Vector3 m_ToEye;

    public GLModel_Trail()
    {
        m_Program = 0;

        m_ProjMatrixHandle = 0;
        m_PositionHandle = 0;
        m_ColourHandle = 0;

        m_TrailPoints = new ArrayList<>();

        m_EyePos = new Vector3();
        m_ToEye = new Vector3();

        InitShaders();
    }

    public void InitialiseModel(float[] projMatrix, Vector3 eye)
    {
        GLES20.glUseProgram(m_Program);

        GLES20.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);
        GLES20.glLineWidth(100);
    }

    public void AddPoint(TrailPoint point)
    {
        m_TrailPoints.add(point.GetPosition());
    }

    public void Draw()
    {
        int numPoints = m_TrailPoints.size();
        float[] vertices = new float[numPoints * 3];

        for(int i = 0; i < numPoints; i++)
        {
            Vector3 pos     = m_TrailPoints.get(i);
            vertices[i*3]   = (float)pos.I;
            vertices[i*3+1] = (float)pos.J;
            vertices[i*3+2] = (float)pos.K;
        }

        ByteBuffer bb = ByteBuffer.allocateDirect(numPoints * 12);
        bb.order(ByteOrder.nativeOrder());
        m_FloatBuffer = bb.asFloatBuffer();
        m_FloatBuffer.put(vertices);
        m_FloatBuffer.position(0);

        GLES20.glEnableVertexAttribArray(m_PositionHandle);
        GLES20.glVertexAttribPointer(m_PositionHandle, 3, GLES20.GL_FLOAT, false, 12, m_FloatBuffer);

        Colour colour = new Colour(Colours.VioletRed);
        GLES20.glUniform4f(m_ColourHandle, (float) colour.Red, (float) colour.Green, (float) colour.Blue, (float) colour.Alpha);

        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, numPoints);

        m_TrailPoints.clear();
    }

    public void CleanModel()
    {
        GLES20.glDisableVertexAttribArray(m_PositionHandle);
    }

    private void InitShaders()
    {
        int vertexShaderHandler 	= loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_TRAIL);
        int fragmentShaderHandler 	= loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_STANDARD);

        m_Program = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle      = GLES20.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_PositionHandle        = GLES20.glGetAttribLocation(m_Program, "a_Position");
        m_ColourHandle 			= GLES20.glGetUniformLocation(m_Program, "u_Color");
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
            Log.e("shader trail", "Shits broken yo");
        }

        return shader;
    }
}
