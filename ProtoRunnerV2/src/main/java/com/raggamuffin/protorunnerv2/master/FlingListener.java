package com.raggamuffin.protorunnerv2.master;

import java.util.ArrayList;

public class FlingListener 
{
	private ArrayList<FlingEvent> m_Events;
	private float m_ThresholdX;
	private float m_ThresholdY;
	
	public FlingListener(final float thresholdX, final float thresholdY)
	{
		m_Events = new ArrayList<>();
		
		m_ThresholdX = thresholdX;
		m_ThresholdY = thresholdY;
	}
	
	public void RegisterDown(int id, float x, float y)
	{
		FlingEvent event = new FlingEvent(id, x, y, System.currentTimeMillis());
		m_Events.add(event);
	}
	
	public FlingOutcomeType RegisterUp(int id, float x, float y)
	{
		FlingEvent event = GetFlingWithId(id);
		
		long deltaTime = System.currentTimeMillis() - event.GetTimeStamp();
		
		float velocityX = (x - event.GetX()) / (deltaTime * 0.001f);
		float velocityY = (y - event.GetY()) / (deltaTime * 0.001f);
	
		m_Events.remove(event);

		if(velocityX > m_ThresholdX)
		{
			return FlingOutcomeType.Right; 
		}
		
		if(velocityX < -m_ThresholdX)
		{
			return FlingOutcomeType.Left; 
		}
		
		if(velocityY > m_ThresholdY)
		{
			return FlingOutcomeType.Down; 
		}
		
		if(velocityY < -m_ThresholdY)
		{
			return FlingOutcomeType.Up; 
		}
		
		return FlingOutcomeType.None;
	}
	
	private FlingEvent GetFlingWithId(int index)
	{
		for (FlingEvent event : m_Events)
		{
			if(event.GetId() == index)
			{
				return event;
			}
		}
		
		return null;
	}
}
