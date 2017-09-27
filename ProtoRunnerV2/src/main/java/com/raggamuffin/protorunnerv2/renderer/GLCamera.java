package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES31;
import android.opengl.Matrix;
import android.util.Log;

import com.raggamuffin.protorunnerv2.utils.Vector3;

public class GLCamera 
{
	public static final String TAG = "DEBUG GLCamera";
	
	public final float[] m_VMatrix = new float[16];
	public final float[] m_ProjMatrix = new float[16];

	private float m_Near;
	private float m_Far;

	private Vector3 m_Position;

	public GLCamera()
	{
		m_Near = 1.0f;
		m_Far = 10000.0f;

		m_Position = new Vector3();
	}
	
	public void Update(RenderCamera camera)
	{
		//Vector3 pos = camera.GetPosition();
		//Vector3 look = camera.GetLookAt();
		//Vector3 up = camera.GetUp();

	//	Matrix.setLookAtM(m_VMatrix, 0,   (float)pos.X, (float)pos.Y, (float)pos.Z,   (float)look.X, (float)look.Y, (float)look.Z,  (float) up.X,(float)up.Y, (float)up.Z);

	//	m_Position.SetVector(pos);
	}
	
	public void ViewPortChanged(int width, int height)
	{
		Log.e(TAG, "ViewPortChanged");
		GLES31.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(m_ProjMatrix, 0, ratio, -ratio, -1, 1, m_Near, m_Far);
	}
	
	public Vector3 GetPosition()
	{
		return m_Position;
	}
}
