package com.raggamuffin.protorunnerv2.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import android.opengl.GLES20;

public class GLPulseLaser 
{
    public final FloatBuffer vertexBuffer;
    public int PositionHandle;
    
    private int m_Program;
	
	private int m_MVPMatrixHandle; 
	private int m_SizeHandle;
    private int m_ColourHandle;
    private int m_PositionHandle;
    private int m_WorldPosHandle;
    private int m_EyePosHandle;
    
    private float[] m_Colour;
    private float m_Size;
    private float[] m_WorldPos;
    private float[] m_EyePos;
    
    static final int COORDS_PER_VERTEX = 3;
    static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
    static float VertexCoords[] = 
    { 
        0.0f, 0.0f, 0.0f
    };
    
    public GLPulseLaser(float size) 
    {
    	// create a byte buffer for the vertex coords.
    	ByteBuffer bb = ByteBuffer.allocateDirect(VertexCoords.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(VertexCoords);
		vertexBuffer.position(0);
		
		m_Colour = new float[4];
		m_Colour[0] = 1.0f;
		m_Colour[1] = 1.0f;
		m_Colour[2] = 1.0f;
		m_Colour[3] = 1.0f;
		
		m_Size = size;
		
		m_WorldPos = new float[4];
		m_WorldPos[0] = 0.0f;
		m_WorldPos[1] = 0.0f;
		m_WorldPos[2] = 0.0f;
		m_WorldPos[3] = 0.0f;
		
		m_EyePos = new float[4];
		m_EyePos[0] = 0.0f;
		m_EyePos[1] = 0.0f;
		m_EyePos[2] = 0.0f;
		m_EyePos[3] = 0.0f;
		
		m_Program 			= 0;
	    m_MVPMatrixHandle	= 0;  
	    m_SizeHandle		= 0;
	    m_ColourHandle		= 0;
	    m_PositionHandle	= 0;
	    m_WorldPosHandle 	= 0;
	    m_EyePosHandle 		= 0;
	    
	    InitShaders();
	       
    }
	
	public void draw(float[] mvpMatrix) 
	{
		// set the shader program that will render this object.
	    GLES20.glUseProgram(m_Program);
	    
		// Set the shader information.
		GLES20.glUniformMatrix4fv(m_MVPMatrixHandle, 1, false, mvpMatrix, 0);
		GLES20.glUniform4fv(m_ColourHandle, 1, m_Colour, 0);
		GLES20.glUniform4fv(m_WorldPosHandle, 1, m_WorldPos, 0);
		GLES20.glUniform4fv(m_EyePosHandle, 1, m_EyePos, 0);
        GLES20.glUniform1f(m_SizeHandle, m_Size);
        
        GLES20.glEnableVertexAttribArray(m_PositionHandle);
		GLES20.glVertexAttribPointer(m_PositionHandle, GLPulseLaser.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, GLPulseLaser.VERTEX_STRIDE, vertexBuffer);

		// Draw the object using glPoints.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
        
    	GLES20.glDisableVertexAttribArray(m_PositionHandle);
	}
	
	public void SetColour(Colour colour)
	{
		m_Colour[0] = (float)colour.Red;
		m_Colour[1] = (float)colour.Green;
		m_Colour[2] = (float)colour.Blue;
		m_Colour[3] = (float)colour.Alpha;
	}
	
	public void SetEyePos(Vector3 EyePos)
	{
		m_EyePos[0] = (float)EyePos.I;
		m_EyePos[1] = (float)EyePos.J;
		m_EyePos[2] = (float)EyePos.K;
		m_EyePos[3] = 0.0f;
	}
	
	public void SetWorldPos(Vector3 WorldPos)
	{
		m_WorldPos[0] = (float)WorldPos.I;
		m_WorldPos[1] = (float)WorldPos.J;
		m_WorldPos[2] = (float)WorldPos.K;
		m_WorldPos[3] = 0.0f;
	}
	
	private void InitShaders()
    {
		int vertexShaderHandler = 0;
		int fragmentShaderHandler = 0;

		// prepare shaders and OpenGL program
		vertexShaderHandler 	= loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_POINT);
		fragmentShaderHandler 	= loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_FADEPOINT);

		m_Program = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_MVPMatrixHandle 	= GLES20.glGetUniformLocation(m_Program, "u_MVPMatrix");  
        m_ColourHandle 		= GLES20.glGetUniformLocation(m_Program, "u_Color");
        m_SizeHandle 		= GLES20.glGetUniformLocation(m_Program, "u_Size");
        m_EyePosHandle 		= GLES20.glGetUniformLocation(m_Program, "u_EyePos");
        m_WorldPosHandle 	= GLES20.glGetUniformLocation(m_Program, "u_WorldPos");
        
        m_PositionHandle 	= GLES20.glGetAttribLocation(m_Program, "a_Position");
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
}
