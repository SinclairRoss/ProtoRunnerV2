// Author: 	Sinclair Ross
// Date:	23/09/2014

package com.raggamuffin.protorunnerv2.master;

import com.raggamuffin.protorunnerv2.renderer.GLRenderer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameView extends GLSurfaceView
{
	public static final String TAG = "DEBUG GameView";
	private GLRenderer m_Renderer;
	
	public GameView(Context context, RendererPacket packet) 
	{	
		super(context);
		
		Log.e(TAG, "GameView");

		m_Renderer = new GLRenderer(packet);	
		
		setEGLContextClientVersion(2);		// Sets openGLES version 2.
		setPreserveEGLContextOnPause(true);
		setRenderer(m_Renderer);			// Set the renderer for drawing on GLSurfaceView.
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); // Must call requestRender().	
	}

	@Override 
	public void surfaceCreated(SurfaceHolder holder)
	{
		super.surfaceCreated(holder);
		
		Log.e(TAG, "surfaceCreated");
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		
		Log.e(TAG, "onPause");
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		Log.e(TAG, "onResume");
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) 
	{
		super.surfaceDestroyed(holder);
	}
}
