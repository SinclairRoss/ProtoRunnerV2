// Author: 	Sinclair Ross
// Date:	23/09/2014

package com.raggamuffin.protorunnerv2.master;

import java.util.ArrayList;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.ChaseCamera;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.ui.UIElement;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity implements SensorEventListener
{
	public static final String TAG = "GameActivity";
	public static final int REQUEST_RENDER = 0;
	
	private final double GYRO_LIMIT = 3.0;
	
	private GameThread m_GameThread;
	private GameView m_GameView;
	private ControlScheme m_ControlScheme;
	
	// User input.
	private FlingListener m_FlingListener;
	private SensorManager m_SensorManager;
	private Sensor m_Accelerometer;

	private Handler m_Handler;
	
	private GameLogic m_GameLogic;

    @Override
	protected void onCreate(Bundle savedInstanceState) 
	{		
		super.onCreate(savedInstanceState);
		
		Log.e(TAG, "onCreate");

		m_Handler = new Handler(Looper.getMainLooper())
		{
			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				
				switch(msg.what)
				{
					case REQUEST_RENDER:
						StartRender();
						break;
				}
			}
		};

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		// Adds support for devices that cannot use emersive mode.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Log.e("Activity", "Refresh rate: " + getWindowManager().getDefaultDisplay().getRefreshRate());

		RendererPacket rendererPacket = new RendererPacket(this, new ChaseCamera(), new RenderEffectSettings());

        PubSubHub pubSub = new PubSubHub();
		m_ControlScheme = new ControlScheme(this, pubSub);
		m_FlingListener = new FlingListener(2000.0f, 2000.0f);
		
		// Game states.
		m_GameLogic = new GameLogic(this, pubSub, m_ControlScheme, rendererPacket);
		
		m_GameThread = new GameThread(m_GameLogic, m_ControlScheme, m_Handler);
		m_GameThread.start();

		m_GameView = new GameView(this, rendererPacket);
		setContentView(m_GameView);	
		
		m_SensorManager = (SensorManager) getSystemService(GameActivity.SENSOR_SERVICE);
		m_Accelerometer = m_SensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		m_SensorManager.registerListener(this, m_Accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }
	
	private void StartRender()
	{
		m_GameView.requestRender();
		m_Handler.removeMessages(REQUEST_RENDER);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) 
	{
	    super.onWindowFocusChanged(hasFocus);
	    
	    if (hasFocus) 
	    {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
	    }
	}

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.e(TAG, "onStart");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.e(TAG, "onStop");
    }

	@Override
	protected void onResume()
	{
		super.onResume();
		Log.e(TAG, "onResume");
		
		m_GameView.onResume();
		m_GameThread.resumeThread();
		m_SensorManager.registerListener(this, m_Accelerometer, SensorManager.SENSOR_DELAY_GAME);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		Log.e(TAG, "onPause");
		
		m_GameView.onPause();
		
		m_SensorManager.unregisterListener(this);
		m_GameThread.pauseThread();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		Log.e(TAG, "onDestroy");

		m_GameThread.DestroyThread();

		boolean Retry = true;
		
		while(Retry)
		{
			try 
			{
				Log.e(TAG, "Thread Join");
				m_GameThread.join();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			finally
			{
				Log.e(TAG, "Thread Interrupt");
				Retry = false;
				m_GameThread.interrupt();
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) 
	{
	
	}

	@Override
	public void onSensorChanged(SensorEvent event) 
	{
		double tilt = MathsHelper.Clamp(event.values[1], -GYRO_LIMIT, GYRO_LIMIT);
		tilt 		= MathsHelper.SignedNormalise(tilt, -GYRO_LIMIT, GYRO_LIMIT);
		
		m_ControlScheme.RegisterTilt(tilt);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		super.onTouchEvent(event);
		
		int action 		= MotionEventCompat.getActionMasked(event);
		int index 		= MotionEventCompat.getActionIndex(event);
		int id			= event.getPointerId(index);

		float x = MotionEventCompat.getX(event, index);
		float y = MotionEventCompat.getY(event, index);
		
		switch(action)
		{
			case MotionEvent.ACTION_POINTER_DOWN: 
			case MotionEvent.ACTION_DOWN:
				m_ControlScheme.RegisterEvent(x, y, ControlScheme.TOUCH_DOWN);
				m_FlingListener.RegisterDown(id, x, y);
				break;
			case MotionEvent.ACTION_POINTER_UP:
			case MotionEvent.ACTION_UP:
				m_ControlScheme.RegisterEvent(x, y, ControlScheme.TOUCH_UP);

				FlingOutcomeType fling = m_FlingListener.RegisterUp(id, x, y);
				
				switch(fling)
				{
					case Down:
						m_ControlScheme.RegisterEvent(x, y, ControlScheme.SWIPE_DOWN);
						break;
						
					case Left:
						m_ControlScheme.RegisterEvent(x, y, ControlScheme.SWIPE_LEFT);					
						break;
					
					case Right:
						m_ControlScheme.RegisterEvent(x, y, ControlScheme.SWIPE_RIGHT);	
						break;
						
					case Up:
						m_ControlScheme.RegisterEvent(x, y, ControlScheme.SWIPE_UP);
						break;
						
					case None:
						break;
				}

				break;
		}

		return false;
	}
}
