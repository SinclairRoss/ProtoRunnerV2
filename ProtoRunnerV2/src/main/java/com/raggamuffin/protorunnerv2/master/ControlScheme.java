// Author: 	Sinclair Ross.
// Notes:	The purpose of ControlScheme is to put a layer between the game and the android code.
//			Any class that relies on player input will check if EVADE_LEFT has been activated rather than 
//			touches, gestures, accelerometer output etc.

package com.raggamuffin.protorunnerv2.master;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

import java.util.ArrayList;

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

	private Publisher Publisher_OnPointerDown;
	private Publisher Publisher_OnPointerUp;

	///// Inputs \\\\\
	public static final int SWIPE_LEFT 		 	= 3;
	public static final int SWIPE_RIGHT 		= 4;
	public static final int DOUBLE_SWIPE_LEFT  	= 5;
	public static final int DOUBLE_SWIPE_RIGHT 	= 6;
	public static final int SWIPE_UP 		 	= 7;
	public static final int SWIPE_DOWN	 		= 8;
	public static final int DOUBLE_SWIPE_UP 	= 9;
	public static final int DOUBLE_SWIPE_DOWN 	= 10;

	private double m_Tilt;
	
	private double m_DoubleTapTime;
	private int m_HalfWayX;
	
	private double m_DoubleSwipeTimerLeft[];
	private double m_DoubleSwipeTimerRight[];

    private ArrayList<TouchPointer> m_ActiveTouchPointers;
    private ArrayList<TouchPointer> m_InactiveTouchPointers;

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

        Publisher_OnPointerDown = pubSub.CreatePublisher(PublishedTopics.OnPointerDown);
		Publisher_OnPointerUp = pubSub.CreatePublisher(PublishedTopics.OnPointerUp);

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

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display  = wm.getDefaultDisplay();
		
		Point size = new Point();
		display.getRealSize(size);
		m_HalfWayX = size.x / 2;

        m_ActiveTouchPointers = new ArrayList<>();
        m_InactiveTouchPointers = new ArrayList<>();
	}

	public void RegisterEvent_PointerDown(int pointerID, float eventOriginX, float eventOriginY)
    {
        TouchPointer pointer = CreateTouchPointer(pointerID, eventOriginX, eventOriginY);

        if(pointer.GetInitialPosition().X > 0)
        {
            AfterburnersEngagePublisher.Publish();
        }
        else
        {
            FirePublisher.Publish();
        }

        Publisher_OnPointerDown.Publish(pointer);
    }

    public void RegisterEvent_PointerUp(int pointerID)
    {
        TouchPointer pointer = GetTouchPointerWithID(pointerID);
        Publisher_OnPointerUp.Publish(pointer);

        if (pointer.GetInitialPosition().X > 0)
        {
            AfterBurnersDisengagePublisher.Publish();
        }
        else
        {
            CeaseFirePublisher.Publish();
        }

        RemoveTouchPointer(pointerID);
    }

    public void RegisterEvent_PointerMove(int pointerID, float x, float y)
    {
        TouchPointer pointer = GetTouchPointerWithID(pointerID);
        pointer.UpdatePosition(x, y);
    }

	public void RegisterEvent(float eventOriginX, int event)
	{
		switch(event)
		{
			case SWIPE_LEFT:
				
				if(eventOriginX > 0)
				{
					if(m_DoubleSwipeTimerRight[LEFT] > 0.0)
					{
                        StrafeLeftPublisher.Publish();
					}
					else
					{
                        EvadeLeftPublisher.Publish();
						m_DoubleSwipeTimerRight[LEFT] = m_DoubleTapTime;
					}
				}
				else
				{
					if(m_DoubleSwipeTimerLeft[LEFT] > 0.0)
					{}
					else
					{
                        WeaponLeftPublisher.Publish();
						m_DoubleSwipeTimerLeft[LEFT] = m_DoubleTapTime;
					}
				}
				break;
				
			case SWIPE_RIGHT: // Swipe Right.
				if(eventOriginX > 0)
				{
					if(m_DoubleSwipeTimerRight[RIGHT] > 0.0)
					{
                        StrafeRightPublisher.Publish();
					}
					else
					{
                        EvadeRightPublisher.Publish();
						m_DoubleSwipeTimerRight[RIGHT] = m_DoubleTapTime;
					}
				}
				else
				{
					if(m_DoubleSwipeTimerLeft[RIGHT] > 0.0)
					{}
					else
					{
                        WeaponRightPublisher.Publish();
						m_DoubleSwipeTimerLeft[RIGHT] = m_DoubleTapTime;
					}
				}
				
				break;
				
			case SWIPE_UP: // Swipe Up.
				if(eventOriginX > 0)
				{
					if(m_DoubleSwipeTimerRight[UP] > 0.0)
					{}
					else
					{
						ForwardPublisher.Publish();
						m_DoubleSwipeTimerRight[UP] = m_DoubleTapTime;
					}
				}
				else
				{
					if(m_DoubleSwipeTimerLeft[UP] > 0.0)
					{}
					else
					{
                        WeaponUpPublisher.Publish();
						m_DoubleSwipeTimerLeft[UP] = m_DoubleTapTime;
					}
				}
				
				break;
				
			case SWIPE_DOWN: // Swipe Down.
				if(eventOriginX > 0)
				{
					if(m_DoubleSwipeTimerRight[DOWN] > 0.0)
					{}
					else
					{
                        ReversePublisher.Publish();
						m_DoubleSwipeTimerRight[DOWN] = m_DoubleTapTime;
					}
				}
				else
				{
					if(m_DoubleSwipeTimerLeft[DOWN] > 0.0)
					{}
					else
					{
                        WeaponDownPublisher.Publish();
						m_DoubleSwipeTimerLeft[DOWN] = m_DoubleTapTime;
					}
				}
				break;
		}
	}

	public void Update(double deltaTime)
	{
		// Update.
		m_DoubleSwipeTimerLeft[LEFT]   -= deltaTime;
		m_DoubleSwipeTimerLeft[RIGHT]  -= deltaTime;
		m_DoubleSwipeTimerLeft[UP] 	   -= deltaTime;
		m_DoubleSwipeTimerLeft[DOWN]   -= deltaTime;
		
		m_DoubleSwipeTimerRight[LEFT]  -= deltaTime;
		m_DoubleSwipeTimerRight[RIGHT] -= deltaTime;
		m_DoubleSwipeTimerRight[UP]    -= deltaTime;
		m_DoubleSwipeTimerRight[DOWN]  -= deltaTime;

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

    private TouchPointer CreateTouchPointer(int pointerID, float x, float y)
    {
        TouchPointer pointer;
        if(m_InactiveTouchPointers.isEmpty())
        {
            pointer = new TouchPointer();
        }
        else
        {
            int inactivePointerCount = m_InactiveTouchPointers.size();
            pointer = m_InactiveTouchPointers.get(inactivePointerCount - 1);
            m_InactiveTouchPointers.remove(inactivePointerCount - 1);
        }

        pointer.Initialise(pointerID, x, y);
        m_ActiveTouchPointers.add(pointer);

        return  pointer;
    }

    public void ReleaseAllTouches()
    {
        int activePointerCount = m_ActiveTouchPointers.size();
        for(int i = 0; i < activePointerCount;)
        {
            TouchPointer pointer = m_ActiveTouchPointers.remove(i);
            pointer.CleanUp();
            m_InactiveTouchPointers.add(pointer);

            --activePointerCount;
        }
    }

    private void RemoveTouchPointer(int pointerID)
    {
       	TouchPointer pointer = GetTouchPointerWithID(pointerID);

		pointer.CleanUp();
        m_ActiveTouchPointers.remove(pointer);
        m_InactiveTouchPointers.add(pointer);
    }

    private TouchPointer GetTouchPointerWithID(int id)
    {
        TouchPointer touchPointer = null;
        int activePointerCount = m_ActiveTouchPointers.size();
        for(int i = 0; i < activePointerCount; ++i)
        {
            TouchPointer pointer = m_ActiveTouchPointers.get(i);
            if(pointer.GetId() == id)
            {
                touchPointer = pointer;
                break;
            }
        }

        return touchPointer;
    }

	public void RegisterTilt(double tilt) { m_Tilt = tilt; }
	public double GetTilt() { return m_Tilt; }

    public int GetActivePointerCount() { return m_ActiveTouchPointers.size(); }
	public ArrayList<TouchPointer> GetTouchPointers() { return m_ActiveTouchPointers; }
    public TouchPointer GetPointerAtIndex(int index) { return m_ActiveTouchPointers.get(index); }
}
