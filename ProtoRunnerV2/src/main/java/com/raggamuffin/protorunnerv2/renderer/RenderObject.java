package com.raggamuffin.protorunnerv2.renderer;

// Author: Sinclair Ross
// Date:   13/08/2016

import android.opengl.GLES20;
import android.util.Log;

public abstract class RenderObject
{
    protected final int m_Program;
    private ModelType m_ModelType;

    public RenderObject(String vertexShader, String fragmentShader, ModelType type)
    {
        m_Program = CreateProgram(vertexShader, fragmentShader);
        m_ModelType = type;
    }

    public abstract void InitialiseModel(float[] projMatrix);
    public abstract void Draw();
    public abstract void CleanModel();

    private int CreateProgram(String vertexShaderCode, String fragmentShaderCode)
    {
        int vertexShaderHandle = LoadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShaderHandle = LoadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        int program = GLES20.glCreateProgram();             	// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandle);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandle); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);                  		// create OpenGL program executables

        return program;
    }

    private int LoadShader(int type, String shaderCode)
    {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);

        if (compiled[0] == 0)
        {
            Log.e("Shader", "Shader is broken.");
        }
        else
        {
            Log.e("Shader", "Shader compiled correctly.");
        }

        return shader;
    }

    public ModelType GetModelType()
    {
        return m_ModelType;
    }
}


