package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES31;

import com.raggamuffin.protorunnerv2.utils.MathsHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLScreenQuad
{
	public final FloatBuffer vertexBuffer;
	public final FloatBuffer textureBuffer;

	private int m_ProgramHoriz;
	private int m_ProgramVert;
	private int m_ProgramFilmGrain;
	private int m_ProgramNorm;

	private int m_PositionHandle;
	private int m_PositionHandleVert;
	private int m_PositionHandleHoriz;
	private int m_PositionHandleGrain;

	private int m_TexUniformHandleVert;
	private int m_TexUniformHandleHoriz;

	private int m_TexUniformHandleA;
	private int m_TexUniformHandleB;
	private int m_TexUnifromHandleC;

	private int m_RandomOffsetHandle;
	private int m_FilmGrainIntensityHandle;

	private int m_TexCoordHandle;
	private int m_TexCoordHandleVert;
	private int m_TexCoordHandleHoriz;

	private int m_GlowIntensityHorizHandle;
	private int m_GlowIntensityVertHandle;

	private float m_GlowIntensityHoriz;
	private float m_GlowIntensityVert;
	private float m_FilmGrainIntensity;

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
		m_PositionHandleGrain = 0;

		m_TexUniformHandleVert = 0;
		m_TexUniformHandleHoriz = 0;

		m_TexUniformHandleA = 0;
		m_TexUniformHandleB = 0;
		m_TexUnifromHandleC = 0;

		m_TexCoordHandle	  = 0;
		m_TexCoordHandleVert  = 0;
		m_TexCoordHandleHoriz = 0;

		m_GlowIntensityHorizHandle 	= 0;
		m_GlowIntensityVertHandle 	= 0;

		m_GlowIntensityHoriz 	= 0.0f;
		m_GlowIntensityVert 	= 0.0f;
		m_FilmGrainIntensity    = 0.0f;

		InitShaders();
	}

	public void draw(int mode)
	{
		switch(mode)
		{
			case ModelManager.HORIZ:
			{
				GLES31.glUseProgram(m_ProgramHoriz);
				GLES31.glUniform1f(m_GlowIntensityHorizHandle, m_GlowIntensityHoriz);

				GLES31.glEnableVertexAttribArray(m_PositionHandleHoriz);
				GLES31.glVertexAttribPointer(m_PositionHandleHoriz, GLScreenQuad.COORDS_PER_VERTEX, GLES31.GL_FLOAT, false, GLScreenQuad.VERTEX_STRIDE, vertexBuffer);

				GLES31.glEnableVertexAttribArray(m_TexCoordHandleHoriz);
				GLES31.glVertexAttribPointer(m_TexCoordHandleHoriz, TEX_COORDS_PER_VERTEX, GLES31.GL_FLOAT, false, TEX_STRIDE, textureBuffer);

				GLES31.glUniform1f(m_GlowIntensityHorizHandle, m_GlowIntensityHoriz);

				GLES31.glUniform1i(m_TexUniformHandleHoriz, 0);

				break;
			}
			case ModelManager.VERT:
			{
				GLES31.glUseProgram(m_ProgramVert);
				GLES31.glUniform1f(m_GlowIntensityVertHandle, m_GlowIntensityVert);

				GLES31.glEnableVertexAttribArray(m_PositionHandleVert);
				GLES31.glVertexAttribPointer(m_PositionHandleVert, GLScreenQuad.COORDS_PER_VERTEX, GLES31.GL_FLOAT, false, GLScreenQuad.VERTEX_STRIDE, vertexBuffer);

				GLES31.glEnableVertexAttribArray(m_TexCoordHandleVert);
				GLES31.glVertexAttribPointer(m_TexCoordHandleVert, TEX_COORDS_PER_VERTEX, GLES31.GL_FLOAT, false, TEX_STRIDE, textureBuffer);

				GLES31.glUniform1f(m_GlowIntensityVertHandle, m_GlowIntensityVert);

				GLES31.glUniform1i(m_TexUniformHandleVert, 0);

				break;
			}
			case ModelManager.GRAIN:
			{
				GLES31.glUseProgram(m_ProgramFilmGrain);

				GLES31.glEnableVertexAttribArray(m_PositionHandleGrain);
				GLES31.glVertexAttribPointer(m_PositionHandleGrain, GLScreenQuad.COORDS_PER_VERTEX, GLES31.GL_FLOAT, false, GLScreenQuad.VERTEX_STRIDE, vertexBuffer);

				GLES31.glEnableVertexAttribArray(m_TexCoordHandleVert);
				GLES31.glVertexAttribPointer(m_TexCoordHandleVert, TEX_COORDS_PER_VERTEX, GLES31.GL_FLOAT, false, TEX_STRIDE, textureBuffer);

				GLES31.glUniform2f(m_RandomOffsetHandle, MathsHelper.RandomFloat(0, 1), MathsHelper.RandomFloat(0, 1));
				GLES31.glUniform1f(m_FilmGrainIntensityHandle, m_FilmGrainIntensity);

				break;
			}
			case ModelManager.NORM:
			{
				GLES31.glUseProgram(m_ProgramNorm);
				GLES31.glEnableVertexAttribArray(m_PositionHandle);

				GLES31.glEnableVertexAttribArray(m_PositionHandle);
				GLES31.glVertexAttribPointer(m_PositionHandle, GLScreenQuad.COORDS_PER_VERTEX, GLES31.GL_FLOAT, false, GLScreenQuad.VERTEX_STRIDE, vertexBuffer);

				GLES31.glEnableVertexAttribArray(m_TexCoordHandle);
				GLES31.glVertexAttribPointer(m_TexCoordHandle, TEX_COORDS_PER_VERTEX, GLES31.GL_FLOAT, false, TEX_STRIDE, textureBuffer);

				GLES31.glUniform1i(m_TexUniformHandleA, 0);
				GLES31.glUniform1i(m_TexUniformHandleB, 1);
				GLES31.glUniform1i(m_TexUnifromHandleC, 2);

				break;
			}
		}

		GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, vertexCount);
		GLES31.glDisableVertexAttribArray(m_PositionHandle);
	}

	public void InitShaders()
	{
		int vertexShaderHandler;
		int fragmentShaderHandler;

		// prepare shaders and OpenGL program
		///// HORIZ \\\\\
		vertexShaderHandler = loadShader(GLES31.GL_VERTEX_SHADER, Shaders.vertexShader_SCREENQUAD);
		fragmentShaderHandler = loadShader(GLES31.GL_FRAGMENT_SHADER, Shaders.fragmentShader_BLURH);

		m_ProgramHoriz = GLES31.glCreateProgram();             		// create empty OpenGL Program
		GLES31.glAttachShader(m_ProgramHoriz, vertexShaderHandler);   // add the vertex shader to program
		GLES31.glAttachShader(m_ProgramHoriz, fragmentShaderHandler); // add the fragment shader to program
		GLES31.glLinkProgram(m_ProgramHoriz);                  		// create OpenGL program executables

		m_TexUniformHandleHoriz		= GLES31.glGetUniformLocation(m_ProgramHoriz, "u_Texture");
		m_GlowIntensityHorizHandle  = GLES31.glGetUniformLocation(m_ProgramHoriz, "u_GlowIntensity");

		m_PositionHandleHoriz = GLES31.glGetAttribLocation(m_ProgramHoriz, "a_Position");
		m_TexCoordHandleHoriz = GLES31.glGetAttribLocation(m_ProgramHoriz, "a_TexCoord");

		///// VERT \\\\\
		vertexShaderHandler = loadShader(GLES31.GL_VERTEX_SHADER, Shaders.vertexShader_SCREENQUAD);
		fragmentShaderHandler = loadShader(GLES31.GL_FRAGMENT_SHADER, Shaders.fragmentShader_BLURV);

		m_ProgramVert = GLES31.glCreateProgram();             		// create empty OpenGL Program
		GLES31.glAttachShader(m_ProgramVert, vertexShaderHandler);   // add the vertex shader to program
		GLES31.glAttachShader(m_ProgramVert, fragmentShaderHandler); // add the fragment shader to program
		GLES31.glLinkProgram(m_ProgramVert);                  		// create OpenGL program executables

		m_TexUniformHandleVert		= GLES31.glGetUniformLocation(m_ProgramVert, "u_Texture");
		m_GlowIntensityVertHandle   = GLES31.glGetUniformLocation(m_ProgramVert, "u_GlowIntensity");

		m_PositionHandleVert = GLES31.glGetAttribLocation(m_ProgramVert, "a_Position");
		m_TexCoordHandleVert = GLES31.glGetAttribLocation(m_ProgramVert, "a_TexCoord");

		///// GRAIN \\\\\
		vertexShaderHandler = loadShader(GLES31.GL_VERTEX_SHADER, Shaders.vertexShader_SCREENQUAD);
		fragmentShaderHandler = loadShader(GLES31.GL_FRAGMENT_SHADER, Shaders.fragmentShader_FILMGRAIN);

		m_ProgramFilmGrain = GLES31.glCreateProgram();
		GLES31.glAttachShader(m_ProgramFilmGrain, vertexShaderHandler);
		GLES31.glAttachShader(m_ProgramFilmGrain, fragmentShaderHandler);
		GLES31.glLinkProgram(m_ProgramFilmGrain);

		m_PositionHandleGrain = GLES31.glGetAttribLocation(m_ProgramFilmGrain, "a_Position");
		m_RandomOffsetHandle = GLES31.glGetUniformLocation(m_ProgramFilmGrain, "u_RandomOffset");
		m_FilmGrainIntensityHandle = GLES31.glGetUniformLocation(m_ProgramFilmGrain, "u_Intensity");

		///// NORM \\\\\
		vertexShaderHandler 	= loadShader(GLES31.GL_VERTEX_SHADER,Shaders.vertexShader_SCREENQUAD);
		fragmentShaderHandler 	= loadShader(GLES31.GL_FRAGMENT_SHADER,Shaders.fragmentShader_ADD);

		m_ProgramNorm = GLES31.glCreateProgram();             		// create empty OpenGL Program
		GLES31.glAttachShader(m_ProgramNorm, vertexShaderHandler);   // add the vertex shader to program
		GLES31.glAttachShader(m_ProgramNorm, fragmentShaderHandler); // add the fragment shader to program
		GLES31.glLinkProgram(m_ProgramNorm);                  		// create OpenGL program executables

		m_TexUniformHandleA		= GLES31.glGetUniformLocation(m_ProgramNorm, "u_TextureA");
		m_TexUniformHandleB		= GLES31.glGetUniformLocation(m_ProgramNorm, "u_TextureB");

		m_PositionHandle = GLES31.glGetAttribLocation(m_ProgramNorm, "a_Position");
		m_TexCoordHandle = GLES31.glGetAttribLocation(m_ProgramNorm, "a_TexCoord");
	}

	public static int loadShader(int type, String shaderCode)
	{
		// create a vertex shader type (GLES31.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES31.GL_FRAGMENT_SHADER)
		int shader = GLES31.glCreateShader(type);

		// add the source code to the shader and compile it
		GLES31.glShaderSource(shader, shaderCode);
		GLES31.glCompileShader(shader);

		return shader;
	}

	public void SetHorizontalGlowIntensity(float horiz)
	{
		m_GlowIntensityHoriz = horiz;
	}

	public void SetVerticalGlowIntensity(float vert)
	{
		m_GlowIntensityVert  = vert;
	}

	public void SetFilmGrainIntensity(float grain)
	{
		m_FilmGrainIntensity = grain;
	}
}
