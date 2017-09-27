package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES31;

import com.raggamuffin.protorunnerv2.RenderObjects.RenderObject;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLTextQuad extends GLModel
{
    public final FloatBuffer vertexBuffer;
    public final FloatBuffer textureBuffer;

    private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_WorldPosHandle;
    private int m_ScaleHandle;
    private int m_ColourHandle;
    private int m_PositionHandle;
    private int m_TexUniformHandle;
    private int m_TexCoordHandle;
    private int m_TexOffsetHandle;

    private float[] m_Offset;

    static final int COORDS_PER_VERTEX = 3;

    // Origin is the bottom left.
    static float VertexCoords[] =
    {
            // BOTTOM.
            0.0f,	 0.5f,	 0.0f, // A
            0.0f,	-0.5f,	 0.0f, // B
            1.0f,	-0.5f,	 0.0f, // C

            0.0f,	 0.5f,	 0.0f, // A
            1.0f,	-0.5f,	 0.0f, // C
            1.0f,	 0.5f,	 0.0f // D
    };

    private final int vertexCount = VertexCoords.length / COORDS_PER_VERTEX;

    // 0.0625 is the size of one cell in the text sprite sheet.
    static float TextureCoords[] =
    {
            0.0f,	 0.0f, 		// A
            0.0f,	 0.0625f, 	// B
            0.0625f, 0.0625f, 	// C

            0.0f,	  0.0f, 	// A
            0.0625f,  0.0625f,  // C
            0.0625f,  0.0f 		// D
    };

    public GLTextQuad()
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(VertexCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(VertexCoords);
        vertexBuffer.position(0);

        ByteBuffer tc = ByteBuffer.allocateDirect(TextureCoords.length * 4);
        tc.order(ByteOrder.nativeOrder());
        textureBuffer = tc.asFloatBuffer();
        textureBuffer.put(TextureCoords);
        textureBuffer.position(0);

        m_Offset = new float[2];
        m_Offset[0] = 0.0f;
        m_Offset[1] = 0.0f;

        m_Program = 0;
        m_ProjMatrixHandle = 0;
        m_WorldPosHandle = 0;
        m_ScaleHandle = 0;
        m_ColourHandle = 0;
        m_PositionHandle = 0;
        m_TexUniformHandle = 0;
        m_TexCoordHandle = 0;
        m_TexOffsetHandle = 0;

        InitShaders();
    }

    public void SetOffset(float[] offset)
    {
        m_Offset[0] = offset[0];
        m_Offset[1] = offset[1];
    }

    public void draw(float x, float y, float scale, Colour colour)
    {
        GLES31.glUniform4f(m_WorldPosHandle, x, y, 0.0f, 1.0f);
        GLES31.glUniform3f(m_ScaleHandle, scale, scale, 1.0f);
       // GLES31.glUniform4f(m_ColourHandle, (float)colour.Red, (float)colour.Green, (float)colour.Blue, (float)colour.Alpha);
        GLES31.glUniform2f(m_TexOffsetHandle, m_Offset[0], m_Offset[1]);

        GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, vertexCount);
    }

    public void InitShaders()
    {
        // prepare shaders and OpenGL program
        int vertexShaderHandler 	= loadShader(GLES31.GL_VERTEX_SHADER,Shaders.vertexShader_TEXTURED);
        int fragmentShaderHandler 	= loadShader(GLES31.GL_FRAGMENT_SHADER,Shaders.fragmentShader_TEXTURED);

        m_Program = GLES31.glCreateProgram();             		// create empty OpenGL Program
        GLES31.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES31.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES31.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle      = GLES31.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_WorldPosHandle        = GLES31.glGetUniformLocation(m_Program, "u_WorldPos");
        m_ScaleHandle           = GLES31.glGetUniformLocation(m_Program, "u_Scale");

        m_ColourHandle 			= GLES31.glGetUniformLocation(m_Program, "u_Color");
        m_TexUniformHandle		= GLES31.glGetUniformLocation(m_Program, "u_Texture");
        m_TexOffsetHandle		= GLES31.glGetUniformLocation(m_Program, "u_TexOffset");

        m_PositionHandle = GLES31.glGetAttribLocation(m_Program, "a_Position");
        m_TexCoordHandle = GLES31.glGetAttribLocation(m_Program, "a_TexCoord");
    }

    @Override
    public void InitialiseModel(float[] projMatrix, Vector3 eye)
    {
        GLES31.glUseProgram(m_Program);

        GLES31.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);

        GLES31.glEnableVertexAttribArray(m_PositionHandle);
        GLES31.glVertexAttribPointer(m_PositionHandle, 3, GLES31.GL_FLOAT, false, 12, vertexBuffer);

        GLES31.glEnableVertexAttribArray(m_TexCoordHandle);
        GLES31.glVertexAttribPointer(m_TexCoordHandle, 2, GLES31.GL_FLOAT, false, 8, textureBuffer);

        GLES31.glUniform1i(m_TexUniformHandle, 0);
    }

    @Override
    public void Draw(RenderObject obj)
    {}

    @Override
    public void CleanModel()
    {
        GLES31.glDisableVertexAttribArray(m_PositionHandle);
        GLES31.glDisableVertexAttribArray(m_TexCoordHandle);
    }
}