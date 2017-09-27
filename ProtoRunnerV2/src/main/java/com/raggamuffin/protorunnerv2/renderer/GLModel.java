package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES31;
import android.util.Log;

import com.raggamuffin.protorunnerv2.RenderObjects.RenderObject;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class GLModel
{
    public abstract void InitialiseModel(float[] projMatrix, Vector3 eye);
   // public abstract void DrawVehicles(GameObject obj);
    public abstract void Draw(RenderObject obj);
    public abstract void CleanModel();

    protected abstract void InitShaders();

    protected int loadShader(int type, String shaderCode)
    {
        return loadShader(type, shaderCode, "");
    }

    protected int loadShader(int type, String shaderCode, String tag)
    {
        // create a vertex shader type (GLES31.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES31.GL_FRAGMENT_SHADER)
        int shader = GLES31.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES31.glShaderSource(shader, shaderCode);
        GLES31.glCompileShader(shader);

        int[] compiled = new int[1];
        GLES31.glGetShaderiv(shader, GLES31.GL_COMPILE_STATUS, compiled, 0);

        if (compiled[0] == 0)
        {
            Log.e("Shader", tag + ": Shader is broken");
        }

        return shader;
    }
}
