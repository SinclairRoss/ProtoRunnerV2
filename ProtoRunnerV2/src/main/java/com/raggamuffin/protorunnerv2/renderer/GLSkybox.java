package com.raggamuffin.protorunnerv2.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import android.opengl.GLES20;

public class GLSkybox extends GLModel
{
	public final FloatBuffer vertexBuffer;
	public final FloatBuffer textureBuffer;

	private int m_Program;
	
	private int m_ProjMatrixHandle;
    private int m_WorldPosHandle;
    private int m_ScaleHandle;
    private int m_PositionHandle;
    private int m_TexUniformHandle;
    private int m_TexCoordHandle;
    private int m_ColourHandle;
    
    private float[] m_Colour;
    
	static final int COORDS_PER_VERTEX = 3;
	static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;	// 4 Bytes to a float.
		
	static float VertexCoords[] =
	{	
		 // FRONT.
		-3.0f,	 3.0f,	 3.0f, // A
		 3.0f,	 3.0f,	 3.0f, // D
		 3.0f,	-3.0f,	 3.0f, // C
		
		-3.0f,	 3.0f,	 3.0f, // A
		 3.0f,	-3.0f,	 3.0f, // C
		-3.0f,	-3.0f,	 3.0f, // B
		 
		// BACK
		3.0f,	 3.0f,	-3.0f, // E
	   -3.0f,	 3.0f,	-3.0f, // H
	   -3.0f,	-3.0f,	-3.0f, // G
		
		3.0f,	 3.0f,	-3.0f, // E
	   -3.0f,	-3.0f,	-3.0f, // G
	    3.0f,	-3.0f,	-3.0f, // F
		
		// LEFT.
	   -3.0f,	 3.0f,	-3.0f, // H
	   -3.0f,	 3.0f,	 3.0f, // A
	   -3.0f,	-3.0f,	 3.0f, // B
		
	   -3.0f,	 3.0f,	-3.0f, // H
	   -3.0f,	-3.0f,	 3.0f, // B
	   -3.0f,	-3.0f,	-3.0f, // G
		
		// RIGHT.
		3.0f,	 3.0f,	 3.0f, // D
		3.0f,	 3.0f,	-3.0f, // E
		3.0f,	-3.0f,	-3.0f, // F
		
		3.0f,	 3.0f,	 3.0f, // D
		3.0f,	-3.0f,	-3.0f, // F
		3.0f,	-3.0f,	 3.0f, // C
		
		// TOP.
	   -3.0f,	 3.0f,	-3.0f, // H
	    3.0f,	 3.0f,	-3.0f, // E
		3.0f,	 3.0f,	 3.0f, // D
		
	   -3.0f,	 3.0f,	-3.0f, // H
		3.0f,	 3.0f,	 3.0f, // D
	   -3.0f,	 3.0f,	 3.0f, // A
		
		// BOTTOM.
	   -3.0f,	-3.0f,	 3.0f, // B
	    3.0f,	-3.0f,	 3.0f, // C
		3.0f,	-3.0f,	-3.0f, // F
		
	   -3.0f,	-3.0f,	 3.0f, // B
		3.0f,	-3.0f,	-3.0f, // F
	   -3.0f,	-3.0f,	-3.0f, // G
	};
	
	static final int TEX_COORDS_PER_VERTEX = 2;
	static final int TEX_STRIDE = TEX_COORDS_PER_VERTEX * 4;	// 4 Bytes to a float.
	
