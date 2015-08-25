package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class LaserPointer extends GameObject
{
	private enum LaserState
	{
		Activating,
		Idle,
		Deactivating,
	}
	
	private LaserState m_State;

	private Weapon m_Anchor;
	private Vector3 m_Muzzle;
	
	private Timer m_Timer;

	public LaserPointer(Weapon Anchor, Vector3 Muzzle)
	{
		super(null, null);

		m_Anchor = Anchor;

        m_Model = ModelType.LaserPointer;

		m_Muzzle = Muzzle;
		m_Forward = Anchor.GetForward();
		
		m_Scale.SetVector(100.0);
		
		m_Anchor.AddChild(this);
		
		m_Timer = new Timer(1.0);

		Off();
	}
	
	@Override
	public void Update(double deltaTime)
	{
		super.Update(deltaTime);
		
		switch(m_State)
		{
			case Activating:
				m_Timer.Update(deltaTime);
				m_BaseColour.Alpha = m_Timer.GetProgress();
				
				if(m_Timer.TimedOut())
				{
					m_BaseColour.Alpha = 1.0;
					m_State = LaserState.Idle;
				}
				
				break;
				
			case Deactivating:
				m_Timer.Update(deltaTime);
				m_BaseColour.Alpha = m_Timer.GetInverseProgress();
				
				if(m_Timer.TimedOut())
				{
					m_BaseColour.Alpha = 0.0;
					m_State = LaserState.Idle;
				}
				
				break;
				
			case Idle:
				// Do nothing.
				break;
		}
		
		m_Position.SetVector(m_Anchor.GetMuzzlePosition(m_Muzzle));
		m_Yaw = m_Anchor.GetOrientation();
	}

	@Override
	public boolean IsValid() 
	{
		return true;
	}
	
	public void On()
	{
        SetBaseColour(m_Anchor.GetAnchor().GetBaseColour());

        GetAltColour().Alpha = 0.0;
		m_State = LaserState.Activating;
		m_Timer.ResetTimer();
	}
	
	public void Off()
	{
		m_State = LaserState.Deactivating;
		m_Timer.ResetTimer();	
	}	
}
