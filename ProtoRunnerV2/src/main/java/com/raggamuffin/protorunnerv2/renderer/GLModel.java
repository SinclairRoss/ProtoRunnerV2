package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES20;
import android.util.Log;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class GLModel
{
    public abstract void InitialiseModel(float[] projMatrix, Vector3 eye);
    public abstract void Draw(GameObject obj);
    public abstract void CleanModel();

    protected abstract void InitShaders();

    protected int loadShader(int type, String shaderCode)
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
            Log.e("Shader", "Shader is broken");
        }

        return shader;
    }
}
