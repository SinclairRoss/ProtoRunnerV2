package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES31;
import android.util.Log;

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLModel_FloorPanel
{
    public final FloatBuffer vertexBuffer;
    public final FloatBuffer textureBuffer;

    private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_WorldPosHandle;
    private int m_ColourHandle;
    private int m_PositionHandle;
    private int m_TexUniformHandle;
    private int m_TexCoordHandle;
    private int m_TexOffsetHandle;
    private int m_AttenuationCoefficientHandle;

    private float m_RepeatStride;

    static final int COORDS_PER_VERTEX = 3;
    static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;	// 4 Bytes to a float.
    static final float SIZE = 15.0f;

    static float VertexCoords[] =
    {
            // BOTTOM.
            -SIZE,	0.0f,	 SIZE, // A
            SIZE,	0.0f,	-SIZE, // C
            -SIZE,	0.0f,	-SIZE, // B

            -SIZE,	0.0f,	 SIZE, // A
            SIZE,	0.0f,	 SIZE, // D
            SIZE,	0.0f,	-SIZE, // C
    };

    static final int TEX_COORDS_PER_VERTEX = 2;
    static final int TEX_STRIDE = TEX_COORDS_PER_VERTEX * 4;	// 4 Bytes to a float.
    static final float NUM_CELLS = 10.0f;

    static float TextureCoords[] =
    {
            // BOTTOM.
            0.0f,		NUM_CELLS, // A
            NUM_CELLS,	0.0f, // C
            0.0f,		0.0f, // B

            0.0f,		NUM_CELLS, // A
            NUM_CELLS,	NUM_CELLS, // D
            NUM_CELLS,	0.0f, // C
    };

    private final int vertexCount = VertexCoords.length / COORDS_PER_VERTEX;

    public GLModel_FloorPanel()
    {
        ByteBuffer vc = ByteBuffer.allocateDirect(VertexCoords.length * 4);
        vc.order(ByteOrder.nativeOrder());
        vertexBuffer = vc.asFloatBuffer();
        vertexBuffer.put(VertexCoords);
        vertexBuffer.position(0);

        ByteBuffer tc = ByteBuffer.allocateDirect(TextureCoords.length * 4);
        tc.order(ByteOrder.nativeOrder());
        textureBuffer = tc.asFloatBuffer();
        textureBuffer.put(TextureCoords);
        textureBuffer.position(0);

        m_Program = 0;
        m_ProjMatrixHandle = 0;
        m_WorldPosHandle = 0;
        m_ColourHandle = 0;
        m_PositionHandle = 0;
        m_TexUniformHandle = 0;
        m_TexCoordHandle = 0;
        m_TexOffsetHandle = 0;
        m_AttenuationCoefficientHandle = 0;

        m_RepeatStride = (SIZE / NUM_CELLS) * 2.0f;

        InitShaders();
    }

    public void InitShaders()
    {
        // prepare shaders and OpenGL program
        int vertexShaderHandler	= LoadShader(GLES31.GL_VERTEX_SHADER,Shaders.vertexShader_SCROLLTEX);
        int fragmentShaderHandler = LoadShader(GLES31.GL_FRAGMENT_SHADER,Shaders.fragmentShader_SCROLLTEX);

        m_Program = GLES31.glCreateProgram();             		// create empty OpenGL Program
        GLES31.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES31.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES31.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle = GLES31.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_WorldPosHandle = GLES31.glGetUniformLocation(m_Program, "u_WorldPos");
        m_ColourHandle = GLES31.glGetUniformLocation(m_Program, "u_Color");
        m_TexUniformHandle = GLES31.glGetUniformLocation(m_Program, "u_Texture");
        m_TexOffsetHandle = GLES31.glGetUniformLocation(m_Program, "u_TexOffset");
        m_AttenuationCoefficientHandle = GLES31.glGetUniformLocation(m_Program, "u_AttenuationCoefficient");

        m_PositionHandle = GLES31.glGetAttribLocation(m_Program, "a_Position");
        m_TexCoordHandle = GLES31.glGetAttribLocation(m_Program, "a_TexCoord");
    }

    public void InitialiseModel(float[] projMatrix)
    {
        GLES31.glDisable(GLES31.GL_DEPTH_TEST);
        GLES31.glDisable(GLES31.GL_CULL_FACE);

        GLES31.glUseProgram(m_Program);

        GLES31.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);

        GLES31.glUniform1i(m_TexUniformHandle, 0);

        GLES31.glEnableVertexAttribArray(m_PositionHandle);
        GLES31.glVertexAttribPointer(m_PositionHandle, COORDS_PER_VERTEX, GLES31.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);

        GLES31.glEnableVertexAttribArray(m_TexCoordHandle);
        GLES31.glVertexAttribPointer(m_TexCoordHandle, TEX_COORDS_PER_VERTEX, GLES31.GL_FLOAT, false, TEX_STRIDE, textureBuffer);
    }

    public void Draw(Vector3 pos, Colour colour, double attenuation)
    {
      // GLES31.glUniform4f(m_WorldPosHandle, (float)pos.X, (float)pos.Y, (float)pos.Z, 1.0f);
      // GLES31.glUniform4f(m_ColourHandle, (float)colour.Red, (float)colour.Green, (float)colour.Blue, (float)colour.Alpha);
      // GLES31.glUniform1f(m_AttenuationCoefficientHandle, (float)attenuation);

        double offsetX = (pos.X / m_RepeatStride) % m_RepeatStride;
        double offsetY = (pos.Z / m_RepeatStride) % m_RepeatStride;
        GLES31.glUniform2f(m_TexOffsetHandle, (float)offsetX, (float)offsetY);

        GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, vertexCount);
    }

    public void CleanModel()
    {
        GLES31.glDisableVertexAttribArray(m_PositionHandle);
        GLES31.glDisableVertexAttribArray(m_TexCoordHandle);

        GLES31.glEnable(GLES31.GL_DEPTH_TEST);
        GLES31.glEnable(GLES31.GL_CULL_FACE);
    }

    private int LoadShader(int type, String shaderCode)
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
            Log.e("Shader", "Shader is broken");
        }

        return shader;
    }
}
