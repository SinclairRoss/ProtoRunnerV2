package com.raggamuffin.protorunnerv2.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import android.opengl.GLES20;
import android.util.Log;

public class GLExplosion extends GLModel
{
	public final FloatBuffer vertexBuffer;
	public final FloatBuffer barycentricCoordBuffer;
	
	private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_WorldPosHandle;
    private int m_YawHandle;
    private int m_RollHandle;
    private int m_ForwardHandle;
    private int m_ColourHandle;
    private int m_PositionHandle;
    private int m_BarycentricHandle;
    
    private float[] m_Colour = new float[4];
	
	static final int COORDS_PER_VERTEX = 3;
	static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;	// 4 Bytes to a float.

	static float VertexCoords[] =
	{	
		 0.0f, 1.0f, 0.0f, // A
		 0.219f, 0.707f, 0.672f, // C
		 0.707f, 0.707f, 0.0f, // B
		 
		 0.0f, 1.0f, 0.0f, // A
		 -0.572f, 0.707f, 0.416f, // D
		 0.219f, 0.707f, 0.672f, // C
		 
		 0.0f, 1.0f, 0.0f, // A
		 -0.572f, 0.707f, -0.416f, // E
		 -0.572f, 0.707f, 0.416f, // D
		 
		 0.0f, 1.0f, 0.0f, // A
		 0.219f, 0.707f, -0.672f, // F
		 -0.572f, 0.707f, -0.416f, // E
		 
		 0.0f, 1.0f, 0.0f, // A
		 0.707f, 0.707f, 0.0f, // B
		 0.219f, 0.707f, -0.672f, // F
		 
		 0.707f, 0.707f, 0.0f, // B
		 0.809f, 0.0f, 0.588f, // H
		 1.0f, 0.0f, 0.0f, // G
		 
		 0.219f, 0.707f, 0.672f, // C
		 0.309f, 0.0f, 0.951f, // I
		 0.809f, 0.0f, 0.588f, // H
		 		 
		 0.219f, 0.707f, 0.672f, // C
		 0.809f, 0.0f, 0.588f, // H
		 0.707f, 0.707f, 0.0f, // B
		 
		 0.219f, 0.707f, 0.672f, // C
		 -0.309f, 0.0f, 0.951f, // J
		 0.309f, 0.0f, 0.951f, // I
		 
		 0.219f, 0.707f, 0.672f, // C
		 -0.572f, 0.707f, 0.416f, // D
		 -0.309f, 0.0f, 0.951f, // J
		 
		 -0.572f, 0.707f, 0.416f, // D
		 -0.809f, 0.0f, 0.588f, // K
		 -0.309f, 0.0f, 0.951f, // J
		 
		 -0.572f, 0.707f, 0.416f, // D
		 -1.0f,   0.0f, 0.0f, // L	
		 -0.809f, 0.0f, 0.588f, // K
		 
		 -0.572f, 0.707f, 0.416f, // D
		 -0.572f, 0.707f, -0.416f, // E
		 -1.0f,   0.0f, 0.0f, // L	
		 
		 -0.572f, 0.707f, -0.416f, // E
		 -0.809f, 0.0f, -0.588f, // M
		 -1.0f,   0.0f, 0.0f, // L	
		 
		 -0.572f, 0.707f, -0.416f, // E
		 -0.309f, 0.0f, -0.951f, // N
		 -0.809f, 0.0f, -0.588f, // M
		 
		 -0.572f, 0.707f, -0.416f, // E
		 0.219f, 0.707f, -0.672f, // F
		 -0.309f, 0.0f, -0.951f, // N
		 
		 0.219f, 0.707f, -0.672f, // F
		 0.309f, 0.0f, -0.951f, // O
		 -0.309f, 0.0f, -0.951f, // N
		 
		 0.219f, 0.707f, -0.672f, // F
		 0.809f, 0.0f, -0.588f, // P
		 0.309f, 0.0f, -0.951f, // O
		 
		 0.219f, 0.707f, -0.672f, // F
		 0.707f, 0.707f, 0.0f, // B
		 0.809f, 0.0f, -0.588f, // P
		 
		 0.707f, 0.707f, 0.0f, // B
		 1.0f,   0.0f, 0.0f, // G
		 0.809f, 0.0f, -0.588f, // P
		 
	};
	
	private final int vertexCount = VertexCoords.length / COORDS_PER_VERTEX;
	
	static final int BARYCENTRICCOORDS_PER_VERTEX = 3;
	static final int BARYCENTRICCOORD_STRIDE = BARYCENTRICCOORDS_PER_VERTEX * 4;	// 4 Bytes to a float.
	
