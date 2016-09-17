package com.raggamuffin.protorunnerv2.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import android.opengl.GLES20;

public class GLModel_FloorPanel extends GLModel
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

    private float m_RepeatStride;

	static final int COORDS_PER_VERTEX = 3;
	static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;	// 4 Bytes to a float.
	static final float SIZE = 6.0f;

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
	static final float NUM_CELLS = 4.0f;

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

		m_Program 			= 0;
	    m_ProjMatrixHandle  = 0;
        m_WorldPosHandle    = 0;
	    m_ColourHandle		= 0;
	    m_PositionHandle	= 0;
	    m_TexUniformHandle	= 0;
	    m_TexCoordHandle	= 0;
	    m_TexOffsetHandle	= 0;

	    m_RepeatStride = (SIZE / NUM_CELLS) * 2.0f;

	    InitShaders();
	}

	public void InitShaders()
    {
		// prepare shaders and OpenGL program
        int vertexShaderHandler 	= loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_SCROLLTEX);
        int fragmentShaderHandler 	= loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_SCROLLTEX);

		m_Program = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle      = GLES20.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_WorldPosHandle        = GLES20.glGetUniformLocation(m_Program, "u_WorldPos");
        m_ColourHandle 			= GLES20.glGetUniformLocation(m_Program, "u_Color");
        m_TexUniformHandle		= GLES20.glGetUniformLocation(m_Program, "u_Texture");
        m_TexOffsetHandle		= GLES20.glGetUniformLocation(m_Program, "u_TexOffset");

        m_PositionHandle = GLES20.glGetAttribLocation(m_Program, "a_Position");
        m_TexCoordHandle = GLES20.glGetAttribLocation(m_Program, "a_TexCoord");
    }

    @Override
    public void InitialiseModel(float[] projMatrix, Vector3 eye)
    {
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisable(GLES20.GL_CULL_FACE);

        GLES20.glUseProgram(m_Program);

        GLES20.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);

        GLES20.glUniform1i(m_TexUniformHandle, 0);

        GLES20.glEnableVertexAttribArray(m_PositionHandle);
        GLES20.glVertexAttribPointer(m_PositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);

        GLES20.glEnableVertexAttribArray(m_TexCoordHandle);
        GLES20.glVertexAttribPointer(m_TexCoordHandle, TEX_COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, TEX_STRIDE, textureBuffer);
    }

    @Override
    public void Draw(GameObject obj)
    {
        Vector3 pos = obj.GetPosition();

        float offsetX = (float)pos.I / m_RepeatStride;
        float offsetY = (float)pos.K / m_RepeatStride;

        offsetX %= m_RepeatStride;
        offsetY %= m_RepeatStride;

        GLES20.glUniform4f(m_WorldPosHandle, (float)pos.I, (float)pos.J, (float)pos.K, 1.0f);
        Colour colour = obj.GetColour();
        GLES20.glUniform4f(m_ColourHandle, (float)colour.Red, (float)colour.Green, (float)colour.Blue, (float)colour.Alpha);
        GLES20.glUniform2f(m_TexOffsetHandle, offsetX, offsetY);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
    }

    @Override
    public void CleanModel()
    {
        GLES20.glDisableVertexAttribArray(m_PositionHandle);
        GLES20.glDisableVertexAttribArray(m_TexCoordHandle);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
    }

}
