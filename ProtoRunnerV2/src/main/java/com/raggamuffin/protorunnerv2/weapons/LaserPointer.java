package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.RenderObjectType;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Timer;

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
	private WeaponBarrel m_Barrel;
	
	private Timer m_Timer;

    private final double m_MaxAlpha;

	public LaserPointer(GameLogic game, Weapon anchor, WeaponBarrel barrel)
	{
		super(game, ModelType.LaserPointer);

		m_Anchor = anchor;
		m_Barrel = barrel;

		m_Timer = new Timer(1.0);

		SetForward(m_Anchor.GetForward());
		
		m_Scale.SetVector(100.0);
		
		m_Anchor.AddChild(this);

        m_MaxAlpha = 0.5;

		Off();
	}
	
	@Override
	public void Update(double deltaTime)
	{
		super.Update(deltaTime);
		
		switch(m_State)
		{
			case Activating:
            {
                m_Timer.Update(deltaTime);
                m_BaseColour.Alpha = m_Timer.GetProgress() * m_MaxAlpha;

                if (m_Timer.TimedOut())
                {
                    m_BaseColour.Alpha = 1.0;
                    m_State = LaserState.Idle;
                }

                break;
            }
			case Deactivating:
            {
                m_Timer.Update(deltaTime);
                m_BaseColour.Alpha = m_Timer.GetInverseProgress() * m_MaxAlpha;

                if (m_Timer.TimedOut())
                {
                    m_BaseColour.Alpha = 0.0;
                    m_State = LaserState.Idle;
                }

                break;
            }
			case Idle:
            {
                // Do nothing.
                break;
            }
		}

        UpdateOrientation();
	}

    private void UpdateOrientation()
    {
        SetForward(m_Anchor.GetForward());
        m_Forward.RotateY(m_Barrel.GetRotation());
        m_Yaw = m_Forward.Yaw();
        SetPosition(m_Anchor.GetPosition());
    }

	@Override
	public boolean IsValid() 
	{
		return true;
	}

    @Override
    public void CleanUp()
    {

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
