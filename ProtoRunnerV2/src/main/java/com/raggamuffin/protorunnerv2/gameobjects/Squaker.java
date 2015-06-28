package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;

public class Squaker extends GameObject
{
	private FloorGrid m_FloorGrid;
	
	private final double m_TotalSquakTime = 1.5;
	private double m_SquakTimer;
	
	public Squaker(PubSubHub PubSub, GameAudioManager audio) 
	{
		super(PubSub, audio);
		
		m_Position.SetVector(0, 0, 10);

		AddChild(new FloorGrid(m_Colour));
		
		m_SquakTimer = m_TotalSquakTime;
	}
	
	@Override
	public void Update(double deltaTime)
	{
		m_SquakTimer -= deltaTime;
		
		if(m_SquakTimer <= 0.0)
		{
			m_SquakTimer += m_TotalSquakTime;
			m_GameAudioManager.PlaySound(m_Position, AudioClips.UIClick);
		}
		m_FloorGrid.Update(deltaTime);
	}

	@Override
	public boolean IsValid() 
	{
		return true;
	}
}
