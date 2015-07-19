package com.raggamuffin.protorunnerv2.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import android.opengl.GLES20;

public class GLPulseLaser extends GLModel
{
    public final FloatBuffer vertexBuffer;
    
    private int m_Program;

    private int m_ProjMatrixHandle;
    private int m_SizeHandle;
    private int m_ColourHandle;
    private int m_PositionHandle;
    private int m_WorldPosHandle;
    private int m_EyePosHandle;
    
    private float[] m_Colour;
    private float m_Size;
    
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

        m_Program 			= 0;
        m_ProjMatrixHandle  = 0;
        m_SizeHandle		= 0;
        m_ColourHandle		= 0;
        m_PositionHandle	= 0;
        m_WorldPosHandle 	= 0;
        m_EyePosHandle 		= 0;
	    
	    InitShaders();
    }

    public void draw(Vector3 pos, Vector3 eye, float[] projMatrix)
    {
        // Set the shader information.
        GLES20.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);
        GLES20.glUniform4fv(m_ColourHandle, 1, m_Colour, 0);
        GLES20.glUniform4f(m_WorldPosHandle, (float) pos.I, (float) pos.J, (float) pos.K, 1.0f);
        GLES20.glUniform4f(m_EyePosHandle, (float) eye.I, (float) eye.J, (float) eye.K, 1.0f);
        GLES20.glUniform1f(m_SizeHandle, m_Size);

        // Draw the object using glPoints.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
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
        int vertexShaderHandler 	= loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_POINT);
        int fragmentShaderHandler 	= loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_FADEPOINT);

        m_Program = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle = GLES20.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_ColourHandle 		= GLES20.glGetUniformLocation(m_Program, "u_Color");
        m_SizeHandle 		= GLES20.glGetUniformLocation(m_Program, "u_Size");
        m_EyePosHandle 		= GLES20.glGetUniformLocation(m_Program, "u_EyePos");
        m_WorldPosHandle 	= GLES20.glGetUniformLocation(m_Program, "u_WorldPos");

        m_PositionHandle 	= GLES20.glGetAttribLocation(m_Program, "a_Position");
    }

    @Override
    public void InitialiseModel()
    {
        GLES20.glUseProgram(m_Program);

        GLES20.glEnableVertexAttribArray(m_PositionHandle);
        GLES20.glVertexAttribPointer(m_PositionHandle, GLPulseLaser.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, GLPulseLaser.VERTEX_STRIDE, vertexBuffer);
    }

    @Override
    public void CleanModel()
    {
        GLES20.glDisableVertexAttribArray(m_PositionHandle);
    }

    @Override
    public int GetVertexCount()
    {
        return 1;
    }

    @Override
    public void Draw(float[] projMatrix)
    {

    }
}
