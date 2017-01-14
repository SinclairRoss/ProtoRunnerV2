package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.renderer.ModelType;

public class LaserPointer extends GameObject
{
    private boolean m_On;

	private Weapon m_Anchor;
	private WeaponBarrel m_Barrel;

	public LaserPointer(GameLogic game, Weapon anchor, WeaponBarrel barrel)
	{
		super(game, ModelType.LaserPointer);

		m_Anchor = anchor;
		m_Barrel = barrel;

		SetForward(m_Anchor.GetForward());

        m_Scale.SetVector(0.3, 0.3, 1000);
		
		m_Anchor.AddChild(this);

        SetBaseColour(game.GetColourManager().GetDangerColour());
        m_Colour.Alpha = 0;
        m_Model = ModelType.Nothing;
    }
	
	@Override
	public void Update(double deltaTime)
	{
        if(m_On)
        {
            UpdateOrientation();
        }
	}

    private void UpdateOrientation()
    {
        m_Forward.SetVector(m_Anchor.GetForward());
        m_Forward.RotateY(m_Barrel.GetRotation());
        UpdateVectors();

        m_Velocity.SetVector(0);
        m_Position.SetVector(m_Anchor.GetPosition());
    }

	@Override
	public boolean IsValid() 
	{
        return m_Anchor.GetAnchor().IsValid();
    }

    @Override
    public void CleanUp()
    {}

    public void On()
	{
        m_On = true;
        m_Colour.Alpha = 0.4;

        UpdateOrientation();
        m_Model = ModelType.ParticleLaser;
	}
	
	public void Off()
	{
        m_On = false;
        m_Model = ModelType.Nothing;
	}	
}