package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES20;

public abstract class GLModel
{
    public abstract void InitialiseModel();
    public abstract void CleanModel();

    public abstract int GetVertexCount();
    public abstract void Draw(float[] projMatrix);

    protected int loadShader(int type, String shaderCode)
    {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}