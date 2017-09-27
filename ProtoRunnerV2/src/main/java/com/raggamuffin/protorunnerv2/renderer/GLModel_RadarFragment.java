package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES31;

import com.raggamuffin.protorunnerv2.RenderObjects.RenderObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLModel_RadarFragment extends GLModel
{
	public final FloatBuffer vertexBuffer;
	public final FloatBuffer textureBuffer;
	
	private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_WorldPosHandle;
    private int m_ScaleHandle;
    private int m_ColourHandle;
    private int m_PositionHandle;
    private int m_TexCoordHandle;
    private int m_TexOffsetHandle;
	
	static final int COORDS_PER_VERTEX = 3;
	static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;	// 4 Bytes to a float.
	
	static float VertexCoords[] =
	{	
		 // FRONT.
		-1.0f,	 1.0f,	 1.0f, // A
			1.0f,	-3.0f,	 1.0f, // C
		-1.0f,	-3.0f,	 1.0f, // B

		
		-1.0f,	 1.0f,	 1.0f, // A
			1.0f,	 1.0f,	 1.0f, // D
		 1.0f,	-3.0f,	 1.0f, // C

		 
		// BACK
		1.0f,	 1.0f,	-1.0f, // E
			-1.0f,	-3.0f,	-1.0f, // G
		1.0f,	-3.0f,	-1.0f, // F

		
		1.0f,	 1.0f,	-1.0f, // E
			-1.0f,	 1.0f,	-1.0f, // H
	   -1.0f,	-3.0f,	-1.0f, // G

		
		// LEFT.
	   -1.0f,	 1.0f,	-1.0f, // H
			-1.0f,	-3.0f,	 1.0f, // B
	   -1.0f,	-3.0f,	-1.0f, // G

		
	   -1.0f,	 1.0f,	-1.0f, // H
			-1.0f,	 1.0f,	 1.0f, // A
	   -1.0f,	-3.0f,	 1.0f, // B

		
		// RIGHT.
		1.0f,	 1.0f,	 1.0f, // D
			1.0f,	-2.0f,	-1.0f, // F
		1.0f,	-2.0f,	 1.0f, // C

		
		1.0f,	 1.0f,	 1.0f, // D
			1.0f,	 1.0f,	-1.0f, // E
		1.0f,	-2.0f,	-1.0f, // F

		
		// TOP.
	   -1.0f,	 1.0f,	-1.0f, // H
			1.0f,	 1.0f,	 1.0f, // D
	   -1.0f,	 1.0f,	 1.0f, // A

		
	   -1.0f,	 1.0f,	-1.0f, // H
			1.0f,	 1.0f,	-1.0f, // E
		1.0f,	 1.0f,	 1.0f, // D

	};
	
	static final int TEX_COORDS_PER_VERTEX = 2;
	static final int TEX_STRIDE = TEX_COORDS_PER_VERTEX * 4;	// 4 Bytes to a float.
	
	static float TextureCoords[] =
	{	
		// FRONT.
		0.0f,	0.0f, // A
			1.0f,	1.0f, // C
		0.0f,	1.0f, // B

		
		0.0f,	0.0f, // A
			1.0f,	0.0f, // D
		1.0f,	1.0f, // C

		 
		// BACK
		0.0f,	 0.0f, // E
			1.0f,	 1.0f, // G
		0.0f,	 1.0f, // F

		
	   	0.0f,	 0.0f, // E
			1.0f,	 0.0f, // H
	    1.0f,	 1.0f, // G

		
		// LEFT.
	    1.0f,	 0.0f, // H
			0.0f,	 1.0f, // B
	    1.0f,	 1.0f, // G

		
	    1.0f,	 0.0f, // H
			0.0f,	 0.0f, // A
	    0.0f,	 1.0f, // B

		
		// RIGHT.
	   	1.0f,	0.0f, // D
			0.0f,	1.0f, // F
		1.0f,	1.0f, // C

		
		1.0f,	0.0f, // D
			0.0f,	0.0f, // E
		0.0f,	1.0f, // F

		
		// TOP.
		1.0f,	0.0f, // H
			1.0f,	0.0f, // D
	    0.0f,	0.0f, // A

		
   		1.0f,	0.0f, // H
			0.0f,	0.0f, // E
	   	1.0f,	0.0f, // D

		
	};
	
	private final int vertexCount = VertexCoords.length / COORDS_PER_VERTEX;
	
	public GLModel_RadarFragment()
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

        m_Program 			= 0;
        m_ProjMatrixHandle  = 0;
        m_WorldPosHandle    = 0;
        m_ScaleHandle       = 0;
        m_ColourHandle		= 0;
        m_PositionHandle	= 0;
        m_TexCoordHandle	= 0;
        m_TexOffsetHandle	= 0;
	    
	    InitShaders();
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
        m_TexOffsetHandle		= GLES31.glGetUniformLocation(m_Program, "u_TexOffset");

        m_PositionHandle = GLES31.glGetAttribLocation(m_Program, "a_Position");
        m_TexCoordHandle = GLES31.glGetAttribLocation(m_Program, "a_TexCoord");
    }

    @Override
    public void InitialiseModel(float[] projMatrix, Vector3 eye)
    {
        GLES31.glUseProgram(m_Program);

        GLES31.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);

        GLES31.glUniform2f(m_TexOffsetHandle, 0.0f, 0.0f);

        GLES31.glEnableVertexAttribArray(m_PositionHandle);
        GLES31.glVertexAttribPointer(m_PositionHandle, GLModel_RadarFragment.COORDS_PER_VERTEX, GLES31.GL_FLOAT, false, GLModel_RadarFragment.VERTEX_STRIDE, vertexBuffer);

        GLES31.glEnableVertexAttribArray(m_TexCoordHandle);
        GLES31.glVertexAttribPointer(m_TexCoordHandle, TEX_COORDS_PER_VERTEX, GLES31.GL_FLOAT, false, TEX_STRIDE, textureBuffer);

	  //	GLES31.glDisable(GLES31.GL_DEPTH_TEST);
    }

    @Override
    public void Draw(RenderObject obj)
    {
        Vector3 pos = obj.GetPosition();
        GLES31.glUniform4f(m_WorldPosHandle, (float) pos.X, (float) pos.Y, (float) pos.Z, 1.0f);

        Vector3 scale = obj.GetScale();
        GLES31.glUniform3f(m_ScaleHandle, (float)scale.X, (float)scale.Y, (float)scale.Z);

        Colour colour = obj.GetColour();
       // GLES31.glUniform4f(m_ColourHandle, (float)colour.Red, (float)colour.Green, (float)colour.Blue, (float)colour.Alpha);

        GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, vertexCount);
    }

    @Override
    public void CleanModel()
    {
     //   GLES31.glEnable(GLES31.GL_DEPTH_TEST);

        GLES31.glDisableVertexAttribArray(m_PositionHandle);
        GLES31.glDisableVertexAttribArray(m_TexCoordHandle);
    }

}
