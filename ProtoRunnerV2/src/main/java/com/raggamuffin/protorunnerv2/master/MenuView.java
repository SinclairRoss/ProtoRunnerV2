package com.raggamuffin.protorunnerv2.master;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MenuView extends SurfaceView implements SurfaceHolder.Callback
{
	public MenuView(Context context, RendererPacket packet)
	{
		super(context);

		getHolder().addCallback(this);
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		canvas.drawARGB(255,0,0,0);

		/// Probably not used.
		//invalidate();
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{
	
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{
		setWillNotDraw(false);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) 
	{
		
	}

}
