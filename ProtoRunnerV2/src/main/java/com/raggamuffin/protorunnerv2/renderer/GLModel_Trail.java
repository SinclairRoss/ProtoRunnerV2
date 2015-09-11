package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES20;
import android.util.Log;

import com.raggamuffin.protorunnerv2.particles.TrailPoint;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class GLModel_Trail
{
    private FloatBuffer m_VertexBuffer;
    private FloatBuffer m_ColourBuffer;

    private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_PositionHandle;
    private int m_ColourHandle;

    private ArrayList<Vector3> m_TrailPoints;
    private ArrayList<Colour> m_Colours;

    private Vector3 m_EyePos;
    private Vector3 m_ToEye;

    public GLModel_Trail()
    {
        m_Program = 0;

        m_ProjMatrixHandle = 0;
        m_PositionHandle = 0;
        m_ColourHandle = 0;

        m_TrailPoints = new ArrayList<>();
        m_Colours = new ArrayList<>();

        m_EyePos = new Vector3();
        m_ToEye = new Vector3();

        InitShaders();
    }

    public void InitialiseModel(float[] projMatrix, Vector3 eye)
    {
        GLES20.glUseProgram(m_Program);

        GLES20.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);
        m_EyePos.SetVector(eye);

        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
    }

    public void AddPoint(TrailPoint point)
    {
        m_TrailPoints.add(point.GetPosition());
        m_Colours.add(point.GetColour());
    }

    public void Draw()
    {
        m_ToEye.SetVectorDifference(m_EyePos, m_TrailPoints.get(0));
        float dist = (float) m_ToEye.GetLength();

        GLES20.glLineWidth((float) (20 * MathsHelper.FastInverseSqrt(dist)));

        int numPoints = m_TrailPoints.size();
        float[] vertices = new float[numPoints * 3];

        for(int i = 0; i < numPoints; i++)
        {
            Vector3 pos     = m_TrailPoints.get(i);
            vertices[i*3]   = (float)pos.I;
            vertices[i*3+1] = (float)pos.J;
            vertices[i*3+2] = (float)pos.K;
        }

        ByteBuffer vb = ByteBuffer.allocateDirect(numPoints * 12);
        vb.order(ByteOrder.nativeOrder());
        m_VertexBuffer = vb.asFloatBuffer();
        m_VertexBuffer.put(vertices);
        m_VertexBuffer.position(0);

        GLES20.glEnableVertexAttribArray(m_PositionHandle);
        GLES20.glVertexAttribPointer(m_PositionHandle, 3, GLES20.GL_FLOAT, false, 12, m_VertexBuffer);

        float[] colours = new float[numPoints * 4];

        for(int i = 0; i < numPoints; i++)
        {
            Colour colour   = m_Colours.get(i);
            colours[i*4]   = (float)colour.Red;
            colours[i*4+1] = (float)colour.Green;
            colours[i*4+2] = (float)colour.Blue;
            colours[i*4+3] = (float)colour.Alpha;
        }

        ByteBuffer cb = ByteBuffer.allocateDirect(numPoints * 16);
        cb.order(ByteOrder.nativeOrder());
        m_ColourBuffer = cb.asFloatBuffer();
        m_ColourBuffer.put(colours);
        m_ColourBuffer.position(0);

        GLES20.glEnableVertexAttribArray(m_ColourHandle);
        GLES20.glVertexAttribPointer(m_ColourHandle, 4, GLES20.GL_FLOAT, false, 16, m_ColourBuffer);

        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, numPoints);

        m_TrailPoints.clear();
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

        Log.e("shader trail", "Shader A-OK.");

        return shader;
    }
}
