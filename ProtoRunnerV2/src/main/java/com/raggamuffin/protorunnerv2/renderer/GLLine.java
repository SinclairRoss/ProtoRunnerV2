package com.raggamuffin.protorunnerv2.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

import com.raggamuffin.protorunnerv2.utils.Colour;

public class GLLine
{
	public final FloatBuffer vertexBuffer;
	public final FloatBuffer weightBuffer;
	
	private int m_Program;
	
	private int m_MVPMatrixHandle;  
    private int m_ColourHandle;
    private int m_EndPointColourHandle;
    private int m_PositionHandle;
    private int m_WeightHandle;
    
    private float[] m_Colour;
    private float[] m_EndPointColour;
	
	static final int COORDS_PER_VERTEX = 3;
	static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;	// 4 Bytes to a float.
	
	static float VertexCoords[] =
	{	
		 0.0f,  0.0f, 0.0f, // A.
		 0.0f,  0.0f, 1.0f, // B.
	};
	
	private final int vertexCount = VertexCoords.length / COORDS_PER_VERTEX;
	
	static final int WEIGHTS_PER_VERTEX = 1;
	static final int WEIGHT_STRIDE = WEIGHTS_PER_VERTEX * 4;
	
	static float VertexWeight[] =
	{
		0.0f, 1.0f
	};
	
	public GLLine()
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(VertexCoords.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(VertexCoords);
		vertexBuffer.position(0);
		
		ByteBuffer wb = ByteBuffer.allocateDirect(VertexWeight.length * 4);
		wb.order(ByteOrder.nativeOrder());
		weightBuffer = wb.asFloatBuffer();
		weightBuffer.put(VertexWeight);
		weightBuffer.position(0);
	
		m_Colour = new float[4];
		m_Colour[0] = 1.0f;
		m_Colour[1] = 1.0f;
		m_Colour[2] = 1.0f;
		m_Colour[3] = 1.0f;
		
		m_EndPointColour = new float[4];
		m_EndPointColour[0] = 1.0f;
		m_EndPointColour[1] = 1.0f;
		m_EndPointColour[2] = 1.0f;
		m_EndPointColour[3] = 1.0f;

		m_Program 			= 0;
	    m_MVPMatrixHandle	= 0;  
	    m_ColourHandle		= 0;
	    m_EndPointColourHandle = 0;
	    m_PositionHandle	= 0;
	    m_WeightHandle 		= 0;
	
	    InitShaders();
	}
	
	public void draw(float[] mvpMatrix)
	{
		GLES20.glUseProgram(m_Program);
		
		GLES20.glUniformMatrix4fv(m_MVPMatrixHandle, 1, false, mvpMatrix, 0);
        GLES20.glUniform4fv(m_ColourHandle, 1, m_Colour, 0);
        GLES20.glUniform4fv(m_EndPointColourHandle, 1, m_EndPointColour, 0);

        GLES20.glEnableVertexAttribArray(m_PositionHandle);
		GLES20.glVertexAttribPointer(m_PositionHandle, GLLine.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, GLLine.VERTEX_STRIDE, vertexBuffer);
		
		GLES20.glEnableVertexAttribArray(m_WeightHandle);
		GLES20.glVertexAttribPointer(m_WeightHandle, GLLine.WEIGHTS_PER_VERTEX, GLES20.GL_FLOAT, false, GLLine.WEIGHT_STRIDE, weightBuffer);		
		
		GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertexCount);
		
		GLES20.glDisableVertexAttribArray(m_PositionHandle);
	}
	
	public void SetColour(Colour colour)
	{
		m_Colour[0] = (float)colour.Red;
		m_Colour[1] = (float)colour.Green;
		m_Colour[2] = (float)colour.Blue;
		m_Colour[3] = (float)colour.Alpha;
	}
	
	public void SetEndPointColour(Colour colour)
	{
		m_EndPointColour[0] = (float)colour.Red;
		m_EndPointColour[1] = (float)colour.Green;
		m_EndPointColour[2] = (float)colour.Blue;
		m_EndPointColour[3] = (float)colour.Alpha;
	}
	
	private void InitShaders()
    {
		int vertexShaderHandler = 0;
		int fragmentShaderHandler = 0;

		// prepare shaders and OpenGL program
		vertexShaderHandler 	= loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_LINE);
		fragmentShaderHandler 	= loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_LINE);

		m_Program = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_MVPMatrixHandle 		= GLES20.glGetUniformLocation(m_Program, "u_MVPMatrix");  
        m_ColourHandle 			= GLES20.glGetUniformLocation(m_Program, "u_Color");
        m_EndPointColourHandle	= GLES20.glGetUniformLocation(m_Program, "u_EndPointColor");
        
        m_PositionHandle = GLES20.glGetAttribLocation(m_Program, "a_Position");    
        m_WeightHandle = GLES20.glGetAttribLocation(m_Program, "a_Weight");
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
