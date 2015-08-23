package com.raggamuffin.protorunnerv2.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class GLLine extends GLModel
{
	public final FloatBuffer vertexBuffer;
	public final FloatBuffer weightBuffer;

    private Vector3 m_EyePos;
    private Vector3 m_ToEye;

	private int m_Program;
	
	private int m_ProjMatrixHandle;
    private int m_WorldPosHandle;
    private int m_YawHandle;
    private int m_ScaleHandle;
    private int m_ColourHandle;
    private int m_EndPointColourHandle;
    private int m_PositionHandle;
    private int m_WeightHandle;

    private float m_LineThickness;
	
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
	
	public GLLine(float thickness)
	{
        m_LineThickness = thickness;

        m_EyePos = new Vector3();
        m_ToEye = new Vector3();

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

		m_Program 			= 0;
	    m_ProjMatrixHandle = 0;
        m_WorldPosHandle    = 0;
        m_YawHandle = 0;
        m_ScaleHandle       = 0;
	    m_ColourHandle		= 0;
	    m_EndPointColourHandle = 0;
	    m_PositionHandle	= 0;
	    m_WeightHandle 		= 0;
	
	    InitShaders();
	}
	
	protected void InitShaders()
    {
		// prepare shaders and OpenGL program
        int vertexShaderHandler 	= loadShader(GLES20.GL_VERTEX_SHADER,Shaders.vertexShader_LINE);
        int fragmentShaderHandler 	= loadShader(GLES20.GL_FRAGMENT_SHADER,Shaders.fragmentShader_LINE);

		m_Program = GLES20.glCreateProgram();             		// create empty OpenGL Program
        GLES20.glAttachShader(m_Program, vertexShaderHandler);   // add the vertex shader to program
        GLES20.glAttachShader(m_Program, fragmentShaderHandler); // add the fragment shader to program
        GLES20.glLinkProgram(m_Program);                  		// create OpenGL program executables

        m_ProjMatrixHandle = GLES20.glGetUniformLocation(m_Program, "u_ProjMatrix");
        m_WorldPosHandle   = GLES20.glGetUniformLocation(m_Program, "u_WorldPos");
        m_YawHandle        = GLES20.glGetUniformLocation(m_Program, "u_Yaw");
        m_ScaleHandle      = GLES20.glGetUniformLocation(m_Program, "u_Scale");

        m_ColourHandle 			= GLES20.glGetUniformLocation(m_Program, "u_Color");
        m_EndPointColourHandle	= GLES20.glGetUniformLocation(m_Program, "u_EndPointColor");
        
        m_PositionHandle = GLES20.glGetAttribLocation(m_Program, "a_Position");    
        m_WeightHandle = GLES20.glGetAttribLocation(m_Program, "a_Weight");
    }

    @Override
    public void InitialiseModel(float[] projMatrix, Vector3 eye)
    {
        m_EyePos.SetVector(eye);

        GLES20.glUseProgram(m_Program);

        GLES20.glUniformMatrix4fv(m_ProjMatrixHandle, 1, false, projMatrix, 0);

        GLES20.glEnableVertexAttribArray(m_PositionHandle);
        GLES20.glVertexAttribPointer(m_PositionHandle, GLLine.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, GLLine.VERTEX_STRIDE, vertexBuffer);

        GLES20.glEnableVertexAttribArray(m_WeightHandle);
        GLES20.glVertexAttribPointer(m_WeightHandle, GLLine.WEIGHTS_PER_VERTEX, GLES20.GL_FLOAT, false, GLLine.WEIGHT_STRIDE, weightBuffer);
    }

    @Override
    public void Draw(GameObject obj)
    {
        Vector3 pos = obj.GetPosition();

        m_ToEye.SetVectorDifference(m_EyePos, pos);
        float dist = (float) m_ToEye.GetLength();

        GLES20.glLineWidth((float) (m_LineThickness * MathsHelper.FastInverseSqrt(dist)));

        GLES20.glUniform4f(m_WorldPosHandle, (float) pos.I, (float) pos.J, (float) pos.K, 1.0f);

        GLES20.glUniform1f(m_YawHandle, (float) obj.GetYaw());

        Vector3 scale = obj.GetScale();
        GLES20.glUniform3f(m_ScaleHandle, (float) scale.I, (float) scale.J, (float) scale.K);

        Colour colour = obj.GetColour();
        GLES20.glUniform4f(m_ColourHandle, (float) colour.Red, (float) colour.Green, (float) colour.Blue, (float) colour.Alpha);

        Colour endColour = obj.GetAltColour();
        GLES20.glUniform4f(m_EndPointColourHandle, (float) endColour.Red, (float) endColour.Green, (float) endColour.Blue, (float) endColour.Alpha);

        GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertexCount);
    }

    @Override
    public void CleanModel()
    {
        GLES20.glDisableVertexAttribArray(m_PositionHandle);
    }
}
