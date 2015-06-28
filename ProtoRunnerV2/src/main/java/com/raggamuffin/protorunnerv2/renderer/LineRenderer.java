package com.raggamuffin.protorunnerv2.renderer;

import android.opengl.GLES20;

import com.raggamuffin.protorunnerv2.particles.TrailParticle;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class LineRenderer 
{
	private GLLine m_Line;
	private Vector3 m_ToEye;
	
	public LineRenderer()
	{
		m_Line = null;
		
		m_ToEye = new Vector3();
	}
	
	public void LoadAssets()
	{
		m_Line = new GLLine();
	}
	
	public void DrawTrail(Vector3 pos, Vector3 eyePos, Colour colour, Colour endColour, final float[] mvpMatrix)
	{
		m_ToEye.SetVectorDifference(eyePos, pos);
		float dist = (float) m_ToEye.GetLength();
				
		GLES20.glLineWidth((float) (20.0f * MathsHelper.FastInverseSqrt(dist)));
		m_Line.SetColour(colour);
		m_Line.SetEndPointColour(endColour);
		m_Line.draw(mvpMatrix);		
	}
}
