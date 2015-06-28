package com.raggamuffin.protorunnerv2.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class GLScreenQuad 
{
	public final FloatBuffer vertexBuffer;
	public final FloatBuffer textureBuffer;

	private int m_ProgramHoriz;
	private int m_ProgramVert;
	private int m_ProgramNorm;

	private int m_PositionHandle;
	private int m_PositionHandleVert;
	private int m_PositionHandleHoriz;
	
	private int m_TexUniformHandleVert;
	private int m_TexUniformHandleHoriz;

    private int m_TexUniformHandleA;
    private int m_TexUniformHandleB;
    
    private int m_TexCoordHandle;
    private int m_TexCoordHandleVert;
    private int m_TexCoordHandleHoriz;
    
    private int m_GlowIntensityHorizHandle;
    private int m_GlowIntensityVertHandle;
    
    private float m_GlowIntensityHoriz;
    private float m_GlowIntensityVert;
    
	static final int COORDS_PER_VERTEX = 3;
	static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;	// 4 Bytes to a float.
	
	static float VertexCoords[] =
	{	
	   -1.0f,	 1.0f,	 0.0f, // A
	   -1.0f,	-1.0f,	 0.0f, // B
	    1.0f,	-1.0f,	 0.0f, // C
	   
	   -1.0f,	 1.0f,	 0.0f, // A
	    1.0f,	-1.0f,	 0.0f, // C
	    1.0f,	 1.0f,	 0.0f, // D	
	};
	
	static final int TEX_COORDS_PER_VERTEX = 2;
	static final int TEX_STRIDE = TEX_COORDS_PER_VERTEX * 4;	// 4 Bytes to a float.
	
	static float TextureCoords[] =
	{	
	    0.0f,	 1.0f, // A
	    0.0f,	 0.0f, // B
	    1.0f,	 0.0f, // C
	   
		
	    0.0f,	 1.0f, // A
	    1.0f,	 0.0f, // C
	    1.0f,	 1.0f, // D
	};

	private final int vertexCount = VertexCoords.length / COORDS_PER_VERTEX;
	
	public GLScreenQuad()
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

	    m_PositionHandle	  = 0;
	    m_PositionHandleVert  = 0;
		m_PositionHandleHoriz = 0;
		
		m_TexUniformHandleVert = 0;
		m_TexUniformHandleHoriz = 0;

	    m_TexUniformHandleA = 0;
	    m_TexUniformHandleB = 0;
	    
	    m_TexCoordHandle	  = 0;
	    m_TexCoordHandleVert  = 0;
	    m_TexCoordHandleHoriz = 0;
	    
	    m_GlowIntensityHorizHandle 	= 0;
	    m_GlowIntensityVertHandle 	= 0;
	    
	    m_GlowIntensityHoriz 	= 0.0f;
	    m_GlowIntensityVert 	= 0.0f;
	    
	    InitShaders();
	}
	
	public void draw(int mode)
	{
		switch(mode)
		{
			case ModelManager.HORIZ:

				GLES20.glUseProgram(m_ProgramHoriz);
				GLES20.glUniform1f(m_GlowIntensityHorizHandle, m_GlowIntensityHoriz);   
				
				GLES20.glEnableVertexAttribArray(m_PositionHandleHoriz);
				GLES20.glVertexAttribPointer(m_PositionHandleHoriz, GLScreenQuad.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, GLScreenQuad.VERTEX_STRIDE, vertexBuffer);

				GLES20.glEnableVertexAttribArray(m_TexCoordHandleHoriz);
				GLES20.glVertexAttribPointer(m_TexCoordHandleHoriz, TEX_COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, TEX_STRIDE, textureBuffer);
				
				GLES20.glUniform1i(m_TexUniformHandleHoriz, 0);
				
				break;
				
			case ModelManager.VERT:

				GLES20.glUseProgram(m_ProgramVert);
				GLES20.glUniform1f(m_GlowIntensityVertHandle,  m_GlowIntensityVert); 
				
				GLES20.glEnableVertexAttribArray(m_PositionHandleVert);
				GLES20.glVertexAttribPointer(m_PositionHandleVert, GLScreenQuad.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, GLScreenQuad.VERTEX_STRIDE, vertexBuffer);

				GLES20.glEnableVertexAttribArray(m_TexCoordHandleVert);
				GLES20.glVertexAttribPointer(m_TexCoordHandleVert, TEX_COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, TEX_STRIDE, textureBuffer);
				
				GLES20.glUniform1i(m_TexUniformHandleVert, 0);
				
				break;
				
			case ModelManager.NORM:

				GLES20.glUseProgram(m_ProgramNorm);
				GLES20.glEnableVertexAttribArray(m_PositionHandle);
				
				GLES20.glEnableVertexAttribArray(m_PositionHandle);
				GLES20.glVertexAttribPointer(m_PositionHandle, GLScreenQuad.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, GLScreenQuad.VERTEX_STRIDE, vertexBuffer);

				GLES20.glEnableVertexAttribArray(m_TexCoordHandle);
				GLES20.glVertexAttribPointer(m_TexCoordHandle, TEX_COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, TEX_STRIDE, textureBuffer);
				
				GLES20.glUniform1i(m_TexUniformHandleA, 0);
		        GLES20.glUniform1i(m_TexUniformHandleB, 1);

				break;
		}
       
    	GLES20.glUniform1f(m_GlowIntensityHorizHandle,m_GlowIntensityHoriz);
    	GLES20.glUniform1f(m_GlowIntensityVertHandle, m_GlowIntensityVert);
        
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
		
		GLES20.glDisableVertexAttribArray(m_PositionHandle);
	}

	public void InitShaders()
    {
		int vertexShaderHandler = 0;
		int fragmentShaderHandler = 0;

		// prepare shaders and OpenGL program
		///// HORIZ \\\\\
		vertexShaderHandler 	= loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_SCREENQUAD);
		fragmentShaderHandler 	= loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_BLURH);

		m_ProgramHoriz = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_ProgramHoriz, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_ProgramHoriz, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_ProgramHoriz);                  		// create OpenGL program executables

        m_TexUniformHandleHoriz		= GLES20.glGetUniformLocation(m_ProgramHoriz, "u_Texture");
        m_GlowIntensityHorizHandle  = GLES20.glGetUniformLocation(m_ProgramHoriz, "u_GlowIntensity");
        
        m_PositionHandleHoriz = GLES20.glGetAttribLocation(m_ProgramHoriz, "a_Position");
        m_TexCoordHandleHoriz = GLES20.glGetAttribLocation(m_ProgramHoriz, "a_TexCoord");

		///// VERT \\\\\
		vertexShaderHandler 	= loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_SCREENQUAD);
		fragmentShaderHandler 	= loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_BLURV);

		m_ProgramVert = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_ProgramVert, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_ProgramVert, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_ProgramVert);                  		// create OpenGL program executables

        m_TexUniformHandleVert			= GLES20.glGetUniformLocation(m_ProgramVert, "u_Texture");
        m_GlowIntensityVertHandle   = GLES20.glGetUniformLocation(m_ProgramVert, "u_GlowIntensity");
        
        m_PositionHandleVert = GLES20.glGetAttribLocation(m_ProgramVert, "a_Position");
        m_TexCoordHandleVert = GLES20.glGetAttribLocation(m_ProgramVert, "a_TexCoord");

		///// NORM \\\\\
		vertexShaderHandler 	= loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_SCREENQUAD);
		fragmentShaderHandler 	= loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_ADD);

		m_ProgramNorm = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_ProgramNorm, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_ProgramNorm, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_ProgramNorm);                  		// create OpenGL program executables

        m_TexUniformHandleA		= GLES20.glGetUniformLocation(m_ProgramNorm, "u_TextureA");
        m_TexUniformHandleB		= GLES20.glGetUniformLocation(m_ProgramNorm, "u_TextureB");

        m_PositionHandle = GLES20.glGetAttribLocation(m_ProgramNorm, "a_Position");
        m_TexCoordHandle = GLES20.glGetAttribLocation(m_ProgramNorm, "a_TexCoord");
    }
	
	public static int loadShader(int type, String shaderCode)
    {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
	
	public void SetHorizontalGlowIntensity(float Horiz)
	{
		m_GlowIntensityHoriz = Horiz;
	}
	
	public void SetVerticalGlowIntensity(float Vert)
	{
		m_GlowIntensityVert  = Vert;
	}
}