	static float TextureCoords[] =
	{	
		 // FRONT.
		 1.0f,	 0.0f, 	
		 0.0f,	 0.0f,	
		 0.0f,	 1.0f, 	
		
		 1.0f,	 0.0f, 	
		 0.0f,	 1.0f,	
		 1.0f,	 1.0f, 	
		 
		 // BACK
		 1.0f,	 0.0f, 	
		 0.0f,	 0.0f,	
		 0.0f,	 1.0f, 	
		
		 1.0f,	 0.0f, 	
		 0.0f,	 1.0f,	
		 1.0f,	 1.0f, 		
		
		 // LEFT.
		 1.0f,	 0.0f, 	
		 0.0f,	 0.0f,	
		 0.0f,	 1.0f, 	
		
		 1.0f,	 0.0f, 	
		 0.0f,	 1.0f,	
		 1.0f,	 1.0f, 
		
		 // RIGHT.
		 1.0f,	 0.0f, 	
		 0.0f,	 0.0f,	
		 0.0f,	 1.0f, 	
		
		 1.0f,	 0.0f, 	
		 0.0f,	 1.0f,	
		 1.0f,	 1.0f, 
		
		 // TOP.
		 0.0f,	 0.0f, 	
		 0.0f,	 0.0f,	
		 0.0f,	 0.0f, 	
		
		 0.0f,	 0.0f, 	
		 0.0f,	 0.0f,	
		 0.0f,	 0.0f, 
		
		 // BOTTOM.
		 0.0f,	 0.0f, 	
		 0.0f,	 0.0f,	
		 0.0f,	 0.0f, 	
		
		 0.0f,	 0.0f, 	
		 0.0f,	 0.0f,	
		 0.0f,	 0.0f, 		
		
	};
	
	private final int vertexCount = VertexCoords.length / COORDS_PER_VERTEX;

	public GLSkybox()
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
		
		m_Colour = new float[4];
		m_Colour[0] = 1.0f;
		m_Colour[1] = 1.0f;
		m_Colour[2] = 1.0f;
		m_Colour[3] = 1.0f;
		
		m_Program 			= 0;
	    m_ProjMatrixHandle = 0;
        m_WorldPosHandle = 0;
        m_ScaleHandle = 0;
	    m_PositionHandle	= 0;
	    m_TexUniformHandle	= 0;
	    m_TexCoordHandle	= 0;
	    m_ColourHandle		= 0;
	    
	    InitShaders();
	}
	
	public void SetColour(Colour colour)
	{
		m_Colour[0] = (float)colour.Red;
		m_Colour[1] = (float)colour.Green;
		m_Colour[2] = (float)colour.Blue;
		m_Colour[3] = (float)colour.Alpha;
	}
	
	public void draw(Vector3 pos, float[] projMatrix)
	{
		GLES20.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);
        GLES20.glUniform4f(m_WorldPosHandle, (float) pos.I, (float) pos.J, (float) pos.K, 1.0f);
        GLES20.glUniform3f(m_ScaleHandle, 1.0f, 1.0f, 1.0f);
        GLES20.glUniform4fv(m_ColourHandle, 1, m_Colour, 0);

        GLES20.glUniform1i(m_TexUniformHandle, 0);
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
	}
	
	public void InitShaders()
    {
		// prepare shaders and OpenGL program
        int vertexShaderHandler 	= loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_TEXTURED);
        int fragmentShaderHandler 	= loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_TEXTURED);

		m_Program = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle = GLES20.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_WorldPosHandle   = GLES20.glGetUniformLocation(m_Program, "u_WorldPos");
        m_ScaleHandle      = GLES20.glGetUniformLocation(m_Program, "u_Scale");

        m_ColourHandle 			= GLES20.glGetUniformLocation(m_Program, "u_Color");
        m_TexUniformHandle		= GLES20.glGetUniformLocation(m_Program, "u_Texture");

        m_PositionHandle = GLES20.glGetAttribLocation(m_Program, "a_Position");
        m_TexCoordHandle = GLES20.glGetAttribLocation(m_Program, "a_TexCoord");
    }

    @Override
    public void InitialiseModel(float[] projMatrix)
    {
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);

        GLES20.glUseProgram(m_Program);

        GLES20.glEnableVertexAttribArray(m_PositionHandle);
        GLES20.glVertexAttribPointer(m_PositionHandle, GLSkybox.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, GLSkybox.VERTEX_STRIDE, vertexBuffer);

        GLES20.glEnableVertexAttribArray(m_TexCoordHandle);
        GLES20.glVertexAttribPointer(m_TexCoordHandle, TEX_COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, TEX_STRIDE, textureBuffer);
    }

    @Override
    public void Draw(GameObject obj)
    {

    }

    @Override
    public void CleanModel()
    {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        GLES20.glDisableVertexAttribArray(m_PositionHandle);
        GLES20.glDisableVertexAttribArray(m_TexCoordHandle);
    }
}
