package com.raggamuffin.protorunnerv2.renderer;

import com.raggamuffin.protorunnerv2.utils.Vector3;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class GLOrthoCamera 
{
	public static final String TAG = "DEBUG GLOrthoCamera";
	
	public final float[] m_VMatrix = new float[16];
	public final float[] m_ProjMatrix = new float[16];
	
	private Vector3 m_Position;		
	private Vector3 m_LookAt;
	private Vector3 m_Up;
	
	private float m_Near;
	private float m_Far;

	public GLOrthoCamera()
	{
		Log.e(TAG, "GLCamera");

		m_Position = new Vector3(0.0, 0.0, 10.0);
		m_LookAt   = new Vector3(0.0, 0.0, 0.0);
		m_Up 	   = new Vector3(0.0, 1.0, 0.0);
		
		m_Near = 1.0f;
		m_Far = 100.0f;
		
		Matrix.setLookAtM(m_VMatrix, 0,   (float)m_Position.I, (float)m_Position.J, (float)m_Position.K,   (float)m_LookAt.I, (float)m_LookAt.J, (float)m_LookAt.K,  (float) m_Up.I,(float)m_Up.J, (float)m_Up.K);
	}
	
	public void Update()
	{	
		Matrix.setLookAtM(m_VMatrix, 0,   (float)m_Position.I, (float)m_Position.J, (float)m_Position.K,   (float)m_LookAt.I, (float)m_LookAt.J, (float)m_LookAt.K,  (float) m_Up.I,(float)m_Up.J, (float)m_Up.K);
	}
	
	public void ViewPortChanged(int width, int height)
	{
		Log.e(TAG, "ViewPortChanged");
		
		GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
     
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.orthoM(m_ProjMatrix, 0, -ratio, ratio, -1, 1, m_Near, m_Far);
	}
	
	public Vector3 GetPosition()
	{
		return m_Position;
	}
}
