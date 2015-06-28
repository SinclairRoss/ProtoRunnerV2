// Author: 	Sinclair Ross.
// Notes:	The purpose of ControlScheme is to put a layer between the game and the android code.
//			Any class that relies on player input will check if EVADE_LEFT has been activated rather than 
//			touches, gestures, accelerometer output etc.

package com.raggamuffin.protorunnerv2.master;

import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector2;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class ControlScheme 
{
	private static final int LEFT  = 0;
	private static final int RIGHT = 1;
	private static final int UP	   = 2;
	private static final int DOWN  = 3;
	
	///// Actions \\\\\
	private Publisher EvadeLeftPublisher;
    private Publisher EvadeRightPublisher;
    private Publisher StrafeLeftPublisher;
    private Publisher StrafeRightPublisher;
    private Publisher AfterburnersEngagePublisher;
    private Publisher AfterBurnersDisengagePublisher;
    private Publisher ReversePublisher;
    private Publisher ForwardPublisher;
    private Publisher FirePublisher;
    private Publisher CeaseFirePublisher;
    private Publisher WeaponLeftPublisher;
    private Publisher WeaponRightPublisher;
    private Publisher WeaponUpPublisher;
    private Publisher WeaponDownPublisher;

	///// Inputs \\\\\
	public static final int TOUCH_DOWN 			= 0;
	public static final int LONG_TOUCH_DOWN		= 1;
	public static final int TOUCH_UP			= 2;
	public static final int SWIPE_LEFT 		 	= 3;
	public static final int SWIPE_RIGHT 		= 4;
	public static final int DOUBLE_SWIPE_LEFT  	= 5;
	public static final int DOUBLE_SWIPE_RIGHT 	= 6;
	public static final int SWIPE_UP 		 	= 7;
	public static final int SWIPE_DOWN	 		= 8;
	public static final int DOUBLE_SWIPE_UP 	= 9;
	public static final int DOUBLE_SWIPE_DOWN 	= 10;

	private double m_Tilt;
	
	private Vector2 m_TouchCoords;
	
	private double m_DoubleTapTime;
	private int m_HalfWayX;
	
	private double m_DoubleSwipeTimerLeft[];
	private double m_DoubleSwipeTimerRight[];
	
	public ControlScheme(Context context, PubSubHub pubSub)
	{
		m_Tilt  = 0;

        EvadeLeftPublisher = pubSub.CreatePublisher(PublishedTopics.EvadeLeft);
        EvadeRightPublisher = pubSub.CreatePublisher(PublishedTopics.EvadeRight);
        StrafeLeftPublisher = pubSub.CreatePublisher(PublishedTopics.StrafeLeft);
        StrafeRightPublisher = pubSub.CreatePublisher(PublishedTopics.StrafeRight);
        AfterburnersEngagePublisher = pubSub.CreatePublisher(PublishedTopics.AfterBurnersEngage);
        AfterBurnersDisengagePublisher = pubSub.CreatePublisher(PublishedTopics.AfterBurnersDisengage);
        ReversePublisher = pubSub.CreatePublisher(PublishedTopics.Reverse);
        ForwardPublisher = pubSub.CreatePublisher(PublishedTopics.Forward);
        FirePublisher = pubSub.CreatePublisher(PublishedTopics.Fire);
        CeaseFirePublisher = pubSub.CreatePublisher(PublishedTopics.CeaseFire);
        WeaponLeftPublisher = pubSub.CreatePublisher(PublishedTopics.WeaponLeft);
        WeaponRightPublisher = pubSub.CreatePublisher(PublishedTopics.WeaponRight);
        WeaponUpPublisher = pubSub.CreatePublisher(PublishedTopics.WeaponUp);
        WeaponDownPublisher = pubSub.CreatePublisher(PublishedTopics.WeaponDown);

		m_DoubleSwipeTimerLeft = new double[4];
		m_DoubleSwipeTimerLeft[LEFT]  = 0.0;
		m_DoubleSwipeTimerLeft[RIGHT] = 0.0;
		m_DoubleSwipeTimerLeft[UP] 	  = 0.0;
		m_DoubleSwipeTimerLeft[DOWN]  = 0.0;
		
		m_DoubleSwipeTimerRight = new double[4];
		m_DoubleSwipeTimerRight[LEFT]  = 0.0;
		m_DoubleSwipeTimerRight[RIGHT] = 0.0; 
		m_DoubleSwipeTimerLeft[UP] 	   = 0.0;
		m_DoubleSwipeTimerLeft[DOWN]   = 0.0;
		
		m_DoubleTapTime = 0.75f;
		
		m_TouchCoords = new Vector2();

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display  = wm.getDefaultDisplay();
		
		Point size = new Point();
		display.getRealSize(size);
		m_HalfWayX = size.x / 2;
	}
	
	public void RegisterEvent(float eventOriginX, float eventOriginY, int event)
	{
		switch(event)
		{
			case TOUCH_DOWN:
				
				m_TouchCoords.SetVector(eventOriginX, eventOriginY);
						
				if(eventOriginX > m_HalfWayX)
				{
                    AfterburnersEngagePublisher.Publish();
				}
				else
				{
                    FirePublisher.Publish();
				}
				break;

			case TOUCH_UP:
				
				m_TouchCoords.SetVector(0.0);
				
				if(eventOriginX > m_HalfWayX)
				{
                    AfterBurnersDisengagePublisher.Publish();
				}
				else
				{
                    CeaseFirePublisher.Publish();
				}
					
				break;
				
			case SWIPE_LEFT: 	// Swipe Left.
				
				if(eventOriginX > m_HalfWayX)
				{
					if(m_DoubleSwipeTimerRight[LEFT] > 0.0)	// Register Double Swipe
					{
                        StrafeLeftPublisher.Publish();
					}
					else							// RegisterSingleSwipe.
					{
                        EvadeLeftPublisher.Publish();
						m_DoubleSwipeTimerRight[LEFT] = m_DoubleTapTime;
					}
				}
				else
				{
					if(m_DoubleSwipeTimerLeft[LEFT] > 0.0)	// Register Double Swipe
					{	

					}
					else							// RegisterSingleSwipe.
					{
                        WeaponLeftPublisher.Publish();
						m_DoubleSwipeTimerLeft[LEFT] = m_DoubleTapTime;
					}
				}
				break;
				
			case SWIPE_RIGHT: // Swipe Right.
				if(eventOriginX > m_HalfWayX)
				{
					if(m_DoubleSwipeTimerRight[RIGHT] > 0.0)	// Register Double Swipe
					{
                        StrafeRightPublisher.Publish();
					}
					else							// RegisterSingleSwipe.
					{
                        EvadeRightPublisher.Publish();
						m_DoubleSwipeTimerRight[RIGHT] = m_DoubleTapTime;
					}
				}
				else
				{
					if(m_DoubleSwipeTimerLeft[RIGHT] > 0.0)	// Register Double Swipe
					{	
						
					}
					else							// RegisterSingleSwipe.
					{
                        WeaponRightPublisher.Publish();
						m_DoubleSwipeTimerLeft[RIGHT] = m_DoubleTapTime;
					}
				}
				
				break;
				
			case SWIPE_UP: // Swipe Up.
				if(eventOriginX > m_HalfWayX)
				{
					if(m_DoubleSwipeTimerRight[UP] > 0.0)	// Register Double Swipe
					{	
						
					}
					else							// RegisterSingleSwipe.
					{
						ForwardPublisher.Publish();
						m_DoubleSwipeTimerRight[UP] = m_DoubleTapTime;
					}
				}
				else
				{
					if(m_DoubleSwipeTimerLeft[UP] > 0.0)	// Register Double Swipe
					{	

					}
					else							// RegisterSingleSwipe.
					{
                        WeaponUpPublisher.Publish();
						m_DoubleSwipeTimerLeft[UP] = m_DoubleTapTime;
					}
				}
				
				break;
				
			case SWIPE_DOWN: // Swipe Down.
				if(eventOriginX > m_HalfWayX)
				{
					if(m_DoubleSwipeTimerRight[DOWN] > 0.0)	// Register Double Swipe
					{

					}
					else							// RegisterSingleSwipe.
					{
                        ReversePublisher.Publish();
						m_DoubleSwipeTimerRight[DOWN] = m_DoubleTapTime;
					}
				}
				else
				{
					if(m_DoubleSwipeTimerLeft[DOWN] > 0.0)	// Register Double Swipe
					{	

					}
					else							// RegisterSingleSwipe.
					{
                        WeaponDownPublisher.Publish();
						m_DoubleSwipeTimerLeft[DOWN] = m_DoubleTapTime;
					}
				}
				break;
		}

	}
	
	public void Update(double DeltaTime)
	{
		// Update.
		m_DoubleSwipeTimerLeft[LEFT]   -= DeltaTime;
		m_DoubleSwipeTimerLeft[RIGHT]  -= DeltaTime;
		m_DoubleSwipeTimerLeft[UP] 	   -= DeltaTime;
		m_DoubleSwipeTimerLeft[DOWN]   -= DeltaTime;
		
		m_DoubleSwipeTimerRight[LEFT]  -= DeltaTime;
		m_DoubleSwipeTimerRight[RIGHT] -= DeltaTime;
		m_DoubleSwipeTimerRight[UP]    -= DeltaTime;
		m_DoubleSwipeTimerRight[DOWN]  -= DeltaTime;
		
		
		// Clamp.
		m_DoubleSwipeTimerLeft[LEFT]   = MathsHelper.Clamp(m_DoubleSwipeTimerLeft[LEFT],  0.0f, m_DoubleTapTime);
		m_DoubleSwipeTimerLeft[RIGHT]  = MathsHelper.Clamp(m_DoubleSwipeTimerLeft[RIGHT], 0.0f, m_DoubleTapTime);
		m_DoubleSwipeTimerLeft[UP]     = MathsHelper.Clamp(m_DoubleSwipeTimerLeft[UP],    0.0f, m_DoubleTapTime);
		m_DoubleSwipeTimerLeft[DOWN]   = MathsHelper.Clamp(m_DoubleSwipeTimerLeft[DOWN],  0.0f, m_DoubleTapTime);
		
		m_DoubleSwipeTimerRight[LEFT]  = MathsHelper.Clamp(m_DoubleSwipeTimerRight[LEFT],  0.0f, m_DoubleTapTime);
		m_DoubleSwipeTimerRight[RIGHT] = MathsHelper.Clamp(m_DoubleSwipeTimerRight[RIGHT], 0.0f, m_DoubleTapTime);
		m_DoubleSwipeTimerRight[UP]    = MathsHelper.Clamp(m_DoubleSwipeTimerRight[UP],    0.0f, m_DoubleTapTime);
		m_DoubleSwipeTimerRight[DOWN]  = MathsHelper.Clamp(m_DoubleSwipeTimerRight[DOWN],  0.0f, m_DoubleTapTime);
	}
	
	public Vector2 GetTouchCoordinates()
    {
        return m_TouchCoords;
    }
	
	public void RegisterTilt(double tilt)
	{
		m_Tilt = tilt;
	}

	public double GetTilt()
	{
		return m_Tilt;
	}
	
	public void ResetTouchCoordinates()
	{
		m_TouchCoords.SetVector(-10.0);
	}
}
