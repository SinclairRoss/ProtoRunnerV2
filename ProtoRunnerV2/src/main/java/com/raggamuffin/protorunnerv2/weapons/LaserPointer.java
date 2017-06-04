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
		super(ModelType.LaserPointer, 1.0);

		m_Anchor = anchor;
		m_Barrel = barrel;

		SetForward(m_Anchor.GetForward());

        SetScale(0.05, 0.05, 1000);
        ;

        SetColour(game.GetColourManager().GetDangerColour());
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
        SetPosition(m_Anchor.GetPosition());
        SetForward(m_Anchor.GetForward());
        Rotate(m_Barrel.GetRotation());
    }

	@Override
	public boolean IsValid() 
	{
        return m_Anchor.GetAnchor().IsValid();
    }

    @Override
    public void CleanUp(){}

    public void On()
	{
        m_On = true;

        UpdateOrientation();
        SetAlpha(0.4);
	}
	
	public void Off()
	{
        m_On = false;
        SetAlpha(0);
	}	
}