package com.raggamuffin.protorunnerv2.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.raggamuffin.protorunnerv2.utils.Colour;

import android.opengl.GLES20;

public class GLRing 
{
    private final float OUTER_RADIUS = 1.0f;
    private final float THICKNESS = 0.1f;
    private final float INNER_RADIUS = OUTER_RADIUS - THICKNESS;

	public final FloatBuffer vertexBuffer;
    public final FloatBuffer weightBuffer;
	
	private int m_Program;
	
	private int m_MVPMatrixHandle;  
    private int m_ColourHandle;
    private int m_EndPointColourHandle;
    private int m_PositionHandle;
    private int m_WeightHandle;

    private int m_NumEdges;
    private int m_NumVertices;
    
    private float[] m_Colour = new float[4];
    private float[] m_EndPointColour;
	
	static final int COORDS_PER_VERTEX = 3;
	static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;	// 4 Bytes to a float.
	
	float VertexCoords[];

    static final int WEIGHTS_PER_VERTEX = 1;
    static final int WEIGHT_STRIDE = WEIGHTS_PER_VERTEX * 4;

    static float VertexWeight[];

	public GLRing()
	{
        m_NumEdges = 6;
		m_NumVertices = m_NumEdges * 2 * 3;  // Num Edges * Num Rings * Vertz per triangle.
		
		VertexCoords = new float[m_NumVertices * COORDS_PER_VERTEX];
        VertexWeight = new float[m_NumVertices];

		double DeltaTheta = (float) (Math.PI * 2.0f) / m_NumEdges;
		double Theta = 0.0f;
		
		for (int v = 0; v < m_NumEdges; v++)
		{
            // Triangle 1
            // 0
            VertexCoords[v * 18] 	 = (float) Math.cos(Theta) * OUTER_RADIUS;
            VertexCoords[v * 18 + 1] = 0.0f;
            VertexCoords[v * 18 + 2] = (float) Math.sin(Theta) * OUTER_RADIUS;
            VertexWeight[v * 6]      = 0.0f;

            // 1
            VertexCoords[v * 18 + 3] = (float) Math.cos(Theta) * INNER_RADIUS;
            VertexCoords[v * 18 + 4] = 0.0f;
            VertexCoords[v * 18 + 5] = (float) Math.sin(Theta) * INNER_RADIUS;
            VertexWeight[v * 6 + 1]  = 1.0f;

			Theta += DeltaTheta;

            // 2
            VertexCoords[v * 18 + 6] = (float) Math.cos(Theta) * OUTER_RADIUS;
            VertexCoords[v * 18 + 7] = 0.0f;
            VertexCoords[v * 18 + 8] = (float) Math.sin(Theta) * OUTER_RADIUS;
            VertexWeight[v * 6 + 2]   = 0.0f;

            // Triangle 2
            // 1
            VertexCoords[v * 18 + 9]  = VertexCoords[v * 18 + 3];
            VertexCoords[v * 18 + 10] = 0.0f;
            VertexCoords[v * 18 + 11] = VertexCoords[v * 18 + 5];
            VertexWeight[v * 6 + 3]   = 1.0f;

            // 3
            VertexCoords[v * 18 + 12] = (float) Math.cos(Theta) * INNER_RADIUS;
            VertexCoords[v * 18 + 13] = 0.0f;
            VertexCoords[v * 18 + 14] = (float) Math.sin(Theta) * INNER_RADIUS;
            VertexWeight[v * 6 + 4]   = 1.0f;

            // 2
            VertexCoords[v * 18 + 15] = (float) Math.cos(Theta) * OUTER_RADIUS;
            VertexCoords[v * 18 + 16] = 0.0f;
            VertexCoords[v * 18 + 17] = (float) Math.sin(Theta) * OUTER_RADIUS;
            VertexWeight[v * 6 + 5] = 0.0f;
		}

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
		
		m_Colour[0] = 0.0f;
		m_Colour[1] = 1.0f;
		m_Colour[2] = 0.0f;
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
		GLES20.glVertexAttribPointer(m_PositionHandle, GLRing.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, GLRing.VERTEX_STRIDE, vertexBuffer);

        GLES20.glEnableVertexAttribArray(m_WeightHandle);
        GLES20.glVertexAttribPointer(m_WeightHandle, GLRing.WEIGHTS_PER_VERTEX, GLES20.GL_FLOAT, false, GLRing.WEIGHT_STRIDE, weightBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, m_NumVertices);
	}
	
	public void SetColour(Colour colour)
	{
        m_Colour[0] = (float)colour.Red;
        m_Colour[1] = (float)colour.Green;
        m_Colour[2] = (float)colour.Blue;
        m_Colour[3] = 0.7f;
	}

    public void SetEndPointColour(Colour colour)
    {
        m_EndPointColour[0] = (float)colour.Red;
        m_EndPointColour[1] = (float)colour.Green;
        m_EndPointColour[2] = (float)colour.Blue;
        m_EndPointColour[3] = 0.0f;
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