	static float BarycentricCoords[] =
	{
		1.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 0.0f, 1.0f,
		 
		1.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 0.0f, 1.0f,
		
		1.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 0.0f, 1.0f,
		 
		1.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 0.0f, 1.0f,
		
		1.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 0.0f, 1.0f,
		
		1.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 0.0f, 1.0f,
		
		1.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 0.0f, 1.0f,
		
		1.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 0.0f, 1.0f,
		
		1.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 0.0f, 1.0f,
		
		1.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 0.0f, 1.0f,
		
		1.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 0.0f, 1.0f,
		
		1.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 0.0f, 1.0f,
		
		1.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 0.0f, 1.0f,
		
		1.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 0.0f, 1.0f,
		
		1.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 0.0f, 1.0f,
		
		1.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 0.0f, 1.0f,
		
		1.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 0.0f, 1.0f,
		
		1.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 0.0f, 1.0f,
		
		1.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 0.0f, 1.0f,
		
		1.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 0.0f, 1.0f,
	};
	
	public GLExplosion()
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(VertexCoords.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(VertexCoords);
		vertexBuffer.position(0);
		
		ByteBuffer bcb = ByteBuffer.allocateDirect(BarycentricCoords.length * 4);
		bcb.order(ByteOrder.nativeOrder());
		barycentricCoordBuffer = bcb.asFloatBuffer();
		barycentricCoordBuffer.put(BarycentricCoords);
		barycentricCoordBuffer.position(0);
		
		m_Colour[0] = 1.0f;
		m_Colour[1] = 1.0f;
		m_Colour[2] = 1.0f;
		m_Colour[3] = 1.0f;

        m_Program 			= 0;
        m_ProjMatrixHandle  = 0;
        m_WorldPosHandle    = 0;
        m_YawHandle         = 0;
        m_RollHandle        = 0;
        m_ForwardHandle     = 0;
        m_ColourHandle		= 0;
        m_PositionHandle	= 0;
        m_BarycentricHandle = 0;
	    
	    InitShaders();
	}

    public void draw(Vector3 pos, Vector3 forward, float roll, float yaw, float[] projMatrix)
    {
        GLES20.glUniform4f(m_WorldPosHandle, (float)pos.I, (float)pos.J, (float)pos.K, 1.0f);
        GLES20.glUniform3f(m_ForwardHandle, (float)forward.I, (float)forward.J, (float)forward.K);
        GLES20.glUniform1f(m_YawHandle, yaw);
        GLES20.glUniform1f(m_RollHandle, roll);

        GLES20.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);
        GLES20.glUniform4fv(m_ColourHandle, 1, m_Colour, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
    }
	
	public void SetColour(Colour colour)
	{
		m_Colour[0] = (float)colour.Red;
		m_Colour[1] = (float)colour.Green;
		m_Colour[2] = (float)colour.Blue;
		m_Colour[3] = (float)colour.Alpha;
	}

    private void InitShaders()
    {
        // prepare shaders and OpenGL program
        int vertexShaderHandler 	= loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_BARYCENTRIC);
        int fragmentShaderHandler 	= loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_BARYCENTRIC_HOLLOW);

        m_Program = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle = GLES20.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_ForwardHandle    = GLES20.glGetUniformLocation(m_Program, "u_Forward");
        m_WorldPosHandle   = GLES20.glGetUniformLocation(m_Program, "u_WorldPos");
        m_YawHandle        = GLES20.glGetUniformLocation(m_Program, "u_Yaw");
        m_RollHandle       = GLES20.glGetUniformLocation(m_Program, "u_Roll");
        m_ColourHandle 	   = GLES20.glGetUniformLocation(m_Program, "u_Color");

        m_PositionHandle    = GLES20.glGetAttribLocation(m_Program, "a_Position");
        m_BarycentricHandle = GLES20.glGetAttribLocation(m_Program, "a_Barycentric");
    }

    @Override
    public void InitialiseModel()
    {
        GLES20.glUseProgram(m_Program);

        GLES20.glDisable(GLES20.GL_CULL_FACE);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);

        GLES20.glEnableVertexAttribArray(m_PositionHandle);
        GLES20.glVertexAttribPointer(m_PositionHandle, GLCube.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, GLCube.VERTEX_STRIDE, vertexBuffer);

        GLES20.glEnableVertexAttribArray(m_BarycentricHandle);
        GLES20.glVertexAttribPointer(m_BarycentricHandle, GLCube.BARYCENTRICCOORDS_PER_VERTEX, GLES20.GL_FLOAT, false, GLCube.BARYCENTRICCOORD_STRIDE, barycentricCoordBuffer);
    }

    @Override
    public void CleanModel()
    {
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        GLES20.glDisableVertexAttribArray(m_PositionHandle);
        GLES20.glDisableVertexAttribArray(m_BarycentricHandle);
    }
}
