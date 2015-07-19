package com.raggamuffin.protorunnerv2.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import android.opengl.GLES20;

public class GLMissile extends GLModel
{
	public final FloatBuffer vertexBuffer;
	public final FloatBuffer barycentricCoordBuffer;
	
	private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_WorldPosHandle;
    private int m_YawHandle;
    private int m_RollHandle;
    private int m_ForwardHandle;
    private int m_ScaleHandle;
    private int m_ColourHandle;
    private int m_PositionHandle;
    private int m_BarycentricHandle;
    
    private float[] m_Colour = new float[4];
	
	static final int COORDS_PER_VERTEX = 3;
	static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;	// 4 Bytes to a float.
	
	static float VertexCoords[] =
	{	
		// Rear - Top Left.
		-0.5f,  0.0f, -0.75f, // A.
		 0.0f,  0.375f, -0.4f, // B.
		 0.0f,  0.0f, -0.25f, // E.
		 	 
		 // Rear - Top Right.
		 0.0f,  0.375f, -0.4f, // B.
		 0.5f,  0.0f, -0.75f, // C.
		 0.0f,  0.0f, -0.25f, // E.
		 
		 // Rear - Bottom Right.
		 0.5f,  0.0f, -0.75f, // C.
		 0.0f, -0.375f, -0.4f, // D.
		 0.0f,  0.0f, -0.25f, // E.
		 
		 // Rear - Bottom Left.
		 0.0f, -0.375f, -0.4f, // D.
		 -0.5f,  0.0f, -0.75f, // A.
		 0.0f,  0.0f, -0.25f, // E.
		
		 // Front - Top Left.
		 -0.5f,  0.0f, -0.75f, // A.
		 0.0f,  0.0f,  0.75f, // F.
		 0.0f,  0.375f, -0.4f, // B.
		 
		 // Front - Top Right.
		 0.5f,  0.0f, -0.75f, // C.
		 0.0f,  0.375f, -0.4f, // B.
		 0.0f,  0.0f,  0.75f, // F.
		 
		// Front - Top Left.
		 -0.5f,  0.0f, -0.75f, // A.
		0.0f, -0.375f, -0.4f, // D.
		 0.0f,  0.0f,  0.75f, // F.
		 
		 // Front - Top Right.
		 0.5f,  0.0f, -0.75f, // C.
		 0.0f,  0.0f,  0.75f, // F.
		 0.0f, -0.375f, -0.4f, // D.
	};
	
	private final int vertexCount = VertexCoords.length / COORDS_PER_VERTEX;
	
	static final int BARYCENTRICCOORDS_PER_VERTEX = 3;
	static final int BARYCENTRICCOORD_STRIDE = BARYCENTRICCOORDS_PER_VERTEX * 4;	// 4 Bytes to a float.
	
	static float BarycentricCoords[] =
	{
		// Rear - Top Left.
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
		 
		 // Rear - Top Right.
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
		 
		 // Rear - Bottom Right.
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
		 
		 // Rear - Bottom Left.
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
		 
		 // Front - Top Left.
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
		 
		 // Front - Top Right.
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
		 
		 // Front - Top Left.
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f,
			 
	     // Front - Top Right.
		 1.0f, 0.0f, 0.0f,
		 0.0f, 1.0f, 0.0f,
		 0.0f, 0.0f, 1.0f
	};
	
	public GLMissile()
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
        m_ScaleHandle       = 0;
        m_ColourHandle		= 0;
        m_PositionHandle	= 0;
        m_BarycentricHandle = 0;
	    
	    InitShaders();
	}


    public void draw(Vector3 pos, Vector3 scale, Vector3 forward, float roll, float yaw, float[] projMatrix)
    {
        GLES20.glUniform4f(m_WorldPosHandle, (float)pos.I, (float)pos.J, (float)pos.K, 1.0f);
        GLES20.glUniform3f(m_ForwardHandle, (float) forward.I, (float) forward.J, (float) forward.K);
        GLES20.glUniform1f(m_YawHandle, yaw);
        GLES20.glUniform1f(m_RollHandle, roll);
        GLES20.glUniform3f(m_ScaleHandle, (float) scale.I, (float) scale.J, (float) scale.K);

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
        int fragmentShaderHandler 	= loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_BARYCENTRIC);

        m_Program = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle = GLES20.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_ForwardHandle    = GLES20.glGetUniformLocation(m_Program, "u_Forward");
        m_WorldPosHandle   = GLES20.glGetUniformLocation(m_Program, "u_WorldPos");
        m_YawHandle        = GLES20.glGetUniformLocation(m_Program, "u_Yaw");
        m_RollHandle       = GLES20.glGetUniformLocation(m_Program, "u_Roll");
        m_ScaleHandle      = GLES20.glGetUniformLocation(m_Program, "u_Scale");
        m_ColourHandle 	   = GLES20.glGetUniformLocation(m_Program, "u_Color");

        m_PositionHandle    = GLES20.glGetAttribLocation(m_Program, "a_Position");
        m_BarycentricHandle = GLES20.glGetAttribLocation(m_Program, "a_Barycentric");
    }

    @Override
    public void InitialiseModel()
    {
        GLES20.glUseProgram(m_Program);

        GLES20.glEnableVertexAttribArray(m_PositionHandle);
        GLES20.glVertexAttribPointer(m_PositionHandle, GLMissile.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, GLMissile.VERTEX_STRIDE, vertexBuffer);

        GLES20.glEnableVertexAttribArray(m_BarycentricHandle);
        GLES20.glVertexAttribPointer(m_BarycentricHandle, GLMissile.BARYCENTRICCOORDS_PER_VERTEX, GLES20.GL_FLOAT, false, GLMissile.BARYCENTRICCOORD_STRIDE, barycentricCoordBuffer);
    }

    @Override
    public void CleanModel()
    {
        GLES20.glDisableVertexAttribArray(m_PositionHandle);
        GLES20.glDisableVertexAttribArray(m_BarycentricHandle);
    }

    @Override
    public int GetVertexCount()
    {
        return vertexCount;
    }

    @Override
    public void Draw(float[] projMatrix)
    {

    }
}
